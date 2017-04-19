package com.skovalenko.geocoder.address_parser.us;

public class NumberParser {
	public static ParsedNumber parseAlphabeticNumber(String numberWords) {
		ParsedNumber pn = new ParsedNumber();
		pn.setNumberString(numberWords);
		pn.setSuffix(getNumberSuffix(numberWords));
		pn.setNumber(parseValue(numberWords));
		return pn;
	}

	private static String getNumberSuffix(String numberWords) {
		if (numberWords.endsWith("ST"))
			return "ST";
		if (numberWords.endsWith("ND"))
			return "ND";
		if (numberWords.endsWith("RD"))
			return "RD";
		if (numberWords.endsWith("TH"))
			return "TH";
		return "";
	}

	private static long parseValue(String valueString) {
		int ones = 0;
		int tens = 0;
		int hundreds = 0;
		int hundredsPosition = valueString.indexOf("HUNDRED");
		int hundredsPositionOffset = hundredsPosition + 7;
		int hundredthPosition = valueString.indexOf("HUNDREDTH");
		int hundredthPositionOffset = hundredsPosition + 9;
		String hundredsString;
		String tensOnesString;
		if (hundredsPosition > -1) {
			hundredsString = valueString.substring(0, hundredsPosition).trim();
			tensOnesString = valueString.substring(hundredsPositionOffset)
					.trim();
		} else if (hundredthPosition > -1) {
			hundredsString = valueString.substring(0, hundredthPosition).trim();
			tensOnesString = valueString.substring(hundredthPositionOffset)
					.trim();
		} else {
			hundredsString = "";
			tensOnesString = valueString;
		}
		hundreds = parseSingleDigit(hundredsString);
		if (tensOnesString.startsWith("TWENTY")
				| tensOnesString.startsWith("TWENTIETH")) {
			tens = 2;
			ones = parseSingleDigit(tensOnesString.substring(6).trim());
		} else if (tensOnesString.startsWith("THIRTY")
				| tensOnesString.startsWith("THIRTIETH")) {
			tens = 3;
			ones = parseSingleDigit(tensOnesString.substring(6).trim());
		} else if (tensOnesString.startsWith("FORTY")
				| tensOnesString.startsWith("FORTIETH")) {
			tens = 4;
			ones = parseSingleDigit(tensOnesString.substring(5).trim());
		} else if (tensOnesString.startsWith("FOURTY")) {
			tens = 4;
			ones = parseSingleDigit(tensOnesString.substring(5).trim());
		} else if (tensOnesString.startsWith("FIFTY")
				| tensOnesString.startsWith("FIFTIETH")) {
			tens = 5;
			ones = parseSingleDigit(tensOnesString.substring(5).trim());
		} else if (tensOnesString.startsWith("SIXTY")
				| tensOnesString.startsWith("SIXTIETH")) {
			tens = 6;
			ones = parseSingleDigit(tensOnesString.substring(5).trim());
		} else if (tensOnesString.startsWith("SEVENTY")
				| tensOnesString.startsWith("SEVENTIETH")) {
			tens = 7;
			ones = parseSingleDigit(tensOnesString.substring(7).trim());
		} else if (tensOnesString.startsWith("EIGHTY")
				| tensOnesString.startsWith("EIGHTIETH")) {
			tens = 8;
			ones = parseSingleDigit(tensOnesString.substring(6).trim());
		} else if (tensOnesString.startsWith("NINETY")
				| tensOnesString.startsWith("NINETIETH")) {
			tens = 9;
			ones = parseSingleDigit(tensOnesString.substring(6).trim());
		} else if (tensOnesString.startsWith("NINTY")) {
			tens = 9;
			ones = parseSingleDigit(tensOnesString.substring(6).trim());
		} else if (tensOnesString.startsWith("TEN")
				| tensOnesString.startsWith("TENTH")) {
			tens = 1;
			ones = 0;
		} else if (tensOnesString.startsWith("ELEVEN")
				| tensOnesString.startsWith("ELEVENTH")) {
			tens = 1;
			ones = 1;
		} else if (tensOnesString.startsWith("TWELVE")
				| tensOnesString.startsWith("TWELFTH")) {
			tens = 1;
			ones = 2;
		} else if (tensOnesString.startsWith("THIRTEEN")
				| tensOnesString.startsWith("THIRTEENTH")) {
			tens = 1;
			ones = 3;
		} else if (tensOnesString.startsWith("FOURTEEN")
				| tensOnesString.startsWith("FOURTEENTH")) {
			tens = 1;
			ones = 4;
		} else if (tensOnesString.startsWith("FIFTEEN")
				| tensOnesString.startsWith("FIFTEENTH")) {
			tens = 1;
			ones = 5;
		} else if (tensOnesString.startsWith("SIXTEEN")
				| tensOnesString.startsWith("SIXTEENTH")) {
			tens = 1;
			ones = 6;
		} else if (tensOnesString.startsWith("SEVENTEEN")
				| tensOnesString.startsWith("SEVENTEENTH")) {
			tens = 1;
			ones = 7;
		} else if (tensOnesString.startsWith("EIGHTEEN")
				| tensOnesString.startsWith("EIGHTEENTH")) {
			tens = 1;
			ones = 8;
		} else if (tensOnesString.startsWith("NINETEEN")
				| tensOnesString.startsWith("NINETEENTH")) {
			tens = 1;
			ones = 9;
		} else {
			tens = 0;
			ones = parseSingleDigit(tensOnesString);
		}
		return hundreds * 100 + tens * 10 + ones;
	}

	private static int parseSingleDigit(String digitValue) {
		Integer val = UsAddressParserDataStrings.ALPHA_NUMERIC.get(digitValue);
		return val == null ? 0 : val.intValue();
	}

}
