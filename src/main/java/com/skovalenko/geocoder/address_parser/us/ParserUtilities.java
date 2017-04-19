package com.skovalenko.geocoder.address_parser.us;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skovalenko.geocoder.address_parser.us.AddressToken.HintTypes;

public class ParserUtilities {
	protected final static Logger log = LoggerFactory.getLogger(ParserUtilities.class);

	private static final char NOISE_CHARS[] = { '~', '!', '@', '$', '%', '^',
			'&', '*', '_', '+', '=', '|', '<', '>', '?', '/', '-', '.', '(',
			')', '{', '}', '[', ']', '|', '\'', '"', ',' }; // '#'

	/**
	 * Checks if a supplied string contains only digits. A character is
	 * considered to be a digit if it is not in the range 'u2000' ch
	 * 'u2FFF' and its Unicode name contains the word "DIGIT".
	 * 
	 * @param checkString
	 *            The String to be checked
	 * @return true if the String contains only digits, false otherwise
	 */
	public static boolean isNumeric(String checkString) {
		if (checkString == null || checkString.length() == 0) {
			return false;
		}
		for (int i = 0; i < checkString.length(); i++) {
			boolean check = Character.isDigit(checkString.charAt(i));
			if (!check) {
				return false;
			}
		}
		return true;
	}

	private static String removeNoiseCharacters(String src) {
		String retVal = src;
		for (int j = 0; j < NOISE_CHARS.length; j++) {
			retVal = retVal.replace(NOISE_CHARS[j], ' ');
		}
		return retVal;
	}

	private static String collapseSpaces(String newString) {
		StringBuffer sb = new StringBuffer();
		boolean lastCharWasSpace = false;
		for (int i = 0; i < newString.length(); i++) {
			if (!lastCharWasSpace || newString.charAt(i) != ' ') {
				sb.append(newString.charAt(i));
			}
			lastCharWasSpace = (newString.charAt(i) == ' ');
		}
		return sb.toString().trim();
	}

	public static String normalizeString(String str) {
		if (str == null) {
			return null;
		}
		return collapseSpaces(removeNoiseCharacters(str.trim().toUpperCase()));
	}

	public static String parseZip(String zip) {
		if (zip == null) {
			return null;
		}
		String _zip = normalizeString(zip);
		if (_zip.length() < 5 && _zip.length() > 1) {
			_zip = "00000".substring(5 - zip.length()) + zip;
		}
		if (!isNumeric(_zip) || _zip.length() != 5) {
			return null;
		}
		return _zip;
	}

	public static String parseCity(String city) {
		return normalizeString(city);
	}

	public static String normalizeAddressLine(String address) {
		return normalizeString(address.replaceAll("#", "# "));
	}

	public static int findFirstHintPosition(AddressToken[] addressTokens,
			HintTypes hint) {
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == hint) {
				return x;
			}
		}
		return -1;
	}

	public static int findLastHintPosition(AddressToken[] addressTokens,
			HintTypes hint) {
		for (int x = addressTokens.length - 1; x > -1; x--) {
			if (addressTokens[x].getHint() == hint) {
				return x;
			}
		}
		return -1;
	}

	public static String getValueFromTokens(AddressToken[] addressTokens,
			HintTypes hint) {
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == hint)
				return addressTokens[x].getToken();
		}
		return "";
	}
}
