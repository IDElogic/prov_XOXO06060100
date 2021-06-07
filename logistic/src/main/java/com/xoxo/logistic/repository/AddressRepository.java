package com.xoxo.logistic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xoxo.logistic.dto.AddressByExampleDto;
import com.xoxo.logistic.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

	Page<Address> findAddressesByExample(AddressByExampleDto addressByExampleDto, Pageable pageable);
				
}


