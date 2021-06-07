package com.xoxo.logistic.dto;

public class AddressByExampleDto {
	

	public AddressByExampleDto(String countryCode, String city, String streetAddress, long postalCode) {
		super();
		this.countryCode = countryCode;
		this.city = city;
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
	}


	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public long getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(long postalCode) {
		this.postalCode = postalCode;
	}

	private String countryCode;
	private String city;
	private String streetAddress;
	private long postalCode;

	public AddressByExampleDto() {
	}
	
}
