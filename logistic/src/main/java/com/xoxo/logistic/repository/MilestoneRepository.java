package com.xoxo.logistic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoxo.logistic.model.Milestone;

public interface MilestoneRepository extends JpaRepository<Milestone, Long>{
	
	
	@EntityGraph(attributePaths = {"addresses", "addresses.countryCode", "addresses.city", "addresses.postalCode"})
	@Query("SELECT m FROM Milestone m")
	public List<Milestone> findAllWithAddresses();
	
	List<Milestone> findByAddressId(long id);

	

}