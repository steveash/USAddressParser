package com.skovalenko.geocoder.address_parser.test;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.skovalenko.geocoder.address_parser.ParsedUsAddress;
import com.skovalenko.geocoder.address_parser.UnparsedAddress;
import com.skovalenko.geocoder.address_parser.us.UsAddressParser;

public class UsAddressParserTest {

	private static final ParsedUsAddress PARSED_ADDRESS[] = {
		new ParsedUsAddress("13101", null, "JAMES E CASEY", "AVE", null,
							null, null, "ENGLEWOOD", null, "80112", null),
		new ParsedUsAddress("665","W","CHESTNUT","ST",null,"APT","1","WASHINGTON",null,"15301",null),
		new ParsedUsAddress("55","E","BUFFALO CHURCH","RD",null,null,null,"WASHINGTOIN",null,"15301",null),
		new ParsedUsAddress("100",null,"ROBINSON CTR","DR",null,"#","K10","PITTSBURGH",null,"15205",null),
		new ParsedUsAddress("300",null,"CEDAR","BLVD",null,"STE","B 4","PITTSBURGH",null,"15228",null),
		new ParsedUsAddress("729","N","SHENANGO","RD",null,null,null,"BEAVER FALLS",null,"15010",null),
		new ParsedUsAddress("6748","N","ASHLAND","AVE",null,"APT","206","CHICAGO",null,"60626",null),
		new ParsedUsAddress("6253","N","LAKEWOOD","AVE",null,null,null,"CHICAGO",null,"60660",null),
		new ParsedUsAddress("3632","N","CICERO","AVE",null,null,null,"CHICAGO",null,"60641",null),
		new ParsedUsAddress("536",null,"MICHIGAN","ST",null,null,null,"EVANSTON",null,"60201",null),
		new ParsedUsAddress("4709","W","GOLF","RD",null,"STE","500A","SKOKIE",null,"60076",null),
		new ParsedUsAddress("1",null,"SALMONBERRY","ST",null,null,null,"MEADOW LAKES",null,"99654",null),
		new ParsedUsAddress("881",null,"ROUTE 83",null,null,null,null,"FRANKLIN",null,"60106",null),
		new ParsedUsAddress("1019",null,"RT 519",null,null,"BUILDING","4","EIGHTY FOUR",null,"15330",null),
		new ParsedUsAddress("1019",null,"RTE 519",null,null,"BLD","4","EIGHTY FOUR",null,"15330",null),
		new ParsedUsAddress("57",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("10",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("14",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("435",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("1",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("315",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("50",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("2",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("12",null,"COMMERCE","BLVD",null,null,null,"WASHINGTON",null,"15301",null),
		new ParsedUsAddress("865","","INDIAN SERVICE ROUTE 10","","","","","ZUNI PUEBLO","","87327",null),
		new ParsedUsAddress("5629","","ROUTE 9H AND 23","","","","","CLAVERACK","","12534",null),
		// this is not correct, street name is 'Route 5059 CR 474', but this is very rare, so acceptable
		new ParsedUsAddress("12","","ROUTE 5059","CIR","","","474","SWEETWATER","","86514",null),
		new ParsedUsAddress("506","","ROUTE OF THE OLYMPIC TORCH","","","","","WESTWOOD","","96130",null),
		// see below 95 instead of 9/5 is not perfect, but acceptable
		new ParsedUsAddress("10","","ROUTE 7 TO 95","","","","","HARRISON","","26301",null),
		new ParsedUsAddress("499","","ROUTE 369 WOODWARD TWP","","","","","WOODWARD","","17744",null),
		new ParsedUsAddress("230","","TOWER","","","","","HAINES","","99827",null),
		new ParsedUsAddress("3812","S","TOWER","","","","","NACO","","85603",null),
		new ParsedUsAddress("315","","WEST","","","","","DECATUR","","72722",null),
		new ParsedUsAddress("3360","","EAST","","","","","MONTGOMERY","","36110",null),
		new ParsedUsAddress("38","","SOLDIERS","RTE","","","","SPANISH FORT","","36527",null),
		new ParsedUsAddress("2569","","REVERES","RTE","","","","GRANITE CITY","","62040",null),
		new ParsedUsAddress("22","","PARK LINE","","","","","PARK RIDGE","","60068",null),
		new ParsedUsAddress("4151","","GARDEN","","","","","WESTERN SPRINGS","","60558",null),
		new ParsedUsAddress("534","","PARK PLAINE","","","","","PARK RIDGE","","60068",null),
		new ParsedUsAddress("88","","BOULEVARD","","","","","CORNWALL ON HUD","","12520",null),
		new ParsedUsAddress("7","","RUE DE VIN","","","","","MARLBORO","","12542",null),
		new ParsedUsAddress("128","","BRIDGE","","","","","MONTICELLO","","12701",null),
		new ParsedUsAddress("380","","BOULEVARD","","","","","KINGSTON","","12401",null),
		new ParsedUsAddress("51","","FOREST","","","","","MONROE","","10950",null),
		new ParsedUsAddress("195","","LAKE LOUISE MARIE ROA","","","","","ROCK HILL","","12775",null),
		new ParsedUsAddress("45","","AVENUE A","","","","","CORNWALL ON HUD","","12520",null),
		new ParsedUsAddress("1023","","SUMMIT WOODS","","","","","NEW WINDSOR","","12553",null),
		new ParsedUsAddress("46","","ST ANDREWS","","","","","WALDEN","","12586",null),
		new ParsedUsAddress("181","S","VILLA","","","","","ELMHURST","","60126",null),
		new ParsedUsAddress("229","","R0UTE 202","","","","","POMONA","","10970",null),
		new ParsedUsAddress("4901","W","IRVING PARK","","","","","CHICAGO","","60641",null),
		// numeric street names
		new ParsedUsAddress("549","W","135TH","ST","","","","NEW YORK","","10031",null),
		new ParsedUsAddress("791","","FIVE FORKS TRICKUM","RD","","","","LAWRENCEVILLE","","30045",null),
		new ParsedUsAddress("8193","","THIRTEEN BRIDGES","RD","","","","ENFIELD","","27823",null),
		new ParsedUsAddress("75","W","ELEVENTH","ST","","","","WINNSBORO MILLS","","29180",null),
		new ParsedUsAddress("38","S","NINE","DR","","","","SAWGRASS","","32082",null),
		new ParsedUsAddress("6562","E","1300","RD",null,null,null,"CHENOA",null,"61726",null),
		new ParsedUsAddress("553","N","13TH","ST","","","","NEWARK","","07107",null),
		// uncompleted/no street type/
		new ParsedUsAddress("321","W","LAKE","","","","","ELMHURST","","60126",null),
		new ParsedUsAddress("1567","","TWIN LAKES","","","","","ROGERS","","72756",null),
		new ParsedUsAddress("45","","LAKES","","","","","BETHLEHEM VILLAGE","","06751",null),
		new ParsedUsAddress("216","","CRESCENT","","","","","CHICOPEE","","01013",null),
		new ParsedUsAddress("1310","","CRAFTON","BLVD","","","","PITTSBURGH","","15205",null)
	};

	private static final UnparsedAddress RAW_ADDRESS[] = {
		new UnparsedAddress("13101 James E Casey Ave", "Englewood", "80112"),
		new UnparsedAddress("665 W Chestnut St Apt 1","Washington","15301"),
		new UnparsedAddress("55 East Buffalo Church Rd","Washingtoin","15301"),
		new UnparsedAddress("100 Robinson Ctr Dr #K10","Pittsburgh","15205"),
		new UnparsedAddress("300 Cedar Blvd Ste B-4","Pittsburgh","15228"),
		new UnparsedAddress("729- 731 N. Shenango Rd..","Beaver Falls","15010"),
		new UnparsedAddress("6748 N. Ashland Ave. - Apt # 206","Chicago","60626"),
		new UnparsedAddress("6253 North Lakewood Ave","Chicago","60660"),
		new UnparsedAddress("3632 -36 N Cicero Ave","Chicago","60641"),
		new UnparsedAddress("536 1/2 Michigan St","Evanston","60201"),
		new UnparsedAddress("4709 W Golf Rd Ste. 500A","Skokie","60076"),
		new UnparsedAddress("1A1121 Salmonberry st "," Meadow Lakes"," 99654"),
		new UnparsedAddress("881 Route 83","Franklin","60106"),
		new UnparsedAddress("1019 Rt 519 Building 4","Eighty Four","15330"),
		new UnparsedAddress("1019 Rte 519 Bld 4","Eighty Four","15330"),
		new UnparsedAddress("57A Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("10-B Commerce Blvd. "," Washington "," 15301 "),
		new UnparsedAddress("14Bis Commerce Blvd. "," Washington "," 15301 "),
		new UnparsedAddress("435-IV Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("D1 Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("315A Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("N50W5200 Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("2C200 Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("12 1/4 A Commerce Blvd. "," Washington "," 15301"),
		new UnparsedAddress("865 Indian Service Route 10", "Zuni Pueblo", "87327"),
		new UnparsedAddress("5629 Route 9H and 23", "Claverack", "12534"),
		new UnparsedAddress("12 Route 5059 CR 474", "Sweetwater", "86514"),
		new UnparsedAddress("506 Route of the Olympic Torch", "Westwood", "96130"),
		new UnparsedAddress("10 Route 7 To 9/5", "Harrison", "26301"),
		new UnparsedAddress("499 Route 369 Woodward Twp", "Woodward", "17744"),
		new UnparsedAddress("230 Tower", "Haines", "99827"),
		new UnparsedAddress("3812 S Tower", "Naco", "85603"),
		new UnparsedAddress("315 West", "Decatur", "72722"),
		new UnparsedAddress("3360 East", "Montgomery", "36110"),
		new UnparsedAddress("38 Soldiers Rte","Spanish Fort","36527"),
		new UnparsedAddress("2569 Reveres Route","Granite City","62040"),
		new UnparsedAddress("22 Park Line", "Park Ridge", "60068"),
		new UnparsedAddress("4151 Garden", "Western Springs", "60558"),
		new UnparsedAddress("534 Park Plaine", "Park Ridge", "60068"),
		new UnparsedAddress("88 Boulevard", "Cornwall On Hud", "12520"),
		new UnparsedAddress("7 Rue De Vin", "Marlboro", "12542"),
		new UnparsedAddress("128 Bridge", "Monticello", "12701"),
		new UnparsedAddress("380 Boulevard", "Kingston", "12401"),
		new UnparsedAddress("51 Forest", "Monroe", "10950"),
		new UnparsedAddress("195 Lake Louise Marie Roa", "Rock Hill", "12775"),
		new UnparsedAddress("45 Avenue A", "Cornwall On Hud", "12520"),
		new UnparsedAddress("1023 Summit Woods", "New Windsor", "12553"),
		new UnparsedAddress("46 St.Andrews", "Walden", "12586"),
		new UnparsedAddress("181 S Villa", "Elmhurst", "60126"),
		new UnparsedAddress("229 R0Ute 202", "Pomona", "10970"),
		new UnparsedAddress("4901 W. Irving Park", "Chicago", "60641"),
		// numeric street names
		new UnparsedAddress("549 W 135th St", "New York", "10031"),
		new UnparsedAddress("791 Five Forks Trickum Rd", "Lawrenceville", "30045"),
		new UnparsedAddress("8193 Thirteen Bridges Rd", "Enfield", "27823"),
		new UnparsedAddress("75 W Eleventh St", "Winnsboro Mills", "29180"),
		new UnparsedAddress("38 S Nine Dr", "Sawgrass", "32082"),
		new UnparsedAddress("6562 E 1300 Rd", " Chenoa", "61726"),
		new UnparsedAddress("553 N 13th St", "Newark", "07107"),
		// uncompleted/no street type/ 'lake(s)'
		new UnparsedAddress("321 W Lake", "Elmhurst", "60126"),
		new UnparsedAddress("1567 Twin Lakes", "Rogers", "72756"),
		new UnparsedAddress("45 Lakes", "Bethlehem Village", "06751"),
		new UnparsedAddress("216 Crescent", "Chicopee", "01013"),
		new UnparsedAddress("1310 Crafton Bul", "Pittsburgh", "15205")
	};

	@Test
	public void testParserWorks() {
		UsAddressParser parser = new UsAddressParser();
		for (int j = 0; j < RAW_ADDRESS.length; j++) {
			ParsedUsAddress pa = parser.parse(RAW_ADDRESS[j]);
			Verify.objectsAreEqual(PARSED_ADDRESS[j], pa);
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(UsAddressParserTest.class);
	}
}
