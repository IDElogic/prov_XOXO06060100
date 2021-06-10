package com.xoxo.logistic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xoxo.logistic.dto.DelayDto;
import com.xoxo.logistic.service.MilestoneService;
import com.xoxo.logistic.service.SectionService;
import com.xoxo.logistic.service.TransportService;

@RestController
@RequestMapping("/api/transports")
public class TransportController {
	
	@Autowired
	TransportService transportService;
	
	@Autowired
	SectionService sectionService;
	
	@Autowired
	MilestoneService milestoneService;
	
	
	@PostMapping("/{id}/delay")
	public void addDelayToTransport(@PathVariable Long id, @RequestBody DelayDto delay) {
		if (transportService.findById(id).isEmpty() || milestoneService
			.findById(delay.getMilestoneId()).isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		if (sectionService.findByTransportAndMilestone(id, delay.getMilestoneId()).isEmpty())
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		transportService.registerDelay(id,delay.getMilestoneId(),delay.getDelayInMinutes());
	}
}




















