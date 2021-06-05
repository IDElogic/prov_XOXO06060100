package com.xoxo.logistic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Address {
	
	public Address(long id, @NotEmpty @Size(min = 2, max = 2) String countryCode, @NotEmpty String city,
			@NotEmpty String streetAddress, @NotEmpty long postalCode, @NotEmpty long houseNumber, double lat,
			double lng) {
		super();
		this.id = id;
		this.countryCode = countryCode;
		this.city = city;
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.houseNumber = houseNumber;
		this.lat = lat;
		this.lng = lng;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public long getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(long houseNumber) {
		this.houseNumber = houseNumber;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public Address() {
		
	}
	
	@Id
	@GeneratedValue
	private long id;
	@NotEmpty
	@Size(min = 2, max = 2)
	private String countryCode;
	@NotEmpty
	private String city;
	@NotEmpty
	private String streetAddress;
	@NotEmpty
	private long postalCode;
	@NotEmpty
	private long houseNumber;
	private double lat;
	private double lng;
	
	
	}

