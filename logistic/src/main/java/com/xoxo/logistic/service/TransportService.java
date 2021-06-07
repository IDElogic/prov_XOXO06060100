package com.xoxo.logistic.service;

import java.util.List;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.config.TransportConfigProperties;
import com.xoxo.logistic.dto.TransportDto;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.model.Section;
import com.xoxo.logistic.model.Transport;
import com.xoxo.logistic.repository.TransportRepository;

@Service
public class TransportService {
	
	@Autowired
	TransportRepository transportRepository;
	
	@Autowired
	TransportConfigProperties config;
	
	@Autowired 
	SectionService sectionService;
	
	@Autowired
	MilestoneService milestoneService;
	
	public Transport save(Transport transport) {
		return transportRepository.save(transport);
	}
	
	@Transactional
	public Transport update(Transport transport) {
		if(!transportRepository.existsById(transport.getId()))
			return null;
		return transportRepository.save(transport);
	}
	
	public List<Transport> findAll() {
		return transportRepository.findAll();
	}
	
	public void delete(long id) {
		transportRepository.deleteById(id);
	}
	
	public Map<Long, TransportDto> findById(long id) {		
		return null;
	}		

	@Transactional
	public Transport addNewTransport(Transport transport) {
		Transport newTransport = transportRepository.save(transport);
		newTransport.getSection().stream().forEach(s -> s.setTransport(newTransport));
		return newTransport;		
	}
	
	@Transactional
	public double delay(long transportId, long milestoneId, int delayInMinutes) {
		double  newPrice = modifyPrice(transportId, delayInMinutes);	
		assignDelayMilestones(transportId,milestoneId,delayInMinutes);		
		return newPrice;
	}


	private void assignDelayMilestones(long transportId, long milestoneId, int delayInMinutes) {
		Milestone milestoneNow = milestoneService.findById(milestoneId).get();
		milestoneNow.setPlannedTime(milestoneNow.getPlannedTime().plusMinutes(delayInMinutes));
		
		Section section = sectionService.findByTransportAndMilestone(milestoneId, delayInMinutes).get(0);
		Milestone milestoneNext = null;
		
		if (section.getFromMilestone().equals(milestoneNow)) {
			milestoneNext = section.getToMilestone();
		} else {
			long nextSectionNumber = section.getSectionNumber() + 1;
			Section nextSection = sectionService.findByTransportAndSectionNumber(transportId, nextSectionNumber).orElse(null);
			if (nextSection != null) {	
				milestoneNext = nextSection.getFromMilestone();
			}
		}
		
		if (milestoneNext != null) {
			milestoneNext.setPlannedTime(milestoneNext.getPlannedTime().plusMinutes(delayInMinutes));
		}
	}

	private double modifyPrice(long transportId, int delayInMinutes) {
		Transport transport = transportRepository.findById(transportId).get();
		double priceNow = transport.getPrice();
		double modifiedPrice = priceNow;
		
		if (delayInMinutes < 30) {
			modifiedPrice *= (100-config.getPricePercent().getLessThan30minutes()) * 0.01;
		} else if (delayInMinutes < 60) {
			modifiedPrice *= (100-config.getPricePercent().getLessThan60minutes()) * 0.01;
		} else if (delayInMinutes < 120) {
			modifiedPrice *= (100-config.getPricePercent().getLessThan120minutes()) * 0.01;
		} else {
			modifiedPrice *= (100-config.getPricePercent().getMoreThan120minutes()) * 0.01;
		}
		
		transport.setPrice(modifiedPrice);
		return modifiedPrice;
	}

	public void deleteAll() {
		
	}	
}

