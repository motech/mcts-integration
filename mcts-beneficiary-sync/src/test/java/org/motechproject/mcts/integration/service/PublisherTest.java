package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class PublisherTest {
    
   @InjectMocks Publisher publisher = new Publisher();
    
    @Mock
    private PropertyReader propertyReader;
    
    @Mock 
    private HttpAgent httpAgentServiceOsgi;
    
    @Before
    public void setUp() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("location", "asd");
        ResponseEntity<String> response = new ResponseEntity<String>("response body",headers, HttpStatus.OK);
        System.out.println(response.getHeaders().getLocation());
        when(httpAgentServiceOsgi.executeWithReturnTypeSync((String)anyObject(), (HttpEntity)anyObject(), (Method) anyObject())).thenReturn((ResponseEntity) response);
        when(propertyReader.getHubBaseUrl()).thenReturn("asd");
        when(propertyReader.getMaxNumberOfPublishRetryCount()).thenReturn(2);
        when(propertyReader.getMotechLoginRedirectUrl()).thenReturn("asd");
    }
    
    @Test
    public void shouldPublish() {
        publisher.publish("abc");
    }

}
