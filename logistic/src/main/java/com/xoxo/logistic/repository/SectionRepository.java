package com.xoxo.logistic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoxo.logistic.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

	@Query("SELECT s FROM Section s WHERE s.transport.id = :transportId AND s.sectionNumber = :sectionNumber")
	Optional<Section> findByTransportAndSectionNumber(long transportPlanId, int sectionNumber);
	
	@Query("SELECT s FROM Section s WHERE s.transport.id = :transportId AND (s.fromMilestone.id = :milestoneId OR s.toMilestone.id = :milestoneId)")
	List<Section> findByTransportAndMilestone(long transportPlanId, long milestoneId);

	@Query("SELECT s FROM Section s WHERE s.fromMilestone.id = :milestoneId OR s.toMilestone.id = :milestoneId")
	Optional<Section> findByMilestoneId(long milestoneId);
}

