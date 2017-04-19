package com.skovalenko.geocoder.address_parser;

public abstract class UsAddress {

	static final String FIELD_SEPARATOR = "|";

	protected String streetNumber = "";

	protected String streetPreDir = "";

	protected String streetName = "";

	protected String streetType = "";

	protected String streetPostDir = "";

	protected String city = "";

	protected String state = "";

	protected String zip = "";

	protected String zip4 = "";

	protected String county = "";

	protected String country = "USA";

	/**
	 * This constructor is required by Spring XFire (this object is used in
	 * WebServices).
	 */
	public UsAddress() {
	}

	public UsAddress(String streetNumber, String streetPreDir,
			String streetName, String streetType, String streetPostDir,
			String city, String state, String zip, String zip4) {
		this.streetNumber = (streetNumber == null) ? "" : streetNumber
				.toUpperCase();
		this.streetPreDir = (streetPreDir == null) ? "" : streetPreDir
				.toUpperCase();
		this.streetName = (streetName == null) ? "" : streetName.toUpperCase();
		this.streetType = (streetType == null) ? "" : streetType.toUpperCase();
		this.streetPostDir = (streetPostDir == null) ? "" : streetPostDir
				.toUpperCase();
		this.city = (city == null) ? "" : city.toUpperCase();
		this.state = (state == null) ? "" : state.toUpperCase();
		this.zip = (zip == null) ? "" : zip.toUpperCase();
		this.zip4 = (zip4 == null) ? "" : zip4.toUpperCase();
	}

	public UsAddress(String streetNumber, String streetPreDir,
			String streetName, String streetType, String streetPostDir) {
		this(streetNumber, streetPreDir, streetName, streetType, streetPostDir,
				"", "", "", "");
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if (city != null) {
			this.city = city.toUpperCase();
		} else {
			this.city = "";
		}
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if (state != null) {
			this.state = state.toUpperCase();
		} else {
			this.state = "";
		}
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		if (streetName != null) {
			this.streetName = streetName.toUpperCase();
		} else {
			this.streetName = "";
		}
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public int intStreetNumber() {
		int intStreetNumber = -1;
		try {
			intStreetNumber = Integer.parseInt(streetNumber);
		} catch (NumberFormatException nfe) {
		}
		return intStreetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		if (streetNumber != null) {
			this.streetNumber = streetNumber.toUpperCase();
		} else {
			this.streetNumber = "";
		}
	}

	public String getStreetPostDir() {
		return streetPostDir;
	}

	public void setStreetPostDir(String streetPostDir) {
		if (streetPostDir != null) {
			this.streetPostDir = streetPostDir.toUpperCase();
		} else {
			this.streetPostDir = "";
		}
	}

	public String getStreetPreDir() {
		return streetPreDir;
	}

	public void setStreetPreDir(String streetPreDir) {
		if (streetPreDir != null) {
			this.streetPreDir = streetPreDir.toUpperCase();
		} else {
			this.streetPreDir = "";
		}
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		if (streetType != null) {
			this.streetType = streetType.toUpperCase();
		} else {
			this.streetType = "";
		}
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		if (zip != null) {
			this.zip = zip.toUpperCase();
		} else {
			this.zip = "";
		}
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		if (zip4 != null) {
			this.zip4 = zip4.toUpperCase();
		} else {
			this.zip4 = "";
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(zip).append(FIELD_SEPARATOR).append(zip4).append(
				FIELD_SEPARATOR).append(state).append(FIELD_SEPARATOR).append(
				city).append(FIELD_SEPARATOR).append(streetNumber).append(
				FIELD_SEPARATOR).append(streetName).append(FIELD_SEPARATOR)
				.append(streetPreDir).append(FIELD_SEPARATOR)
				.append(streetType).append(FIELD_SEPARATOR).append(
						streetPostDir);
		return sb.toString();
	}

	protected static String getHeader() {
		StringBuffer sb = new StringBuffer();
		sb.append("Zip").append(FIELD_SEPARATOR).append("Zip4").append(
				FIELD_SEPARATOR).append("State").append(FIELD_SEPARATOR)
				.append("City").append(FIELD_SEPARATOR).append("Street Number")
				.append(FIELD_SEPARATOR).append("Street Name").append(
						FIELD_SEPARATOR).append("Street Pre Dir").append(
						FIELD_SEPARATOR).append("Street Type").append(
						FIELD_SEPARATOR).append("Street Post Dir");
		return sb.toString();
	}

	public boolean checkValid() {
		// street name present ?
		if (streetName.length() == 0) {
			return false;
		}
		// street number present ?
		if (streetNumber.length() == 0 || !streetNumber.matches("^\\d+$")) {
			return false;
		}
		// city of zip present ?
		if (city.length() == 0 && zip.length() == 0) {
			return false;
		}
		return true;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		if (county != null) {
			this.county = county.toUpperCase();
		} else {
			this.county = "";
		}
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country != null) {
			this.country = country.toUpperCase();
		} else {
			this.country = "USA";
		}
	}

	public String getFullZip() {
		StringBuilder builder = new StringBuilder();
		appendPortion(builder, getZip(), false);
		appendPortion(builder, getZip4(), true, "-");
		return builder.toString();
	}

	/*
	 * Returns a formatted string containing all specified street fields in the
	 * appropriate order.
	 */
	public String getFullStreet() {
		// if we've been assigned one explicitly, just use that
		if (fullStreet != null) {
			return fullStreet;
		}

		StringBuilder builder = new StringBuilder();
		appendPortion(builder, getStreetNumber(), false);
		appendPortion(builder, getStreetPreDir());
		appendPortion(builder, getStreetName());
		appendPortion(builder, getStreetType());
		appendPortion(builder, getStreetPostDir());
		// appendPortion(builder, getSubUnitName());
		// appendPortion(builder, getSubUnitNumber());
		return builder.toString();
	}

	// HACK: this is used to create a stub fetched address when you're
	// creating from address history, and which you don't have a parsed
	// address string
	private String fullStreet;

	public void setFullStreet(String fullStreet) {
		this.fullStreet = fullStreet;
	}

	private void appendPortion(StringBuilder builder, String portion) {
		appendPortion(builder, portion, true);
	}

	private void appendPortion(StringBuilder builder, String portion,
			boolean insertDelimiter) {
		appendPortion(builder, portion, insertDelimiter, " ");
	}

	private void appendPortion(StringBuilder builder, String portion,
			boolean insertDelimiter, String delimiter) {
		if (isBlank(portion)) {
			return;
		}
		if (insertDelimiter && builder.length() > 0) {
			builder.append(delimiter);
		}
		builder.append(portion);
	}

	private boolean isBlank(String string) {
		return string == null || string.trim().length() == 0;
	}

	public boolean equals(UsAddress address) {
		return this.streetNumber.equalsIgnoreCase(address.getStreetNumber())
				&& this.streetPreDir
						.equalsIgnoreCase(address.getStreetPreDir())
				&& this.streetName.equalsIgnoreCase(address.getStreetName())
				&& this.streetType.equalsIgnoreCase(address.getStreetType())
				&& this.streetPostDir.equalsIgnoreCase(address
						.getStreetPostDir())
				&& this.city.equalsIgnoreCase(address.getCity())
				&& this.state.equalsIgnoreCase(address.getState())
				&& this.zip.equalsIgnoreCase(address.getZip())
				&& this.zip4.equalsIgnoreCase(address.getZip4())
				&& this.county.equalsIgnoreCase(address.getCounty())
				&& this.country.equalsIgnoreCase(address.getCountry());
	}
}
