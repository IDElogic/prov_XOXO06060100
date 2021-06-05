package com.xoxo.logistic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Milestone {
	public Milestone(long id, Address address, LocalDateTime plannedTime, Section section) {
		super();
		this.id = id;
		this.address = address;
		this.plannedTime = plannedTime;
		this.section = section;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}
	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;
	private LocalDateTime plannedTime;
	
	@OneToOne
	private Section section;
	
	public Milestone(long l, Object object, LocalDateTime localDateTime) {
	}

}
