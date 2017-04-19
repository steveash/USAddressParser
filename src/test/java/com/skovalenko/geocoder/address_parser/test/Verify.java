package com.skovalenko.geocoder.address_parser.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.Assert;

public class Verify {
	public static <T> void listsAreEqual(List<T> expected, List<T> found) {
		Assert.assertEquals("Lists are different lengths", expected.size(), found.size());
		for(T item : expected)
		{
			Assert.assertTrue("Object not found in list: [" + item + "]", found.contains(item));
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> void objectsAreEqual(T expected, T found, HashSet<String> notNullFields,
			HashSet<String> ignoredFields, HashMap objectMap, SerializationUtils serializationUtils,
			String objectPath) {

		if (expected == found) {
			return;
		}

		Object mappedObject = objectMap.get(found);
		if (mappedObject != null) {
			Assert.assertTrue(objectPath + " (" + found + " -> " + mappedObject +
					") didn't match with previous find (" + expected + ")", mappedObject == expected);
			return;
		}
		objectMap.put(found, expected);
		Class objectClass = expected.getClass();
		List<Method> getters = serializationUtils.getGettersWithFieldsAndSetters(objectClass);
		try
		{
			for (Method method : getters) {
				String field = SerializationUtils.getFieldNameForGetter(method);
				if (!ignoredFields.contains(field)) {
					Object value = method.invoke(found, new Object[0]);
					String fieldPath = objectPath + "." + field;
					String assertionPrefix = fieldPath;
					if (notNullFields.contains(field)) {
						Assert.assertNotNull(assertionPrefix + " was null", value);
					}
					else {
						Object expectedValue = method.invoke(expected, new Object[0]);
						valuesAreEqual(assertionPrefix, expectedValue, value,
								notNullFields, ignoredFields,
								objectMap, serializationUtils, fieldPath);
					}
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Collection tryToGetSortedCollection(Set set) {
		Collection result = set;
		if (set.size() > 0) {
			Object firstObject = set.iterator().next();
			Comparator comparator = null;
			boolean naturalSort = false;
			if (firstObject instanceof Comparable) {
				naturalSort = true;
			}
			if (comparator != null || naturalSort) {
				ArrayList list = new ArrayList(set);
				if (naturalSort) {
					Collections.sort(list);
				}
				else {
					Collections.sort(list, comparator);
				}
				result = list;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static void valuesAreEqual(String assertionPrefix, Object expectedValue,
			Object value, HashSet<String> notNullFields,
			HashSet<String> ignoredFields, HashMap objectMap, SerializationUtils serializationUtils,
			String fieldPath) {

		if (expectedValue == null) {
			Assert.assertNull(assertionPrefix + " was not null", value);
		}
		else {
			Assert.assertNotNull(assertionPrefix + " was null", value);
			Class valueClass = expectedValue.getClass();
			Assert.assertEquals(assertionPrefix + " had wrong class", valueClass, value.getClass());
			if (SerializationUtils.isAddressParserClass(valueClass)) {
				objectsAreEqual(expectedValue, value, notNullFields, ignoredFields,
						objectMap, serializationUtils, fieldPath);
			}
			else if (Collection.class.isAssignableFrom(valueClass)) {
				Collection expectedCollection = (Collection) expectedValue;
				Collection collection = (Collection) value;
				Assert.assertEquals(assertionPrefix + " had wrong size", expectedCollection.size(), collection.size());

				if (expectedCollection instanceof Set &&
					!(expectedCollection instanceof SortedSet)) {
					expectedCollection = tryToGetSortedCollection((Set) expectedCollection);
					collection = tryToGetSortedCollection((Set) collection);
				}

				Iterator expectedIterator = expectedCollection.iterator();
				Iterator iterator = collection.iterator();
				int index = 0;
				while (expectedIterator.hasNext()) {
					Object expectedSubValue = expectedIterator.next();
					Object subValue = iterator.next();
					valuesAreEqual(assertionPrefix + " (index = " + index + ")",
							expectedSubValue, subValue, notNullFields, ignoredFields,
							objectMap, serializationUtils, fieldPath + "[" + index + "]");
					index++;
				}
			}
			else if (Map.class.isAssignableFrom(valueClass)) {
				Map expectedMap = (Map) expectedValue;
				Map map = (Map) value;
				Assert.assertEquals(assertionPrefix + " had wrong size", expectedMap.size(), map.size());
				Collection expectedKeys = expectedMap.keySet();
				Collection keys = map.keySet();
				if (!(map instanceof SortedMap)) {
					expectedKeys = tryToGetSortedCollection((Set) expectedKeys);
					keys = tryToGetSortedCollection((Set) keys);
				}

				Iterator expectedIterator = expectedKeys.iterator();
				Iterator iterator = keys.iterator();
				int index = 0;
				while (expectedIterator.hasNext()) {
					Object expectedKey = expectedIterator.next();
					Object key = iterator.next();
					valuesAreEqual(assertionPrefix + " (key index = " + index + ")",
						expectedKey, key, notNullFields, ignoredFields,
						objectMap, serializationUtils, fieldPath + ".keys[" + index + "]");
					Object expectedSubValue = expectedMap.get(expectedKey);
					Object subValue = map.get(key);
					valuesAreEqual(assertionPrefix + " (key = " + key + ")",
							expectedSubValue, subValue, notNullFields, ignoredFields,
							objectMap, serializationUtils, fieldPath + "['" + key + "']");
				}
			}
			else {
				Assert.assertEquals(assertionPrefix + " had wrong value", expectedValue, value);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> void objectsAreEqual(T expected, T found) {
		objectsAreEqual(expected, found, new HashSet<String>(), new HashSet<String>(),
				new HashMap(), new SerializationUtils(), "root");
	}
}
