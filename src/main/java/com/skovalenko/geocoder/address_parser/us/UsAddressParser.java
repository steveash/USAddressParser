package com.skovalenko.geocoder.address_parser.us;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.AddressToken.HintTypes;

public class UsAddressParser {

	public UsAddressParser() {
	}

	/*
	 * A House Number consists out of max 10 characters. Allowed characters are
	 * digits, alphabetic characters, roman numerals, a fraction, a hyphen, a
	 * space or a slash. Most house numbers consist out of a solid sequence of
	 * numbers with or without alphabetic characters. Occasionally, a space
	 * separates house number suffix from the body of the house number (e.g. 22
	 * 100). Alphabetic characters can occur in uppercase or lowercase. Leading
	 * fractions, hyphens, spaces or slashes do not occur. Only one fraction,
	 * hyphen or slash is allowed per house number. Fractions are separated by a
	 * space from the rest of the house number (e.g. 252 1/4 A). Hyphens, spaces
	 * and slashes do not occur at the end of the house number. EXAMPLE: 223,
	 * 26, 57a, 10-b, 14bis, 435-II, 35-3, 156/5, D1, 315A, 350-1, N50W5200,
	 * 2C200, 315 1/2.
	 * 
	 */
	public ParsedUsAddress parse(UnparsedAddress unparsed) {
		String _zip = ParserUtilities.parseZip(unparsed.getZip());
		String _city = ParserUtilities.parseCity(unparsed.getCity());
		String _address = FancyStreetNumberParser.parse(unparsed
				.getAddressLine());
		_address = ParserUtilities.normalizeAddressLine(_address);
		ParsedUsAddress pa = parseAddressLine(_address);
		if (_zip != null) {
			pa.setZip(_zip);
		}
		if (_city != null) {
			pa.setCity(_city);
		}
		return standardize(pa);
	}

	private static ParsedUsAddress parseAddressLine(String address) {
		ParsedUsAddress parsedAddress = new ParsedUsAddress();
		AddressToken addressTokens[] = tokenizeAddressString(address);
		addressTokens = setTokenHints(addressTokens);
		parsedAddress.setStreetNumber(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STREET_NUMBER));
		parsedAddress.setStreetPreDir(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STREET_PREDIR));
		parsedAddress.setStreetName(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STREET_NAME));
		parsedAddress.setStreetType(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STREET_TYPE));
		parsedAddress.setStreetPostDir(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STREET_POSTDIR));
		parsedAddress.setSubUnitNumber(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_SU_NUMBER));
		parsedAddress.setSubUnitName(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_SU_TYPE));
		parsedAddress.setCity(ParserUtilities.getValueFromTokens(addressTokens,
				HintTypes.HINT_CITY));
		parsedAddress.setState(ParserUtilities.getValueFromTokens(
				addressTokens, HintTypes.HINT_STATE));
		parsedAddress.setZip(ParserUtilities.getValueFromTokens(addressTokens,
				HintTypes.HINT_ZIP));
		parsedAddress.setZip4(ParserUtilities.getValueFromTokens(addressTokens,
				HintTypes.HINT_ZIP4));
		return parsedAddress;
	}

	private static AddressToken[] tokenizeAddressString(String cleanAddress) {
		String[] addressFields = cleanAddress.split(" ");
		AddressToken[] addressTokens = new AddressToken[addressFields.length];
		for (int x = 0; x < addressFields.length; x++) {
			AddressToken token = new AddressToken(addressFields[x]);
			addressTokens[x] = token;
		}
		return addressTokens;
	}

	private static AddressToken[] setTokenHints(AddressToken[] addressTokens) {
		addressTokens = ParserSteps.findNumericWords(addressTokens);
		addressTokens = ParserSteps.findDirection(addressTokens);
		addressTokens = ParserSteps.findSubUnitType(addressTokens);
		addressTokens = ParserSteps.findStreetType(addressTokens);
		addressTokens = ParserSteps.groupNumericWordFields(addressTokens);
		addressTokens = ParserSteps.identifyStreetName(addressTokens);
		addressTokens = ParserSteps.categorizeNumbers(addressTokens);
		addressTokens = ParserSteps.groupStreetNumbers(addressTokens);
		addressTokens = ParserSteps.removeDuplicateStreetNumbers(addressTokens);
		addressTokens = ParserSteps.groupNoHits(addressTokens);
		addressTokens = ParserSteps.categorizeNoHits(addressTokens);
		addressTokens = ParserSteps.identifyDirection(addressTokens);
		addressTokens = ParserSteps.trimTokens(addressTokens);
		addressTokens = ParserSteps.fixBadMatches(addressTokens);
		addressTokens = ParserSteps.mergeStreetNames(addressTokens);
		return addressTokens;
	}

	private static ParsedUsAddress standardize(ParsedUsAddress address) {
		String standardDir = UsAddressParserDataStrings.DIRECTION.get(address
				.getStreetPreDir());
		if (standardDir != null) {
			address.setStreetPreDir(standardDir);
		}
		String streetType = UsAddressParserDataStrings.STREET_TYPE.get(address
				.getStreetType());
		if (streetType != null) {
			address.setStreetType(streetType);
		}
		return address;
	}
}
