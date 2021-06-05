package com.xoxo.logistic.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDto {
	
	public MilestoneDto(long id, LocalDate plannedTime, List<AddressDto> addresses) {
		super();
		this.id = id;
		this.plannedTime = plannedTime;
		this.addresses = addresses;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDate plannedTime) {
		this.plannedTime = plannedTime;
	}

	public List<AddressDto> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDto> addresses) {
		this.addresses = addresses;
	}

	private long id;
	private LocalDate plannedTime;
	
	List<AddressDto> addresses = new ArrayList<>();
	
	public MilestoneDto() {
			
		}

}
