package com.xoxo.logistic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.model.Address;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.model.Transport;
import com.xoxo.logistic.repository.AddressRepository;
import com.xoxo.logistic.repository.MilestoneRepository;
import com.xoxo.logistic.repository.SectionRepository;
import com.xoxo.logistic.repository.TransportRepository;

@Service
public class InitDBService {

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	MilestoneRepository milestoneRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	TransportRepository transportRepository;
	
	
	public Transport init() {
				
	Address address1 = addressRepository.save(new Address(1L,"SE","Malmö","Södra Förstadsgatan", 21428L,124L,0.0, 0.0));
	Address address2 = addressRepository.save(new Address(2L,"SE","Jönköping","Ekhagsringen", 55456L,2L, 0.0, 0.0));
	Address address3 = addressRepository.save(new Address(3L,"SE","Borås"," Sjumilagatan", 50742L,1L, 0.0, 0.0));
	Address address4 = addressRepository.save(new Address(4L,"SE","Götebörg","Vasagatan",41137,45L, 0.0, 0.0));
	
	
	Milestone newMilestone = milestoneRepository.save(new Milestone(1L,null, LocalDateTime.of(0, 0, 0, 0, 0)));
	newMilestone.setAddress(address1);
	newMilestone.setAddress(address2);
	newMilestone.setAddress(address3);
	newMilestone.setAddress(address4);
	
	
	Transport newTransport = transportRepository.save(new Transport(0L, 0L, null));
	newTransport.addSection(null);
	newTransport.addSection(null);
	newTransport.addSection(null);
	
	return new Transport();
	}
}













