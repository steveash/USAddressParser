package com.skovalenko.geocoder.address_parser.us;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skovalenko.geocoder.address_parser.us.AddressToken.HintTypes;

public class ParserSteps {
	public static AddressToken[] findNumericWords(AddressToken[] addressTokens) {
		// only first tocken is treated as numeric, consider :
		// what about '791 Five Forks Trickum Rd,Lawrenceville,30045'
		// '8193 Thirteen Bridges Rd,Enfield,27823'
		// '75 W Eleventh St,Winnsboro Mills,29180'
		for (int x = 0; x < Math.min(1, addressTokens.length); x++) {
			if (UsAddressParserDataStrings.NUMERIC_WORDS
					.contains(addressTokens[x].getToken())) {
				addressTokens[x].setHint(HintTypes.HINT_NUMERIC_WORD);
			}
		}
		return addressTokens;
	}

	public static AddressToken[] findDirection(AddressToken[] addressTokens) {
		for (int x = 0; x < addressTokens.length; x++) {
			if (UsAddressParserDataStrings.DIRECTION
					.containsKey(addressTokens[x].getToken())) {
				// quickfix handle '4638 North','43 West' addresses
				if (x == addressTokens.length - 1
						&& addressTokens.length < 4
						&& UsAddressParserDataStrings.STREET_NAMES_EXCL
								.contains(addressTokens[x].getToken())) {
				} else {
					addressTokens[x].setHint(HintTypes.HINT_STREET_DIRECTION);
				}
			}
		}
		return addressTokens;
	}

	public static AddressToken[] findSubUnitType(AddressToken[] addressTokens) {
		for (int x = 0; x < addressTokens.length; x++) {
			if (UsAddressParserDataStrings.SU_TYPES
					.containsKey(addressTokens[x].getToken())) {
				// quickfix handle '1000 Tower','934 S Tower' addresses
				if (x == addressTokens.length - 1
						&& addressTokens.length < 4
						&& UsAddressParserDataStrings.STREET_NAMES_EXCL
								.contains(addressTokens[x].getToken())) {
				} else {
					addressTokens[x].setHint(HintTypes.HINT_SU_TYPE);
				}
			}
		}
		return addressTokens;
	}

	public static AddressToken[] findStreetType(AddressToken[] addressTokens) {
		for (int x = addressTokens.length - 1; x > 1; x--) {
			if (UsAddressParserDataStrings.STREET_TYPE
					.containsKey(addressTokens[x].getToken())) {
				addressTokens[x].setHint(HintTypes.HINT_STREET_TYPE);
				return addressTokens;
			}
		}
		// quickfix - special handling for Routes
		if (UsAddressParserDataStrings.ROUTE_ALIASES
				.contains(addressTokens[addressTokens.length - 1].getToken())
				&& addressTokens.length > 2) {
			addressTokens[addressTokens.length - 1]
					.setHint(HintTypes.HINT_STREET_TYPE);
			addressTokens[addressTokens.length - 1].setToken("RTE");
			return addressTokens;
		}
		return addressTokens;
	}

	public static AddressToken[] groupNumericWordFields(
			AddressToken[] addressTokens) {
		List<AddressToken> at = listFromArray(addressTokens);
		for (int x = 0; x < at.size(); x++) {
			if (at.get(x).getHint() == HintTypes.HINT_NUMERIC_WORD) {
				String nextField = "";
				for (int y = x + 1; y < at.size(); y++) {
					if (at.get(y).getHint() == HintTypes.HINT_NUMERIC_WORD) {
						nextField += at.get(y).getToken() + " ";
						at.remove(y);
						y--;
					} else {
						break;
					}
				}
				at.get(x).setToken(
						at.get(x).getToken() + " " + nextField.trim());
				ParsedNumber pn = NumberParser.parseAlphabeticNumber(at.get(x)
						.getToken());
				at.get(x).setToken(
						String.valueOf(pn.getNumber()) + pn.getSuffix());
				at.get(x).setHint(HintTypes.HINT_NUMERIC_WORD);
			}
		}
		AddressToken[] newAt = new AddressToken[at.size()];
		return at.toArray(newAt);
	}

	public static AddressToken[] identifyStreetName(AddressToken[] addressTokens) {
		for (int x = addressTokens.length - 1; x > 0; x--) {
			if (addressTokens[x].getHint() == HintTypes.HINT_STREET_TYPE) {
				addressTokens[x - 1].setHint(HintTypes.HINT_STREET_NAME);
				return addressTokens;
			}
		}
		return addressTokens;
	}

	public static AddressToken[] categorizeNumbers(AddressToken[] addressTokens) {
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].isPartialNumeric()) {
				if (x > 0) {
					if ((addressTokens[x - 1].getHint() == HintTypes.HINT_CITY)
							| (addressTokens[x - 1].getHint() == HintTypes.HINT_STATE)) {
						if (addressTokens[x].getToken().length() == 5)
							addressTokens[x].setHint(HintTypes.HINT_ZIP);
					} else if ((addressTokens[x].getHint() == HintTypes.HINT_STREET_NAME)) {
						addressTokens[x].setHint(HintTypes.HINT_STREET_NAME);
					} else if (addressTokens[x - 1].getHint() == HintTypes.HINT_ZIP) {
						if (addressTokens[x].getToken().length() == 4)
							addressTokens[x].setHint(HintTypes.HINT_ZIP4);
					} else if ((addressTokens[x - 1].getHint() == HintTypes.HINT_SU_TYPE)) {
						addressTokens[x].setHint(HintTypes.HINT_SU_NUMBER);
					} else if ((addressTokens[x].getHint() == HintTypes.HINT_NUMERIC_WORD)) {
						addressTokens[x].setHint(HintTypes.HINT_NONE);
					} else if (x < (addressTokens.length - 1)) {
						if ((addressTokens[x].getHint() == HintTypes.HINT_SU_TYPE)) {
							addressTokens[x + 1]
									.setHint(HintTypes.HINT_SU_NUMBER);
						} else {
							addressTokens[x]
									.setHint(HintTypes.HINT_STREET_NUMBER);
						}
					} else {
						addressTokens[x].setHint(HintTypes.HINT_STREET_NUMBER);
					}
				} else if (x == 0) {
					addressTokens[x].setHint(HintTypes.HINT_STREET_NUMBER);
				}
			}
		}
		return addressTokens;
	}

	public static AddressToken[] groupStreetNumbers(AddressToken[] addressTokens) {
		List<AddressToken> at = listFromArray(addressTokens);
		for (int x = 0; x < at.size(); x++) {
			if (at.get(x).getHint() == HintTypes.HINT_STREET_NUMBER) {
				String nextField = "";
				for (int y = x + 1; y < at.size(); y++) {
					if (at.get(y).getHint() == HintTypes.HINT_STREET_NUMBER) {
						nextField += at.get(y).getToken();
						at.remove(y);
						y--;
					} else {
						break;
					}
				}
				at.get(x).setToken(at.get(x).getToken() + nextField.trim());
			}
		}
		AddressToken[] newAt = new AddressToken[at.size()];
		return at.toArray(newAt);
	}

	public static AddressToken[] removeDuplicateStreetNumbers(
			AddressToken[] addressTokens) {
		boolean streetNumberFound = false;
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == HintTypes.HINT_STREET_NUMBER) {
				if (streetNumberFound) {
					addressTokens[x].setHint(HintTypes.HINT_NONE);
				} else {
					streetNumberFound = true;
				}
			}
		}
		return addressTokens;
	}

	public static AddressToken[] groupNoHits(AddressToken[] addressTokens) {
		int lastStreetName = ParserUtilities.findLastHintPosition(
				addressTokens, HintTypes.HINT_STREET_NAME);
		List<AddressToken> at = listFromArray(addressTokens);
		for (int x = 0; x < at.size(); x++) {
			if (at.get(x).getHint() == HintTypes.HINT_NONE) {

				String nextField = "";
				for (int y = x + 1; y < at.size(); y++) {
					if (at.get(y).getHint() == HintTypes.HINT_NONE) {
						nextField += at.get(y).getToken() + " ";
						at.remove(y);
						y--;
						lastStreetName--; // lastStreetName index changed
						// since we did a RemoveAt
					} else if ((at.get(y).getHint() == HintTypes.HINT_STREET_DIRECTION)
							&& y < lastStreetName) {
						// This is a somewhat special-case which will properly
						// handle
						// streets containing, for example, proper-names where
						// the middle initial 'happens' to be a directional
						// e.g.
						// 13101 James E Casey Ave, Englewood, CO 80112
						// without this special handling, we would parse the
						// middle initial
						// incorrectly as a directional and the result would be:
						//
						// 13101 James Ave E, Englewood, CO 80112
						//
						while (y <= lastStreetName && y < at.size()) {
							nextField += at.get(y).getToken() + " ";
							at.remove(y);
							y--;
							// lastStreetName index changed since we did a
							// remove
							lastStreetName--;
							y++;
						}
						break;
					} else {
						break;
					}
				}
				at.get(x).setToken(
						at.get(x).getToken() + " " + nextField.trim());
			}
		}
		AddressToken[] newAt = new AddressToken[at.size()];
		return at.toArray(newAt);
	}

	public static AddressToken[] categorizeNoHits(AddressToken[] addressTokens) {
		int streetTypePos = ParserUtilities.findFirstHintPosition(
				addressTokens, HintTypes.HINT_STREET_TYPE);
		if (streetTypePos == -1) {
			streetTypePos = addressTokens.length - 1;
		}
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == HintTypes.HINT_NONE) {
				if (x <= streetTypePos) {
					addressTokens[x].setHint(HintTypes.HINT_STREET_NAME);
				} else {
					addressTokens[x].setHint(HintTypes.HINT_SU_NUMBER);
				}
			}
		}
		return addressTokens;
	}

	public static AddressToken[] identifyDirection(AddressToken[] addressTokens) {
		boolean foundStreetNameOrType = false;
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == HintTypes.HINT_STREET_DIRECTION) {
				if (foundStreetNameOrType)
					addressTokens[x].setHint(HintTypes.HINT_STREET_POSTDIR);
				else
					addressTokens[x].setHint(HintTypes.HINT_STREET_PREDIR);
			} else if (addressTokens[x].getHint() == HintTypes.HINT_STREET_NAME) {
				foundStreetNameOrType = true;
			} else if (addressTokens[x].getHint() == HintTypes.HINT_STREET_TYPE) {
				foundStreetNameOrType = true;
			}
		}
		return addressTokens;
	}

	public static AddressToken[] trimTokens(AddressToken[] addressTokens) {
		for (int x = 0; x < addressTokens.length; x++) {
			addressTokens[x].setToken(addressTokens[x].getToken().trim());
		}
		return addressTokens;
	}

	public static AddressToken[] fixBadMatches(AddressToken[] addressTokens) {
		if (ParserUtilities.getValueFromTokens(addressTokens,
				HintTypes.HINT_STREET_NAME) == "") {
			for (int x = 0; x < addressTokens.length; x++) {
				if (addressTokens[x].getHint() == HintTypes.HINT_STREET_TYPE) {
					addressTokens[x].setHint(HintTypes.HINT_NONE);
				}

				if (addressTokens[x].getHint() == HintTypes.HINT_STREET_POSTDIR) {
					addressTokens[x].setHint(HintTypes.HINT_NONE);
				}

				if (addressTokens[x].getHint() == HintTypes.HINT_SU_NUMBER) {
					addressTokens[x].setHint(HintTypes.HINT_NONE);
				}
			}
		}
		return addressTokens;
	}

	private static List<AddressToken> listFromArray(
			AddressToken addressToken[]) {
		List<AddressToken> at = new ArrayList<>(addressToken.length);
		at.addAll(Arrays.asList(addressToken));
		return at;
	}

	/**
	 * If there are many tokens with hint = HINT_STREET_NAME, the merge them
	 * into one token of hint = HINT_STREET_NAME with token = concatenation of
	 * source strings separated by space.
	 *
	 * @param addressTokens
	 *            source AddressToken array
	 * @return merged AddressToken array
	 * @see com.skovalenko.geocoder.address_parser.us.AddressToken
	 */
	public static AddressToken[] mergeStreetNames(AddressToken[] addressTokens) {
		String streetName = "";
		int numberOfNameTokens = 0;
		for (int x = 0; x < addressTokens.length; x++) {
			if (addressTokens[x].getHint() == HintTypes.HINT_STREET_NAME) {
				numberOfNameTokens++;
				streetName += addressTokens[x].getToken() + " ";
			}
		}
		if (numberOfNameTokens > 1) {
			// remove space at the end
			streetName = streetName.trim();
			AddressToken[] newTokens = new AddressToken[addressTokens.length
					+ 1 - numberOfNameTokens];
			int newTokensIdx = 0;
			boolean first = true;
			for (int x = 0; x < addressTokens.length; x++) {
				if (addressTokens[x].getHint() == HintTypes.HINT_STREET_NAME) {
					if (first) {
						first = false;
						newTokens[newTokensIdx] = new AddressToken(streetName);
						newTokens[newTokensIdx]
								.setHint(HintTypes.HINT_STREET_NAME);
					} else {
						newTokensIdx--;// ignore token
					}
				} else {
					newTokens[newTokensIdx] = addressTokens[x];
				}
				newTokensIdx++;
			}
			return newTokens;
		}
		return addressTokens;
	}
}
