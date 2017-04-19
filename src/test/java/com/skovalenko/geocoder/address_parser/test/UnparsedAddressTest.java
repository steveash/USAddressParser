package com.skovalenko.geocoder.address_parser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.skovalenko.geocoder.address_parser.UnparsedAddress;

public class UnparsedAddressTest {

	@Test
	public void testUnparsedAddress() {
		// test for not null arguments
		UnparsedAddress ua = new UnparsedAddress(null, null, null);
		assertNotNull(ua.getAddressLine());
		assertNotNull(ua.getCity());
		assertNotNull(ua.getZip());
		ua.setAddressLine(null);
		ua.setCity(null);
		ua.setZip(null);
		assertNotNull(ua.getAddressLine());
		assertNotNull(ua.getCity());
		assertNotNull(ua.getZip());
		// test for constructor arguments order
		ua = new UnparsedAddress("address", "city", "zip");
		assertTrue(ua.getAddressLine().equalsIgnoreCase("address"));
		assertTrue(ua.getCity().equalsIgnoreCase("city"));
		assertTrue(ua.getZip().equalsIgnoreCase("zip"));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UnparsedAddressTest.class);
	}
}
