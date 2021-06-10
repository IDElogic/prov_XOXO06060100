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
	TransportConfigProperties config;
	
	@Autowired 
	SectionService sectionService;
	
	@Autowired
	MilestoneService milestoneService;
	
	public List<Transport> findAll() {
		return transportRepository.findAll();
	}
	
	public Optional<Transport> findById(long id) {
		return transportRepository.findById(id);
	}
	
	@Transactional
	public Transport addNewTransport(Transport transport) {
		Transport newTransport = transportRepository.save(transport);
		newTransport.getSection().stream().forEach(s -> s.setTransport(newTransport));
			return newTransport;		
	}
	
	@Transactional
	public Transport update(Transport transport) {
		if(!transportRepository.existsById(transport.getId()))
			return null;
		return transportRepository.save(transport);
	}
	
	@Transactional
	public Transport save(Transport transport) {
		return transportRepository.save(transport);
	}
	
	@Transactional
	public void delete(long id) {
		transportRepository.deleteById(id);
	}
	
	@Transactional
	public long specifiedDelay(long transportId, long milestoneId, int delayInMinutes) {
		long  newPrice = modifyPrice(transportId, delayInMinutes);	
		assignDelayMilestones(transportId,milestoneId,delayInMinutes);		
		return newPrice;
	}

	private void assignDelayMilestones(long transportId, long milestoneId, int delayInMinutes) {
		Milestone milestoneNow = milestoneService.findById(milestoneId).get();
		milestoneNow.setPlannedTime(milestoneNow.getPlannedTime().plusMinutes(delayInMinutes));
		
		Section section = sectionService.findByTransportAndSectionNumber(transportId, milestoneId).get();
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

	private long modifyPrice(long transportId, int delayInMinutes) {
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
}

