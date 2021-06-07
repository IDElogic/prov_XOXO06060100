package com.xoxo.logistic.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xoxo.logistic.dto.DelayDto;
import com.xoxo.logistic.dto.SectionDto;
import com.xoxo.logistic.dto.TransportDto;
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
	
	private Map<Long, TransportDto> transports = new HashMap<>();
	
	{
		transports.put(1L, new TransportDto(1,100.0, null));
		transports.put(2L, new TransportDto(2,200.0, null));
	}

	@PostMapping("/{id}/delay")
	public void addDelayToTransport(@PathVariable long id, @RequestBody DelayDto delay) {
		MilestoneService milestoneService = new MilestoneService();
		if (transportService.findById(id).isEmpty()|| 
			  milestoneService.findById(delay.getMilestoneId()).isEmpty())
			    throw new ResponseStatusException(HttpStatus.NOT_FOUND);	
		if (sectionService.findByMilestone(id, delay.getMilestoneId()).isEmpty())
			    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping("/{id}/sections")
	public TransportDto replaceSections(@PathVariable long id, @RequestBody List<SectionDto> sections) {
		TransportDto transportDto =  findByIdOrThrow(id);
		transportDto.setSections(sections);
			return transportDto;		
	}
		
	public TransportDto FindByIdOrThrow(long id) {
		TransportDto transportDto = transports.get(id);
		if(transportDto == null)			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return transportDto;
	}
	
	@DeleteMapping("/{id}")
	public void deleteTransport(@PathVariable long id) {
		transportService.delete(id);
	}

	private TransportDto findByIdOrThrow(long id) {		
			return null;
	}
	
	@DeleteMapping("/{id}/sections/{sectionId}")
	public TransportDto addNewSection(@PathVariable long id,@PathVariable long sectionId) {
		TransportDto transportDto =  findByIdOrThrow(id);
		for(Iterator<SectionDto> iterator = transportDto.getSections().iterator();
				iterator.hasNext();){
			SectionDto section = iterator.next();
			if(section.getId()== sectionId) {
				iterator.remove();
				break;
				}			
		}
		return transportDto;
	}	
}




















