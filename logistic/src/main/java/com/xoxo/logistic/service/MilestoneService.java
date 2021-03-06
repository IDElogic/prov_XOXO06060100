package com.xoxo.logistic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.repository.MilestoneRepository;

@Service
public class MilestoneService {

	@Autowired
	MilestoneRepository milestoneRepository;
	
	public List<Milestone> getAllMilestones() {
		return milestoneRepository.findAll();
	}
	
	public List<Milestone> findByAddressId(long id) {
		return milestoneRepository.findByAddressId(id);
	}
	
	@Transactional
	public Milestone addNewMilestone(Milestone milestone) {
		return milestoneRepository.save(milestone);
	}
	
	public Optional<Milestone> findById(long id) {
		return milestoneRepository.findById(id);
	}
	
	@Transactional
	public void deleteAll() {
		getAllMilestones().stream().forEach(m -> m.setAddress(null));
		milestoneRepository.deleteAll();
	}
}
