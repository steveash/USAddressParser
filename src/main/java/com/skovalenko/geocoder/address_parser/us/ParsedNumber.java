package com.skovalenko.geocoder.address_parser.us;

public class ParsedNumber {
	public ParsedNumber() {
	}

	private long number;

	private String suffix;

	private String numberString;

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getNumberString() {
		return numberString;
	}

	public void setNumberString(String numberString) {
		this.numberString = numberString;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
