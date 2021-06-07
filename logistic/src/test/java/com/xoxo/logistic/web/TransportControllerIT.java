package com.xoxo.logistic.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.xoxo.logistic.dto.DelayDto;
import com.xoxo.logistic.dto.MilestoneDto;
import com.xoxo.logistic.dto.TransportDto;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransportControllerIT {
	
	@Autowired
	WebTestClient webTestClient;
	
	private static final String BASE_URI ="/api/transports";
	
	@Test
	void testThatCreatedTransportsIsListed()throws Exception {
		List<TransportDto> transportsBefore = getAllTransports();
		TransportDto newTransport = new TransportDto(5, 100.0, null);
		createTransport(newTransport);
		
		List<TransportDto> transportsAfter = getAllTransports();
		
		assertThat(transportsAfter.subList(0, transportsBefore.size()))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(transportsBefore);
		
		assertThat(transportsAfter.get(transportsAfter.size()-1))
			.usingRecursiveComparison()
			.isEqualTo(newTransport);
	}
								
	private void createTransport(TransportDto newTransport) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newTransport)
			.exchange()
			.expectStatus().isOk();	
	}
								
	private List<TransportDto> getAllTransports() {
		List<TransportDto> responseList = webTestClient
		.get()
		.uri(BASE_URI)
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(TransportDto.class)
		.returnResult().getResponseBody();
		
		Collections.sort(responseList,(t1, t2) -> Long.compare(t1.getId(),t2.getId()));
		return responseList;	
	}
	

	@Test
	void testThatNotFoundTransportOrMilestone() throws Exception {
		List<MilestoneDto> milestones = getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		addDelayInMinutesToTransport404(milestoneId, milestoneId, 0);
		addDelayInMinutesToTransport404(0, milestoneId, 1);
		addDelayInMinutesToTransport404(0, 0, 1);
	}

	private List<MilestoneDto> getAllMilestones() {
		List<MilestoneDto> responseList = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(MilestoneDto.class)
				.returnResult().getResponseBody();
				
				Collections.sort(responseList,(m1, m2) -> Long.compare(m1.getId(),m2.getId()));
				return responseList;
	}
	
	private void addDelayInMinutesToTransport404(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isNotFound();
	}
	
	@Test
	void testThatCannotAddDelayToMilestoneThatFoundNotInTransport() throws Exception {	
		List<MilestoneDto> milestones = getAllMilestones();
		long milestoneId = milestones.get(milestones.size() - 1).getId();
		addDelayInMinutesToTransport400(milestoneId, milestoneId, 0);
	}
	
	private void addDelayInMinutesToTransport400(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void testThatFoundMilestoneAtBeginningModifiedThanModifyMilestoneAtEndOfSectionToo()throws Exception {
		List<MilestoneDto> originalMilestones = getAllMilestones();
		long milestoneId = originalMilestones.get(0).getId();
		int delayInMinutes = 18;
		addDelayInMinutesToTransport(milestoneId, milestoneId, delayInMinutes);
		List<MilestoneDto> modifiedMilestones = getAllMilestones();
		assertThat(originalMilestones.get(0).getPlannedTime())
				.isEqualTo(modifiedMilestones.get(0).getPlannedTime());
		assertThat(originalMilestones.get(1).getPlannedTime())
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime()).isEqualTo(modifiedMilestones.get(2).getPlannedTime());
	}
	
	private void addDelayInMinutesToTransport(long transportId, long milestoneId, int delayInMinutes) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
		.post()
		.uri(BASE_URI + "/" + transportId + "/delay")
		.bodyValue(delayDto)
		.exchange()
		.expectStatus()
		.isOk();
	}
	
	@Test
	void testThatFoundEndMilestoneOfSectionThanMustModifyMilestoneAtBeginningOfNextSection()
			throws Exception {
		List<MilestoneDto> originalMilestones = getAllMilestones();
		long milestoneId = originalMilestones.get(1).getId();
		int delayInMinutes = 24;
		addDelayInMinutesToTransport(milestoneId, milestoneId, delayInMinutes);
		List<MilestoneDto> modifiedMilestones = getAllMilestones();
		assertThat(originalMilestones.get(1).getPlannedTime())
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime())
				.isEqualTo(modifiedMilestones.get(2).getPlannedTime());
		assertThat(originalMilestones.get(3).getPlannedTime()).isEqualTo(modifiedMilestones.get(3).getPlannedTime());
	}	

}

	