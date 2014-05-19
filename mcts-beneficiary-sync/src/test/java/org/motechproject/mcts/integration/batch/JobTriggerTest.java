package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class JobTriggerTest {

	@Mock RestTemplate restTemplate;
	@Mock BatchServiceUrlGenerator batchServiceUrlGenerator;
	@InjectMocks MctsJobTrigger jobTrigger = new MctsJobTrigger(restTemplate, batchServiceUrlGenerator);
	
	@Test
	public void triggerJob_success() {

		when(batchServiceUrlGenerator.getTriggerJobUrl()).thenReturn("localhost");
		when(restTemplate.getForObject(batchServiceUrlGenerator.getTriggerJobUrl(), String.class)).thenReturn(null);
		jobTrigger.triggerJob();
		verify(restTemplate).getForObject((String) any(), eq(String.class));
		verify(restTemplate, times(1)).getForObject((String) any(), eq(String.class));
	}
	
}
