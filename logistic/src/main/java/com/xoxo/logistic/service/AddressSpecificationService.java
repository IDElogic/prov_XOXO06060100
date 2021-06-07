package com.xoxo.logistic.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.model.Address;
import com.xoxo.logistic.model.Address_;

@Service
public class AddressSpecificationService {
	
	public static Specification<Address> hasCountryCode(String countryCode){
		return (root, cq, cb) -> cb.equal(root.get(Address_.countryCode), countryCode);
	}

	public static Specification<Address> hasCity(String city) {
		return (root, cq, cb) -> cb.like(root.get(Address_.city), city + "%");
	}
	
	public static Specification<Address> hasStreetAddress(String streetAddress) {
		return (root, cq, cb) -> cb.like(root.get(Address_.streetAddress), streetAddress + "%");
	}
	
	public static Specification<Address> hasPostalCode(long postalCode) {
		return (root, cq, cb) -> cb.equal(root.get(Address_.postalCode), postalCode);
		
	}
}
