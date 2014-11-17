package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class JobTriggerTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    BatchServiceUrlGenerator batchServiceUrlGenerator;
    @Mock
    HttpAgent httpAgentServiceOsgi;
    @InjectMocks
    MctsJobTrigger jobTrigger = new MctsJobTrigger(restTemplate,
            batchServiceUrlGenerator, httpAgentServiceOsgi);

    @Test
    public void triggerJob_success() {

        when(batchServiceUrlGenerator.getTriggerJobUrl()).thenReturn(
                "localhost");
        when(
                restTemplate.getForObject(batchServiceUrlGenerator
                        .getTriggerJobUrl(), String.class)).thenReturn(null);
        jobTrigger.triggerJob();
        verify(httpAgentServiceOsgi).executeSync((String) any(), anyObject(),
                Matchers.any(Method.class));
        verify(httpAgentServiceOsgi, times(1)).executeSync((String) any(),
                anyObject(), Matchers.any(Method.class));
    }

}
