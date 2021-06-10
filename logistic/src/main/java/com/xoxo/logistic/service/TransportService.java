package com.xoxo.logistic.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.config.TransportConfigProperties;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.model.Section;
import com.xoxo.logistic.model.Transport;
import com.xoxo.logistic.repository.TransportRepository;

@Service
public class TransportService {
	
	@Autowired
	TransportRepository transportRepository;
	
	@Autowired 
	SectionService sectionService;
	
	@Autowired
	MilestoneService milestoneService;
	
	@Autowired
	TransportConfigProperties config;
	
	public List<Transport> getAllTransports() {
		return transportRepository.findAll();
	}

	public Optional<Transport> findById(long id) {
		return transportRepository.findById(id);
	}
	
	@Transactional
	public Transport addNewTransport(Transport transport) {
		Transport newTransport = transportRepository.save(transport);
		newTransport.getSections().stream().forEach(s -> s.setTransport(newTransport));
		return newTransport;
		
	}
	
	@Transactional
	public Transport updateTransport(Transport transport) {
		return transportRepository.save(transport);
	}
	
	@Transactional
	public void deleteAll() {
		sectionService.getAllSections().stream().forEach(s -> s.setTransport(null));
		getAllTransports().stream().forEach(t -> t.setSections(null));
		transportRepository.deleteAll();
	}
	
	@Transactional
	public long registerDelay(long transportId, long milestoneId, int delayInMinutes) {
		long newRevenue = adjustRevenue(transportId, delayInMinutes);	
		setDelayInAffectedMilestones(transportId, milestoneId, delayInMinutes);		
		return newRevenue;
	}

	private long adjustRevenue(long transportId, int delayInMinutes) {
		Transport transport = transportRepository.findById(transportId).get();
			long priceNow = transport.getExpectedPrice();
			long modifiedPrice = priceNow;
			
			if (delayInMinutes < 30) {
				modifiedPrice *= (100-config.getPricePercent().getLessThan30minutes()) * 0.01;
			} else if (delayInMinutes < 60) {
				modifiedPrice *= (100-config.getPricePercent().getLessThan60minutes()) * 0.01;
			} else if (delayInMinutes < 120) {
				modifiedPrice *= (100-config.getPricePercent().getLessThan120minutes()) * 0.01;
			} else {
				modifiedPrice *= (100-config.getPricePercent().getMoreThan120minutes()) * 0.01;
			}
			
			transport.setExpectedPrice(modifiedPrice);
			return modifiedPrice;
	}
	
	
	private void setDelayInAffectedMilestones(long transportId, long firstMilestoneId, int delayInMinutes) {
		Milestone currentMilestone = milestoneService.findById(firstMilestoneId).get();
		currentMilestone.setPlannedTime(currentMilestone.getPlannedTime().plusMinutes(delayInMinutes));
		
		Section section = sectionService.findByMilestoneId(firstMilestoneId).get();
		Milestone nextMilestone = null;
		
		if (section.getFromMilestone().equals(currentMilestone)) {
			nextMilestone = section.getToMilestone();
		} else {
			int nextSectionNumber = section.getSectionNumber() + 1;
			Section nextSection = sectionService.findByTransportAndSectionNumber(transportId, nextSectionNumber).orElse(null);
			if (nextSection != null) {	
				nextMilestone = nextSection.getFromMilestone();
			}
		}
		
		if (nextMilestone != null) {
			nextMilestone.setPlannedTime(nextMilestone.getPlannedTime().plusMinutes(delayInMinutes));
		}
	}		
}

	
