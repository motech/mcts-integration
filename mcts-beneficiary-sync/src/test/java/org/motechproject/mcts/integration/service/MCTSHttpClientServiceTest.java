package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.PropertyReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MCTSHttpClientServiceTest {

	@Mock
	private PropertyReader beneficiarySyncSettings;
	@Mock
	private RestTemplate restTemplate;

	private MCTSHttpClientService mctsHttpClientService;

	@Before
	public void setUp() throws Exception {
		mctsHttpClientService = new MCTSHttpClientService(restTemplate,
				beneficiarySyncSettings);
	}
	
	@Test
	public void shouldSyncBeneficiariesToMCTS() {
		String requestUrl = "requestUrl";
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		when(beneficiarySyncSettings.getUpdateRequestUrl()).thenReturn(
				requestUrl);
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(beneficiaryRequest, httpHeaders);
		mctsHttpClientService.syncTo(beneficiaryRequest);
		verify(restTemplate).postForEntity(requestUrl, httpEntity,
				String.class);
	}

	@Test
	public void shouldSyncBeneficiariesFromMCTS() {
		String requestUrl = "requestUrl";
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity expectedRequestEntity = new HttpEntity(requestBody,
				httpHeaders);
		when(beneficiarySyncSettings.getBeneficiaryListRequestUrl())
				.thenReturn(requestUrl);
		String expectedResponse = "ResponseBody";
		when(
				restTemplate.exchange(requestUrl, HttpMethod.POST,
						expectedRequestEntity, String.class)).thenReturn(
				new ResponseEntity<>(expectedResponse, HttpStatus.OK));

		String actualResponse = mctsHttpClientService.syncFrom(requestBody);

		assertEquals(expectedResponse, actualResponse);
	}
}
