package org.motechproject.mcts.integration.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
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

    @Test
    public void ScheduleJobtest() {
        System.out.println("started test");
        schedule.scheduleJob("mcts-job", "0 15 10 * * ? 2014");
    }

    @Test
    public void triggerJobTest() {
        MctsJobTrigger schedule = new MctsJobTrigger(restTemplate,
                batchServiceUrlGenerator, httpAgentServiceOsgi);
        schedule.triggerJob();
    }

}
