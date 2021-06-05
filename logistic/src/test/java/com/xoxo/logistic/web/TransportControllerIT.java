package com.xoxo.logistic.web;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.xoxo.logistic.config.TransportConfigProperties;
import com.xoxo.logistic.dto.DelayDto;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.service.MilestoneService;
import com.xoxo.logistic.service.SectionService;
import com.xoxo.logistic.service.TransportService;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransportControllerIT {

	private static final String TRANSPORTS_BASE_URI = "/api/transports";
	
	private long transportId;

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	MilestoneService milestoneService;

	@Autowired
	SectionService sectionService;

	@Autowired
	TransportService transportService;

	@Autowired
	TransportConfigProperties config;

	@Test
	void testThatNotFoundTransportOrMilestone() 
			throws Exception {
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		addDelayInMinutesToTransport404(transportId, 0, 1);
		addDelayInMinutesToTransport404(0, milestoneId, 1);
		addDelayInMinutesToTransport404(0, 0, 1);
	}
	private void addDelayInMinutesToTransportOk(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(TRANSPORTS_BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isOk();
	}

	private void addDelayInMinutesToTransport400(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(TRANSPORTS_BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private void addDelayInMinutesToTransport404(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(TRANSPORTS_BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isNotFound();
	}

	@Test
	void testThatCannotAddDelayToMilestoneThatFoundNotInTransport() 
			throws Exception {	
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(milestones.size() - 1).getId();
		addDelayInMinutesToTransport400(transportId, milestoneId, 1);
	}


	@Test
	void testThatFoundMilestoneAtBeginningModifiedThanNeedToModifyMilestoneAtEndOfSectionToo()
			throws Exception {
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(0).getId();
		int delayInMinutes = 18;
		addDelayInMinutesToTransportOk(transportId, milestoneId, delayInMinutes);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(0).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(0).getPlannedTime());
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime()).isEqualTo(modifiedMilestones.get(2).getPlannedTime());
	}

	@Test
	void testThatFoundAnEndMilestoneOfSectionThanMustModifyMilestoneAtBeginningOfNextSection()
			throws Exception {
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(1).getId();
		int delayInMinutes = 24;
		addDelayInMinutesToTransportOk(transportId, milestoneId, delayInMinutes);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(2).getPlannedTime());
		assertThat(originalMilestones.get(3).getPlannedTime()).isEqualTo(modifiedMilestones.get(3).getPlannedTime());
	}	

}
