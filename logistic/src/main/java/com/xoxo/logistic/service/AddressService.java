package com.xoxo.logistic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.xoxo.logistic.dto.AddressByExampleDto;
import com.xoxo.logistic.model.Address;
import com.xoxo.logistic.repository.AddressRepository;

import javassist.tools.web.BadHttpRequest;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired 
	MilestoneService milestoneService;
	
private List<Address> allAddresss = new ArrayList<>();
	
	{
		allAddresss.add(new Address(1L,"SE","Malmö","Södra Förstadsgatan", 21428L,124L, 0.0, 0.0));
		allAddresss.add(new Address(2L,"SE","Jönköping","Ekhagsringen", 55456L,2L, 0.0, 0.0));
		allAddresss.add(new Address(3L,"SE","Borås"," Sjumilagatan", 50742L,1L, 0.0, 0.0));
		allAddresss.add(new Address(4L,"SE","Götebörg","Vasagatan",41137,45L, 0.0, 0.0));
	}

	
	public List<Address> getAllAddresses() {
		return addressRepository.findAll();
	}
	
	@Transactional
	public Address createAddress(String countryCode,String city,String streetAddress,long postalCode) {
		Address address = new Address();
		address.setCountryCode(countryCode);
		address.setCity(city);
		address.setStreetAddress(streetAddress);
		address.setPostalCode(postalCode);	
		return addressRepository.save(address);
	}
	
	public Page<Address> findAddressesByExample(AddressByExampleDto example, Pageable pageable) {
		String countryCode = example.getCountryCode();
		String city = example.getCity();
		String streetAddress = example.getStreetAddress();
		long postalCode = example.getPostalCode();
		
		Specification<Address> spec = Specification.where(null);
		if(StringUtils.hasText(countryCode)) {
			spec= spec.and(AddressSpecificationService.hasCountryCode(countryCode));
		}
		if(StringUtils.hasText(city)) {
			spec= spec.and(AddressSpecificationService.hasCity(city));
		}
		if(StringUtils.hasText(streetAddress)) {
			spec= spec.and(AddressSpecificationService.hasStreetAddress(streetAddress));
		}
		if(postalCode > 0) {
			spec= spec.and(AddressSpecificationService.hasPostalCode(postalCode));			
		}
		
		return addressRepository.findAll(pageable) ;
	}


	public Optional<Address> findById(long id) {
		return addressRepository.findById(id);
	}

	@Transactional
	public void deleteAddress(long id) throws BadHttpRequest {
		if (addressRepository.findById(id).isPresent()) {
			if (!milestoneService.findByAddressId(id).isEmpty())
				throw new BadHttpRequest();
			addressRepository.deleteById(id);
		}
	}
	
	@Transactional
	public void deleteAll() {
		addressRepository.deleteAll();
	}

	@Transactional
	public Address updateAddress(Address address) {
		if (!addressRepository.existsById(address.getId()))
			throw new EntityNotFoundException();
		return addressRepository.save(address);
	}


	public Address update(Address dtoToAddress) {
		return null;
	}

	public Address addNewAddress(Address dtoToAddress) {
		
		return null;
	}

	public Address ModifyAddress(Address dtoToAddress) {
		
		return null;
	}

	public Address save(Address dtoToAddress) {
		
		return null;
	}	
}

