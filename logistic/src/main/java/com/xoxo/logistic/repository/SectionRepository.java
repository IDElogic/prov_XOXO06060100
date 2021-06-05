package com.xoxo.logistic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoxo.logistic.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

	@Query("SELECT s FROM Section s WHERE s.fromMilestone.id = :milestoneId OR s.toMilestone.id = :milestoneId")
	Optional<Section> findById(long id);

	@Query("SELECT s FROM Section s WHERE s.transport.id = :transportId AND (s.fromMilestone.id = :milestoneId OR s.toMilestone.id = :milestoneId)")
	List<Section> findByTransportAndMilestone(long transportId, long milestoneId);

	@Query("SELECT s FROM Section s WHERE s.transport.id = :transportId AND s.number = :number")
	Optional<Section> findByTransportAndSectionNumber(long id, long sectionNumber);

	@EntityGraph(attributePaths = {"milestones", "milestone.section", "milestone.address","milestone.plannedTime"})
	@Query("SELECT s FROM Section s")
	public List<Section> findAllWithMilestones();
	
}

