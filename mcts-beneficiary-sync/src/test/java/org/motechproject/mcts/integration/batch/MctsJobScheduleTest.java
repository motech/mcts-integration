package org.motechproject.mcts.integration.batch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "classpath:applicationTestContext.xml" })
public class MctsJobScheduleTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private BatchServiceUrlGenerator batchServiceUrlGenerator;
    @Mock
    HttpAgent httpAgentServiceOsgi;
    @InjectMocks
    MctsJobSchedule schedule = new MctsJobSchedule(restTemplate,
            batchServiceUrlGenerator, httpAgentServiceOsgi);

    String cronExpression;
    String jobName;

    @Before
    public void setUp() {
        cronExpression = "0 15 10 * * ? 2014";
        jobName = "mcts-job";
    }

    @Test
    public void triggerJobTest() {
        MctsJobTrigger schedule = new MctsJobTrigger(restTemplate,
                batchServiceUrlGenerator, httpAgentServiceOsgi);
        schedule.triggerJob();
    }

    @Test
    public void scheduleJob_success() {

        when(batchServiceUrlGenerator.getScheduleBatchUrl()).thenReturn(
                "localhost");
        when(
                restTemplate.postForObject(batchServiceUrlGenerator
                        .getScheduleBatchUrl(), HttpEntity.class, String.class))
                .thenReturn(null);
        schedule.scheduleJob(jobName, cronExpression);
        verify(httpAgentServiceOsgi).executeWithReturnTypeSync((String) any(),
                (HttpEntity) any(), (Method) anyObject());
        verify(httpAgentServiceOsgi, times(1)).executeWithReturnTypeSync(
                (String) any(), (HttpEntity) any(), (Method) anyObject());
    }

}
