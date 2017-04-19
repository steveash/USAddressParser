package com.skovalenko.geocoder.address_parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import com.skovalenko.geocoder.address_parser.UsAddress;

public class UsAddressTestBase {

	@SuppressWarnings("unchecked")
	public static void checkNoNulls(UsAddress address) throws Exception {
		Method methods[] = address.getClass().getMethods();
		for (int j = 0; j < methods.length; j++) {
			String methodName = methods[j].getName();
			Class paramClasses[] = methods[j].getParameterTypes();
			if (paramClasses.length == 1
					&& paramClasses[0].getName().equals("java.lang.String")) {
				if (methodName.length() > 3
						&& methodName.substring(0, 3).equals("get")) {
					assertNotNull(methods[j].invoke(address));
				}
				if (methodName.length() > 3
						&& methodName.substring(0, 3).equals("set")) {
					methods[j].invoke(address, (String) null);
					// check getter
					Method getter = address.getClass().getMethod(
							"get" + methodName.substring(3));
					assertNotNull(getter.invoke(address));
				}
			}
		}
	}

	public static void checkIntStreetNum(UsAddress address) {
		address.setStreetNumber(null);
		assertEquals(address.intStreetNumber(), -1);
		address.setStreetNumber("ABCD");
		assertEquals(address.intStreetNumber(), -1);
		address.setStreetNumber("0123456");
		assertEquals(address.intStreetNumber(), 123456);
	}

	public static void checkIsValid(UsAddress address) throws Exception {
		UsAddress a = address.getClass().newInstance();
		assertFalse(a.checkValid());
		a.setStreetName("streetName");
		a.setStreetNumber("0012345");
		a.setCity("city");
		assertTrue(a.checkValid());
		a = address.getClass().newInstance();
		a.setStreetName("streetName");
		a.setStreetNumber("StreetNumber");
		a.setZip("12345");
		assertFalse(a.checkValid());
		a = address.getClass().newInstance();
		a.setStreetName("streetName");
		a.setStreetNumber("123");
		a.setZip("12345");
		assertTrue(a.checkValid());
	}
}
