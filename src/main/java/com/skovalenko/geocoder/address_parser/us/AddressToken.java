package com.skovalenko.geocoder.address_parser.us;

public class AddressToken {

	private String token;

	private boolean isNumeric = false;

	private boolean isPartialNumeric = false;

	private HintTypes hint = HintTypes.HINT_NONE;

	public AddressToken(String tokenData) {
		token = tokenData;
		isNumeric = isNumericTest(token);
		isPartialNumeric = isPartialNumericTest(token);
	}

	public HintTypes getHint() {
		return hint;
	}

	public void setHint(HintTypes hints) {
		this.hint = hints;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		isNumeric = isNumericTest(token);
		isPartialNumeric = isPartialNumericTest(token);
	}

	private static boolean isNumericTest(String token) {
		return token.matches("^\\d+$");
	}

	private static boolean isPartialNumericTest(String token) {
		// formerly "^.*\\d+.*$" , but what about 229 R0ute 202 /0-not-O/
		return token.matches("^\\d+.*$");
	}

	public boolean isNumeric() {
		return isNumeric;
	}

	public boolean isPartialNumeric() {
		return isPartialNumeric;
	}

	public enum HintTypes {
		HINT_NONE, HINT_STREET_NUMBER, HINT_STREET_DIRECTION, HINT_STREET_TYPE, HINT_SU_TYPE, HINT_SU_NUMBER, HINT_ROUTE_TYPE, HINT_ROUTE_NUMBER, HINT_CITY, HINT_STATE, HINT_ZIP, HINT_ZIP4, HINT_COUNTY, HINT_COUNTRY, HINT_NUMERIC_WORD, HINT_NUMERIC, HINT_STREET_NAME, HINT_STREET_PREDIR, HINT_STREET_POSTDIR
	}
}
