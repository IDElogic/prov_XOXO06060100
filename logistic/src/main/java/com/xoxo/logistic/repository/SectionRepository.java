package com.xoxo.logistic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoxo.logistic.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {


	@EntityGraph(attributePaths = {"milestones", "milestone.section", "milestone.address","milestone.plannedTime"})
	@Query("SELECT s FROM Section s")
	public List<Section> findAllWithMilestones();

	public Optional<Section> findByTransportAndSectionNumber(long id, long sectionNumber);

	
	
}

