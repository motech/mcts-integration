package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.junit.Ignore;
import org.motechproject.http.agent.service.Method;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PostXmlTest {

	@Mock RestTemplate restTemplate;
	@Mock BatchServiceUrlGenerator batchServiceUrlGenerator;
	@Mock HttpAgent httpAgentServiceOsgi;
	@InjectMocks PostXml postXml = new PostXml(restTemplate, batchServiceUrlGenerator,httpAgentServiceOsgi);
	
	@Test
	public void sendXml_success() {

		when(batchServiceUrlGenerator.getUploadXmlUrl()).thenReturn("localhost");
		when(restTemplate.postForObject(batchServiceUrlGenerator.getUploadXmlUrl(), HttpEntity.class,String.class)).thenReturn(null);
		postXml.sendXml(new File("randomPath"));
		verify(httpAgentServiceOsgi).executeSync((String) any(), (HttpEntity) any(), (Method)anyObject());
		verify(httpAgentServiceOsgi, times(1)).executeSync((String) any(), (HttpEntity) any(), (Method)anyObject());
	}
	
}
