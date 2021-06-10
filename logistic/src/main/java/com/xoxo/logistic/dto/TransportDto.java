package com.xoxo.logistic.dto;

import java.util.ArrayList;
import java.util.List;

public class TransportDto {
		
	public TransportDto(long id, long expectedPrice, List<SectionDto> sections) {
		super();
		this.id = id;
		this.expectedPrice = expectedPrice;
		this.sections = sections;
	}

	public long getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(long expectedPrice) {
		this.expectedPrice = expectedPrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<SectionDto> getSections() {
		return sections;
	}

	public void setSections(List<SectionDto> sections) {
		this.sections = sections;
	}

	private long id;
	private long expectedPrice;
	
	List<SectionDto> sections = new ArrayList<>();
	
	public TransportDto() {
		
	}

	
}
