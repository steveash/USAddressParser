package com.skovalenko.geocoder.address_parser.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class SerializationUtils {

	private Map<Class, List<Method>> gettersMap = new HashMap<Class, List<Method>>();
	private Map<Class, List<Field>> fieldsMap = new HashMap<Class, List<Field>>();

	protected void getGettersAndFieldsForType(Class type, ArrayList<Method> gettersList,
			ArrayList<Field> fieldsList, HashMap<String, Method> getters, HashSet<String> setters) {

		if (!type.equals(Object.class)) {
			for (Field field : type.getDeclaredFields()) {
				String fieldName = field.getName().toUpperCase();
				if (getters.containsKey(fieldName) && setters.contains(fieldName) ) {
					gettersList.add(getters.get(fieldName));
					fieldsList.add(field);
				}
			}
			getGettersAndFieldsForType(type.getSuperclass(), gettersList, fieldsList, getters, setters);
		}
	}

	protected void populateGettersAndFieldsWithSetters(Class type) {

		ArrayList<Method> gettersList = new ArrayList<Method>();
		ArrayList<Field> fieldsList = new ArrayList<Field>();

		HashMap<String, Method> getters = new HashMap<String, Method>();
		HashSet<String> setters = new HashSet<String>();
		for (Method method : type.getMethods()) {
			int numParameters = method.getParameterTypes().length;
			String methodName = method.getName();
			if (numParameters == 0 && methodName.startsWith("get")) {
				getters.put(method.getName().substring(3).toUpperCase(), method);
			}
			else if (numParameters == 0 && methodName.startsWith("is")) {
				Class returnType = method.getReturnType();
				if (returnType.equals(Boolean.class) || returnType.equals(Boolean.TYPE)) {
					getters.put(method.getName().substring(2).toUpperCase(), method);
				}
			}
			else if (numParameters == 1 && method.getName().startsWith("set")) {
				setters.add(method.getName().substring(3).toUpperCase());
			}
		}
		getGettersAndFieldsForType(type, gettersList, fieldsList, getters, setters);

		gettersMap.put(type, gettersList);
		fieldsMap.put(type, fieldsList);
	}

	public static boolean isProxy(Class type) {
	    return type.getName().indexOf("$$EnhancerByCGLIB$$") > 0
	            || type.getName().indexOf("CGLIBMapper.Marker") > 0;

	}

	public static Class convertClass(Class type) {
		if (isProxy(type)) {
			return type.getSuperclass();
		}
		else {
			return type;
		}

	}

	public static boolean isAddressParserClass(Class type) {
		return type.getName().startsWith("com.skovalenko.geocoder");
	}


	public List<Field> getFieldsWithGettersAndSetters(Class type) {
		type = convertClass(type);
		List<Field> result = fieldsMap.get(type);
		if (result == null) {
			populateGettersAndFieldsWithSetters(type);
			result = fieldsMap.get(type);
		}
		return result;
	}

	public List<Method> getGettersWithFieldsAndSetters(Class type) {
		type = convertClass(type);
		List<Method> result = gettersMap.get(type);
		if (result == null) {
			populateGettersAndFieldsWithSetters(type);
			result = gettersMap.get(type);
		}
		return result;
	}

	public static interface ObjectVisitor {
		void visitObject(Object object, boolean isProxy);
	}

	@SuppressWarnings("unchecked")
	public void visitObjects(Object object, Set visitedObjects, ObjectVisitor visitor) {
		Class objectClass = object.getClass();
		boolean isProxy = isProxy(objectClass);
		if (isProxy || isAddressParserClass(objectClass)) {
			if (!visitedObjects.contains(object)) {
				visitedObjects.add(object);
				visitor.visitObject(object, isProxy);
				List<Method> getters = getGettersWithFieldsAndSetters(objectClass);

				for (Method method : getters) {
					try
					{
						Object result = method.invoke(object, new Object[0]);
						if (result != null) {
							visitObjects(result, visitedObjects, visitor);
						}
					}
					catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					}
					catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		else if (object instanceof Object[]) {
			for (Object child : (Object[]) object) {
				visitObjects(child, visitedObjects, visitor);
			}
		}
		else if (object instanceof Collection) {
			for (Object child : (Collection) object) {
				visitObjects(child, visitedObjects, visitor);
			}
		}
		else if (object instanceof Map) {
			Map map = (Map) object;
			visitObjects(map.keySet(), visitedObjects, visitor);
			visitObjects(map.values(), visitedObjects, visitor);
		}
	}

	public static String getFieldNameForGetter(Method method) {
		String methodName = method.getName();
		int index = (methodName.startsWith("get") ? 3 : 2);
		return Character.toLowerCase(methodName.charAt(index)) + method.getName().substring(index + 1);
	}
}
