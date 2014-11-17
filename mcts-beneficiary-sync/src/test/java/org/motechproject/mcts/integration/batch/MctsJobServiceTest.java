package org.motechproject.mcts.integration.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.event.MotechEvent;
import org.motechproject.mcts.utils.MCTSBatchConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

@RunWith(MockitoJUnitRunner.class)
public class MctsJobServiceTest {

    @Mock
    private PropertyReader propertyReader;
    @Mock
    private MotechSchedulerService schedulerService;
    @InjectMocks
    MctsJobService service = new MctsJobService();

    String cronExpression;

    @Before
    public void setUp() {
        cronExpression = "0 15 10 * * ? 2014";
    }

    @Test
    public void scheduleFixtureJob_success() {

        when(propertyReader.getCronExpression()).thenReturn(cronExpression);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                CronSchedulableJob cronSchedulableJob = (CronSchedulableJob) args[0];
                assertNotNull(cronSchedulableJob);
                String cron = cronSchedulableJob.getCronExpression();
                assertNotNull(cron);
                assertEquals(cronExpression, cron);
                MotechEvent event = cronSchedulableJob.getMotechEvent();
                assertNotNull(event);
                assertNotNull(event.getSubject());
                assertEquals(event.getSubject(),
                        MCTSBatchConstants.EVENT_SUBJECT);
                assertNotNull(event.getParameters());
                assertNotNull(event.getParameters().get(
                        MotechSchedulerService.JOB_ID_KEY));
                assertEquals("FIXTURE", event.getParameters().get(
                        MotechSchedulerService.JOB_ID_KEY).toString());
                return null;
            }
        }).when(schedulerService).scheduleJob((CronSchedulableJob) any());
        service.scheduleFixtureJob();
        verify(propertyReader).getCronExpression();
        verify(schedulerService).scheduleJob((CronSchedulableJob) any());
    }

}
