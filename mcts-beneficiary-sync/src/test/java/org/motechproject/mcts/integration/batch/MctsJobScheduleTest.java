package org.motechproject.mcts.integration.batch;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationBeneficiarySyncContextTest.xml"})
public class MctsJobScheduleTest {

	@Autowired
	private RestTemplate mctsRestTemplate;
	private Properties properties;
	@Autowired
	private BatchServiceUrlGenerator batchServiceUrlGenerator;
	
	@Test
	public void ScheduleJobtest() {
		System.out.println("started test");
		MctsJobSchedule schedule = new MctsJobSchedule(mctsRestTemplate,batchServiceUrlGenerator);
		schedule.scheduleJob("mcts-job","0 15 10 * * ? 2014");
	}
	
	
	@Test
	public void triggerJobTest() {
		MctsJobTrigger schedule = new MctsJobTrigger(mctsRestTemplate,batchServiceUrlGenerator);
		schedule.triggerJob();
	}
	
}
