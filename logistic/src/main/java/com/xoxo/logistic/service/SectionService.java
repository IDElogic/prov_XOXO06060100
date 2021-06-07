package com.xoxo.logistic.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoxo.logistic.dto.TransportDto;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.model.Section;
import com.xoxo.logistic.model.Transport;
import com.xoxo.logistic.repository.SectionRepository;

@Service
public class SectionService {
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	MilestoneService milestoneService;
	
	public Section save(Section section) {
		return sectionRepository.save(section);
	}
	
	@Transactional
	public Section update(Section section) {
		return sectionRepository.save(section);
	}
	
	public List<Section> findAllSection() {
		return sectionRepository.findAll();
	}

	public Optional<Section> findById(long id) {
		return sectionRepository.findById(id);
	}
	
	public Optional<Section> findByTransportAndSectionNumber(long id,long sectionNumber){
		return sectionRepository.findByTransportAndSectionNumber(id,sectionNumber );
	}
	
	public List<Section>findByTransportAndMilestone(long id, int number) {
		return sectionRepository.findByTransportAndMilestone(id, number);
	}
	
	public void delete(long id) {
		sectionRepository.deleteById(id);
	}

	public List<Section>findAll() {	
		return null;
	}
	
	public Collection<Transport> getAllSections() {
		return null;
	}
	

	@Transactional
	public Section addANewSection(Section section) {
		return sectionRepository.save(section);
	}
	
	@Transactional
	public void deleteAll() {
		getAllSections().stream().forEach(
				s -> s.setFromMilestone());
		getAllSections().stream().forEach(
				s -> s.setToMilestone());
		sectionRepository.deleteAll();
	}

	public Map<Long, TransportDto> findByMilestone(long id, long milestoneId) {
		return null;
	}

	public Optional<Milestone> findByMilestoneId(long milestoneId) {
		return null;
	}		
}
	
	
