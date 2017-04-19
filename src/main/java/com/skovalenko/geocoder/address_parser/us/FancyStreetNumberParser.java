package com.skovalenko.geocoder.address_parser.us;

public class FancyStreetNumberParser {
	// A House Number with double number, like '22 100' or '3345 - 3350'
	private final static String STREET_NUMBER_DBL = "^\\s*\\d+\\s*(\\s|-)\\s*\\d+\\s+";

	// A House Number with fractional number, like '252 1/4 A'
	private final static String STREET_NUMBER_FRC = "^\\s*\\d+\\s+\\d[/]\\d\\s*[aAbBcCdD]?\\s+";

	// A House Number with another fractional number, like '156/5'
	private final static String STREET_NUMBER_FRC2 = "^\\s*\\d+\\s*[/]\\s*\\d+\\s+";

	// A House Number with minus letter '10 - B', '2345 a'
	private final static String STREET_NUMBER_LETR = "^\\s*\\d+\\s*[-]?\\s*[aAbBcCdD]\\s+";

	// A House Number with 'bis', like '14bis', '22 bis'
	private final static String STREET_NUMBER_BIS = "^\\s*\\d+\\s*[Bb][Ii][Ss]\\s+";

	// A House Number with roman numbers, like '435-II'
	private final static String STREET_NUMBER_ROMA = "^\\s*\\d+\\s*[-]?\\s*[IVXivx]+\\s+";

	// An alphanumeric House Number starting from char, like
	// 'MI99','L3','L10201','N50W5200'
	private final static String STREET_NUMBER_ANUM = "^\\s*[a-zA-Z]+\\d+[a-zA-Z]?\\d*\\s+";

	// An alphanumeric House Number starting from number, like
	// '1A1121','3554E','2C200'
	private final static String STREET_NUMBER_NUMA = "^\\s*\\d+[a-zA-Z]\\d*[a-zA-Z]?\\s+";

	private final static String FANCY_STREET_NUMBER_PATTERNS[] = {
			STREET_NUMBER_DBL, STREET_NUMBER_FRC, STREET_NUMBER_FRC2,
			STREET_NUMBER_LETR, STREET_NUMBER_BIS, STREET_NUMBER_ROMA,
			STREET_NUMBER_ANUM, STREET_NUMBER_NUMA };

	public static String parse(String source) {
		String result = handleNumberSign(source);
		for (int i = 0; i < FANCY_STREET_NUMBER_PATTERNS.length; i++) {
			result = handleFancyStreetNumber(result,
					FANCY_STREET_NUMBER_PATTERNS[i]);
		}
		return result;
	}

	/**
	 * This method takes care of non-single number street numbers.EXAMPLE: 57a,
	 * 10-b, 14bis, 435-II, 35-3, 156/5, D1, 315A, 350-1, N50W5200, 2C200, 315
	 * 1/2.
	 * 
	 * @param addressLine
	 *            address line to process (correct building number leaving just
	 *            first numbers)
	 * @param pattern
	 *            regular expression pattern to match
	 * @return corrected address with numeric street number (first number is
	 *         selected)
	 */
	private static String handleFancyStreetNumber(String addressLine,
			String pattern) {
		if (addressLine.matches(pattern + ".+")) {
			String rightAddr[] = addressLine.split(pattern, 2);
			if (rightAddr.length == 0) {
				return addressLine;
			}
			String rightPart = rightAddr[rightAddr.length - 1];

			String streetNumber = findFirstNumber(addressLine.substring(0,
					addressLine.indexOf(rightPart)));
			if (streetNumber.length() > 0) {
				return streetNumber + " " + rightPart;
			}
		}
		return addressLine;
	}

	private static String findFirstNumber(String src) {
		StringBuffer sb = new StringBuffer();
		int ndx = 0;
		while (ndx < src.length()) {
			char chr = src.charAt(ndx);
			if (Character.isDigit(chr)) {
				sb.append(chr);
			} else {
				if (sb.length() > 0) {
					break;
				}
			}
			ndx++;
		}
		return sb.toString();
	}
	
	private static String handleNumberSign(String addressLine) {
		if (addressLine.length() > 6) {
			// special handling of # charater
			return addressLine.substring(0, 4).replace('#', ' ')
					+ addressLine.substring(4);
		}
		return addressLine;
	}	
}
