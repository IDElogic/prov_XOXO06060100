package com.xoxo.logistic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Section {
	
	public Section(long id, int sectionNumber, Milestone fromMilestone, Milestone toMilestone, Transport transport) {
		super();
		this.id = id;
		this.sectionNumber = sectionNumber;
		this.fromMilestone = fromMilestone;
		this.toMilestone = toMilestone;
		this.transport = transport;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Milestone getFromMilestone() {
		return fromMilestone;
	}

	public void setFromMilestone(Milestone fromMilestone) {
		this.fromMilestone = fromMilestone;
	}

	public Milestone getToMilestone() {
		return toMilestone;
	}

	public void setToMilestone(Milestone toMilestone) {
		this.toMilestone = toMilestone;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	@Id
	@GeneratedValue
	private long id;
	private int sectionNumber;
	@OneToOne
	Milestone fromMilestone;
	@OneToOne
	Milestone toMilestone;
	@ManyToOne
	@JoinColumn(name = "transport_id")
	private Transport transport;
	
	public Section() {			
	}	
}
