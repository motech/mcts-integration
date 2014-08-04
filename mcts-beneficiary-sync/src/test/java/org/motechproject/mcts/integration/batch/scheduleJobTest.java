package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.Method;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class scheduleJobTest {
	
	String cronExpression;
	String jobName;
	@Mock RestTemplate restTemplate;
	@Mock BatchServiceUrlGenerator batchServiceUrlGenerator;
	@Mock HttpAgent httpAgentServiceOsgi;
	@InjectMocks MctsJobSchedule jobSchedule = new MctsJobSchedule(restTemplate, batchServiceUrlGenerator, httpAgentServiceOsgi);
	
	@Before
	 public void setUp() {
		cronExpression = "0 15 10 * * ? 2014";
		jobName = "mcts-job";
	}
	
	@Test
	public void scheduleJob_success() {

		when(batchServiceUrlGenerator.getScheduleBatchUrl()).thenReturn("localhost");
		when(restTemplate.postForObject(batchServiceUrlGenerator.getScheduleBatchUrl(), HttpEntity.class,String.class)).thenReturn(null);
		jobSchedule.scheduleJob(jobName, cronExpression);
		verify(httpAgentServiceOsgi).executeWithReturnTypeSync((String) any(), (HttpEntity) any(), (Method)anyObject());
		verify(httpAgentServiceOsgi, times(1)).executeWithReturnTypeSync((String) any(), (HttpEntity) any(), (Method)anyObject());
	}
	
}
