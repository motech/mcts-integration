package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MCTSHttpClientServiceTest {

	@Mock
	private PropertyReader propertyReader;
	
	@Mock
	private RestTemplate restTemplate;

	//@InjectMocks
	//private MCTSHttpClientService mctsHttpClientService = new MCTSHttpClientService(restTemplate, propertyReader);

	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
	}
	
	@Ignore
	@Test
	public void shouldSyncBeneficiariesToMCTS() {
		String requestUrl = "requestUrl";
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		when(propertyReader.getUpdateRequestUrl()).thenReturn(
				requestUrl);
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(restTemplate.postForEntity((String)anyObject(), (HttpEntity)anyObject(), (Class)anyObject())).thenReturn(response);
		//mctsHttpClientService.syncTo(beneficiaryRequest);
		verify(restTemplate).postForEntity((String)anyObject(), (HttpEntity)anyObject(), (Class)anyObject());
	}

	@Test
	public void shouldSyncBeneficiariesFromMCTS() {
		String requestUrl = "requestUrl";
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity expectedRequestEntity = new HttpEntity(requestBody,
				httpHeaders);
		when(propertyReader.getBeneficiaryListRequestUrl())
				.thenReturn(requestUrl);
		NewDataSet expectedResponse = response();
		when(
				restTemplate.exchange((String)anyObject(), (HttpMethod)anyObject(),
						(HttpEntity)anyObject(), (Class)anyObject())).thenReturn(
				new ResponseEntity<>(expectedResponse, HttpStatus.OK));

		//NewDataSet actualResponse = mctsHttpClientService.syncFrom(requestBody);

		//assertEquals(response(), actualResponse);
	}
	
	private NewDataSet response() {
    	NewDataSet newDataSet = new NewDataSet();
    	List<Record> records = new ArrayList<Record>();
    	Record record = new Record();
    	record.setStateID("31");
    	record.setStateName("Lakshadweep");
    	record.setDistrictID("1");
    	record.setDistrictName("Lakshadweep");
    	records.add(record);
    	newDataSet.setRecords(records);
        return newDataSet;
	}
}
