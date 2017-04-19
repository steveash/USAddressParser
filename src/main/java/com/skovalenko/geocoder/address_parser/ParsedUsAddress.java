package com.skovalenko.geocoder.address_parser;

public class ParsedUsAddress extends UsAddress {

	private String subUnitName = "";

	private String subUnitNumber = "";

	/**
	 * This constructor is required by Spring XFire (this object is used in
	 * WebServices).
	 */
	public ParsedUsAddress() {
	}

	public ParsedUsAddress(String streetNumber, String streetPreDir,
			String streetName, String streetType, String streetPostDir,
			String subUnitName, String subUnitNumber, String city,
			String state, String zip, String zip4) {
		super(streetNumber, streetPreDir, streetName, streetType,
				streetPostDir, city, state, zip, zip4);
		this.subUnitName = subUnitName == null ? "" : subUnitName;
		this.subUnitNumber = subUnitNumber == null ? "" : subUnitNumber;
	}

	public ParsedUsAddress(String streetNumber, String streetPreDir,
			String streetName, String streetType, String streetPostDir,
			String subUnitName, String subUnitNumber) {
		this(streetNumber, streetPreDir, streetName, streetType, streetPostDir,
				subUnitName, subUnitNumber, "", "", "", "");
	}

	public String getSubUnitName() {
		return subUnitName;
	}

	public void setSubUnitName(String subUnitName) {
		if (subUnitName != null) {
			this.subUnitName = subUnitName.toUpperCase();
		} else {
			this.subUnitName = "";
		}
	}

	public String getSubUnitNumber() {
		return subUnitNumber;
	}

	public void setSubUnitNumber(String subUnitNumber) {
		if (subUnitNumber != null) {
			this.subUnitNumber = subUnitNumber.toUpperCase();
		} else {
			this.subUnitNumber = "";
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(FIELD_SEPARATOR).append(subUnitName).append(FIELD_SEPARATOR)
				.append(subUnitNumber);
		return sb.toString();
	}

	public static String getHeader() {
		StringBuffer sb = new StringBuffer(UsAddress.getHeader());
		sb.append(FIELD_SEPARATOR).append("Sub Unit Name").append(
				FIELD_SEPARATOR).append("Sub Unit Number");
		return sb.toString();
	}

	public ParsedUsAddress clone() {
		return new ParsedUsAddress(streetNumber, streetPreDir, streetName,
				streetType, streetPostDir, subUnitName, subUnitNumber, city,
				state, zip, zip4);
	}
}
