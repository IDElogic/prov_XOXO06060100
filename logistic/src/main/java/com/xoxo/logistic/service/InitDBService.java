package com.xoxo.logistic.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.model.Address;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.model.Section;
import com.xoxo.logistic.model.Transport;

@Service
public class InitDBService {

	@Autowired
	AddressService addressService;

	@Autowired
	MilestoneService milestoneService;

	@Autowired
	SectionService sectionService;
	
	@Autowired
	TransportService transportService;
	
	public Transport initDb() {	
		transportService.deleteAll();
		sectionService.deleteAll();
		milestoneService.deleteAll();
		addressService.deleteAll();
				
	Address address1 = addressService.save(new Address(1L,"SE","Malmö","Södra Förstadsgatan", 21428L,124L,0.0, 0.0));
	Address address2 = addressService.save(new Address(2L,"SE","Jönköping","Ekhagsringen", 55456L,2L, 0.0, 0.0));
	Address address3 = addressService.save(new Address(3L,"SE","Borås"," Sjumilagatan", 50742L,1L, 0.0, 0.0));
	Address address4 = addressService.save(new Address(4L,"SE","Götebörg","Vasagatan",41137,45L, 0.0, 0.0));
	
	
	Milestone newMilestone1 = milestoneService
			.addNewMilestone(new Milestone(1L,address1, LocalDateTime.of(0, 0, 0, 0, 0)));
	Milestone newMilestone2 = milestoneService
			.addNewMilestone(new Milestone(2L,address2, LocalDateTime.of(0, 0, 0, 0, 0)));
	Milestone newMilestone3 = milestoneService
			.addNewMilestone(new Milestone(3L,address3, LocalDateTime.of(0, 0, 0, 0, 0)));
	Milestone newMilestone4 = milestoneService
			.addNewMilestone(new Milestone(4L,address4, LocalDateTime.of(0, 0, 0, 0, 0)));
	
	List<Section> sections = new ArrayList<>();

	sections.add(sectionService.addNewSection(new Section(1, 0, newMilestone1, newMilestone2, null)));
	sections.add(sectionService.addNewSection(new Section(1, 0, newMilestone3, newMilestone4, null)));
	

	return transportService.addNewTransport(new Transport(1, 0, sections));
	}
}















