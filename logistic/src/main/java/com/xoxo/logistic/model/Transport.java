package com.xoxo.logistic.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Transport {


	public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

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
	private List<Section> section = new ArrayList<>();

	public Transport() {
		
	}	
	
	public void addSection(Section section) {
		if(this.section==null)
			this.section = new ArrayList<>();
		this.section.add(section);
		section.setTransport(this);
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

	public Transport(long id, long expectedPrice, List<Section> section) {
		super();
		this.id = id;
		this.expectedPrice = expectedPrice;
		this.section = section;
	}


}

















