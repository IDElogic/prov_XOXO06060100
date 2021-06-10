package com.xoxo.logistic.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transport {

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	private long id;
	private long expectedPrice;
	
	@OneToMany(mappedBy = "transport"/*, cascade = {CascadeType.MERGE, CascadeType.PERSIST}*/)
	private List<Section> sections = new ArrayList<>();

	public Transport() {
		
	}	
	
	public Transport(long id, long expectedPrice, List<Section> sections) {
		super();
		this.id = id;
		this.expectedPrice = expectedPrice;
		this.sections = sections;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public Object setFromMilestone() {
		
		return null;
	}

	public Object setToMilestone() {
		
		return null;
	}

	public long getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(long expectedPrice) {
		this.expectedPrice = expectedPrice;
	}



}

















