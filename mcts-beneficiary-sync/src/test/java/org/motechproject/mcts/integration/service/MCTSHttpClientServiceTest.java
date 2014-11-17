package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.mcts.integration.commcare.Data;
import org.motechproject.mcts.integration.commcare.UpdateData;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class MCTSHttpClientServiceTest {

    @Mock
    private PropertyReader propertyReader;

    @Mock
    private HttpAgent httpAgentServiceOsgi;

    @InjectMocks
    private MCTSHttpClientService mctsHttpClientService = new MCTSHttpClientService(
            propertyReader, httpAgentServiceOsgi);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSyncBeneficiariesToMCTS() throws BeneficiaryException {
        String requestUrl = "requestUrl";
        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        when(propertyReader.getUpdateRequestUrl()).thenReturn(requestUrl);
        ResponseEntity<String> response = new ResponseEntity<String>(
                "response body", HttpStatus.OK);
        when(
                httpAgentServiceOsgi.executeWithReturnTypeSync(
                        (String) anyObject(), (HttpEntity) anyObject(),
                        (Method) anyObject())).thenReturn(
                (ResponseEntity) response);
        mctsHttpClientService.syncTo(beneficiaryRequest);
        verify(httpAgentServiceOsgi).executeWithReturnTypeSync(
                (String) anyObject(), (HttpEntity) anyObject(),
                (Method) anyObject());
    }

    @Test
    public void shouldSyncBeneficiariesFromMCTS() throws BeneficiaryException {
        String requestUrl = "requestUrl";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity expectedRequestEntity = new HttpEntity(requestBody,
                httpHeaders);
        when(propertyReader.getBeneficiaryListRequestUrl()).thenReturn(
                requestUrl);

        NewDataSet expectedResponse = response();
        String returned = ObjectToXMLConverter.converObjectToXml(
                expectedResponse, NewDataSet.class);
        when(
                httpAgentServiceOsgi.executeWithReturnTypeSync(
                        (String) anyObject(), (HttpEntity) anyObject(),
                        (Method) anyObject())).thenReturn(
                (ResponseEntity) new ResponseEntity<>(returned, HttpStatus.OK));

        NewDataSet actualResponse = mctsHttpClientService.syncFrom(requestBody);

        assertEquals(response(), actualResponse);
    }

    @Test
    public void shouldSyncToCommcare() {
        ResponseEntity<String> response = new ResponseEntity<String>(
                "response body", HttpStatus.OK);
        when(
                httpAgentServiceOsgi.executeWithReturnTypeSync(
                        (String) anyObject(), (HttpEntity) anyObject(),
                        (Method) anyObject())).thenReturn(
                (ResponseEntity) response);
        Data data = new Data();
        mctsHttpClientService.syncToCommcare(data);
    }

    @Test
    public void shouldSyncToCommcareUpdate() {
        ResponseEntity<String> response = new ResponseEntity<String>(
                "response body", HttpStatus.OK);
        when(
                httpAgentServiceOsgi.executeWithReturnTypeSync(
                        (String) anyObject(), (HttpEntity) anyObject(),
                        (Method) anyObject())).thenReturn(
                (ResponseEntity) response);
        UpdateData data = new UpdateData();
        mctsHttpClientService.syncToCommcareUpdate(data);
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
