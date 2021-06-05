package com.xoxo.logistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xoxo.logistic.model.Transport;

public interface TransportRepository extends JpaRepository<Transport, Long> {
	
	
}

