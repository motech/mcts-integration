package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.web.client.RestTemplate;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class PostXmlTest {

	@Mock RestTemplate restTemplate;
	@Mock BatchServiceUrlGenerator batchServiceUrlGenerator;
	@InjectMocks PostXml postXml = new PostXml(restTemplate, batchServiceUrlGenerator);
	
	@Test
	public void sendXml_success() {

		when(batchServiceUrlGenerator.getUploadXmlUrl()).thenReturn("localhost");
		when(restTemplate.postForObject(batchServiceUrlGenerator.getUploadXmlUrl(), HttpEntity.class,String.class)).thenReturn(null);
		postXml.sendXml(new File("randomPath"));
		verify(restTemplate).postForEntity((String) any(), (HttpEntity) any(), eq(Map.class));
		verify(restTemplate, times(1)).postForEntity((String) any(), (HttpEntity) any(), eq(Map.class));
	}
	
}
