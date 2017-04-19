package com.skovalenko.geocoder.address_parser.test;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;

public class ParsedUsAddressTest {

	@Test
	public void testParsedUsAddress() throws Exception {
		UsAddressTestBase.checkNoNulls(new ParsedUsAddress(null, null, null, null,
				null, null, null, null, null, null, null));
		UsAddressTestBase.checkNoNulls(new ParsedUsAddress(null, null, null, null,
				null, null, null));
		UsAddressTestBase.checkNoNulls(new ParsedUsAddress());
		ParsedUsAddress pa = new ParsedUsAddress("streetNumber",
				"streetPreDir", "streetName", "streetType", "streetPostDir",
				"subUnitName", "subUnitNumber", "city", "state", "zip", "zip4");
		// ensure order of constructor agruments
		assertTrue("City".equalsIgnoreCase(pa.getCity()));
		assertTrue("State".equalsIgnoreCase(pa.getState()));
		assertTrue("Streetname".equalsIgnoreCase(pa.getStreetName()));
		assertTrue("Streetnumber".equalsIgnoreCase(pa.getStreetNumber()));
		assertTrue("Streetpostdir".equalsIgnoreCase(pa.getStreetPostDir()));
		assertTrue("Streetpredir".equalsIgnoreCase(pa.getStreetPreDir()));
		assertTrue("Streettype".equalsIgnoreCase(pa.getStreetType()));
		assertTrue("Zip".equalsIgnoreCase(pa.getZip()));
		assertTrue("Zip4".equalsIgnoreCase(pa.getZip4()));
		assertTrue("Subunitname".equalsIgnoreCase(pa.getSubUnitName()));
		assertTrue("Subunitnumber".equalsIgnoreCase(pa.getSubUnitNumber()));
		// verify intStreetNumber
		UsAddressTestBase.checkIntStreetNum(pa);
		pa = new ParsedUsAddress("streetNumber", "streetPreDir", "streetName",
				"streetType", "streetPostDir", "subUnitName", "subUnitNumber");
		// ensure order of constructor agruments
		assertTrue("Streetname".equalsIgnoreCase(pa.getStreetName()));
		assertTrue("Streetnumber".equalsIgnoreCase(pa.getStreetNumber()));
		assertTrue("Streetpostdir".equalsIgnoreCase(pa.getStreetPostDir()));
		assertTrue("Streetpredir".equalsIgnoreCase(pa.getStreetPreDir()));
		assertTrue("Streettype".equalsIgnoreCase(pa.getStreetType()));
		assertTrue("Subunitname".equalsIgnoreCase(pa.getSubUnitName()));
		assertTrue("Subunitnumber".equalsIgnoreCase(pa.getSubUnitNumber()));
		// verify clone method
		ParsedUsAddress pa2 = pa.clone();
		assertTrue(pa2 != pa);
		// slava - Verify class is invisible here
		// Verify.objectsAreEqual(pa2, pa, null, null);
		UsAddressTestBase.checkIsValid(pa);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ParsedUsAddressTest.class);
	}
}
