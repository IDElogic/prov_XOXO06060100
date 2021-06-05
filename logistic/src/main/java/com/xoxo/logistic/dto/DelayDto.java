package com.xoxo.logistic.dto;

public class DelayDto {

	public DelayDto(long milestoneId, int delayInMinutes) {
		super();
		this.milestoneId = milestoneId;
		this.delayInMinutes = delayInMinutes;
	}

	public long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public int getDelayInMinutes() {
		return delayInMinutes;
	}

	public void setDelayInMinutes(int delayInMinutes) {
		this.delayInMinutes = delayInMinutes;
	}

	private long milestoneId;
	private int delayInMinutes;
	
	public DelayDto() {
	}

	
	
	 

}