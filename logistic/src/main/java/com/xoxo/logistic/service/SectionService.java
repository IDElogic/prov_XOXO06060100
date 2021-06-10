package com.xoxo.logistic.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.model.Section;
import com.xoxo.logistic.repository.SectionRepository;

@Service
public class SectionService {
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	MilestoneService milestoneService;
	
	public List<Section> getAllSections() {
		return sectionRepository.findAll();
	}

	public Optional<Section> findById(long id) {
		return sectionRepository.findById(id);
	}
	
	public List<Section> findByTransportAndMilestone(long transportId, long milestoneId) {
		return sectionRepository.findByTransportAndMilestone(transportId, milestoneId);
	}
	
	public Optional<Section>findByTransportAndSectionNumber (long id, int sectionNumber) {
		return sectionRepository.findByTransportAndSectionNumber(id, sectionNumber);
	}
	
	public Optional<Section> findByMilestoneId(long milestoneId) {
		return sectionRepository.findByMilestoneId(milestoneId);
	}
	
	@Transactional
	public Section addNewSection(Section section) {
		Section newSection = sectionRepository.save(section);
		return newSection;
	}
	
	@Transactional
	public void deleteAll() {
		getAllSections().stream().forEach(s -> s.setFromMilestone(null));
		getAllSections().stream().forEach(s -> s.setToMilestone(null));
		sectionRepository.deleteAll();
	}

//	public Optional<Transport> findByMilestone(long id, long milestoneId) {
//		
//		return null;
//	}	
}
	
	

	
	
