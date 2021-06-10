package com.xoxo.logistic.web;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.xoxo.logistic.config.TransportConfigProperties;
import com.xoxo.logistic.dto.DelayDto;
import com.xoxo.logistic.dto.LoginDto;
import com.xoxo.logistic.model.Milestone;
import com.xoxo.logistic.service.AddressService;
import com.xoxo.logistic.service.InitDBService;
import com.xoxo.logistic.service.MilestoneService;
import com.xoxo.logistic.service.SectionService;
import com.xoxo.logistic.service.TransportService;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransportControllerIT {

	private static final String BASE_URI = "/api/transports";
	private static final String LOGIN_URI = "/api/login";

	private long transportId;

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	AddressService addressService;

	@Autowired
	MilestoneService milestoneService;

	@Autowired
	SectionService sectionService;

	@Autowired
	TransportService transportService;

	@Autowired
	InitDBService initDBService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TransportConfigProperties config;

	@BeforeEach
	public void prepareDB() {
		transportId = initDBService.initDb().getId();
	}
	
	@Test
	void testThatCannotAddDelayIfNoAuthority() throws Exception {
		String jwtToken = loginAddressManager();
		addDelayToTransport403(1, 1, 1, jwtToken);
	}
	
		private void addDelayToTransport403(long transportId, long milestoneId, int delayInMinutes,
				String jwtToken) {
			DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
			webTestClient
				.post()
				.uri(BASE_URI + "/" + transportId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(delayDto)
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.FORBIDDEN);
		}
	
		private String loginAddressManager() {
			return loginWithJwtOk("addressManager", "passAddress");
		}
		
	
	@Test
	void testThatCannotAddDelayWhenNotFoundMilestoneOrTransport() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		addDelayToTransport404(transportId, 0, 1, jwtToken);
		addDelayToTransport404(0, milestoneId, 1, jwtToken);
		addDelayToTransport404(0, 0, 1, jwtToken);
	}
		private void addDelayToTransport404(long transportId, long milestoneId, int delayInMinutes,String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
			webTestClient
				.post()
				.uri(BASE_URI + "/" + transportId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken))
				.bodyValue(delayDto)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
		
	
	@Test
	void testThatFoundMilestoneIsNotPartOfTransport() throws Exception {String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(milestones.size() - 1).getId();
		addDelayToTransport400(transportId, milestoneId, 1, jwtToken);
	}

	private void addDelayToTransport400(long transportId, long milestoneId, int delayInMinutes,String jwtToken) {
	DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
			.post()
			.uri(BASE_URI + "/" + transportId + "/delay")
			.headers(headers -> headers.setBearerAuth(jwtToken))
			.bodyValue(delayDto)
			.exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	void testThatExpectedPriceWhenDelayLessThan30minutes() throws Exception {String jwtToken = loginTransportUser();
			List<Milestone> milestones = milestoneService.getAllMilestones();
			long milestoneId = milestones.get(0).getId();
			long originalPrice = transportService.findById(transportId).get().getExpectedPrice();
			int delayInMinutes = 25;		
			addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
			long modifiedPrice = transportService.findById(transportId).get().getExpectedPrice();
			assertThat(modifiedPrice).isEqualTo((long) (originalPrice * (100 - config.getPricePercent().getLessThan30minutes()) * 0.01));
	}
	
	private void addDelayToTransportIsOk(long transportId, long milestoneId, int delayInMinutes,String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient
			.post()
			.uri(BASE_URI + "/" + transportId + "/delay")
			.headers(headers -> headers.setBearerAuth(jwtToken))
			.bodyValue(delayDto)
			.exchange()
			.expectStatus()
			.isOk();
	}

	@Test
	void testThatExpectedPriceWhenDelayLessThan60minutes() throws Exception {String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalPrice = transportService.findById(transportId).get().getExpectedPrice();
		int delayInMinutes = 55;		
		addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
		long modifiedPrice = transportService.findById(transportId).get().getExpectedPrice();
		assertThat(modifiedPrice).isEqualTo(
				(long) (originalPrice * (100 - config.getPricePercent().getLessThan60minutes()) * 0.01));
	}
	
	private String loginTransportUser() {
		return loginWithJwtOk("transportUser", "passTransport");
	}

	
	@Test
	void testThatExpectedPriceWhenDelayLessThan120minutes() throws Exception {String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalPrice = transportService.findById(transportId).get().getExpectedPrice();
		int delayInMinutes = 115;	
		addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
		long modifiedPrice = transportService.findById(transportId).get().getExpectedPrice();
		assertThat(modifiedPrice).isEqualTo(
				(long) (originalPrice * (100 - config.getPricePercent().getLessThan120minutes()) * 0.01));
	}
	
	
	@Test
	void testThatExpectedPriceWhenDelayMoreThan120minutes() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalPrice = transportService.findById(transportId).get().getExpectedPrice();
		int delayInMinutes = 180;
		
		addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
		long modifiedPrice = transportService.findById(transportId).get().getExpectedPrice();
		assertThat(modifiedPrice).isEqualTo(
				(long) (originalPrice * (100 - config.getPricePercent().getMoreThan120minutes()) * 0.01));
	}
					
	
	@Test
	void testThatModifyMilestoneAtBeginningOfSectionModifyAtTheEndToo()
			throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(0).getId();
		int delayInMinutes = 30;
		addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(0).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(0).getPlannedTime());
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime()).isEqualTo(modifiedMilestones.get(2).getPlannedTime());
	}

	
	@Test
	void testModifyMilestoneAtTheEndOfASectionThanModifyTheNextBeginningToo()
			throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(1).getId();
		int delayInMinutes = 30;
		addDelayToTransportIsOk(transportId, milestoneId, delayInMinutes, jwtToken);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(2).getPlannedTime());
		assertThat(originalMilestones.get(3).getPlannedTime()).isEqualTo(modifiedMilestones.get(3).getPlannedTime());
	}
	
	@Test
	void testThatWeCanLogin() throws Exception {
		loginAdminUser();
	}

		private String loginAdminUser() {
		return loginWithJwtOk("admin", "passAdmin");
		}
		
		private String loginWithJwtOk(String username, String password) {
			LoginDto loginDto = new LoginDto(username, password);
			return webTestClient
					.post()
					.uri(LOGIN_URI)
					.bodyValue(loginDto)
					.exchange()
					.expectStatus()
					.isOk()
					.expectBody(String.class)
					.returnResult()
					.getResponseBody();
		}
		
	@Test
	void testThatWeCannotLoginWithBadCredentials() throws Exception {
		loginWithJwtNotOk("baduser", "badpassword");
		loginWithJwtNotOk("admin", "badpassword");
		loginWithJwtNotOk("baduser", "passAdmin");
	}
	
		private void loginWithJwtNotOk(String username, String password) {
		LoginDto loginDto = new LoginDto(username, password);
		webTestClient
			.post()
			.uri(LOGIN_URI)
			.bodyValue(loginDto)
			.exchange()
			.expectStatus()
			.isForbidden();
		}
}
