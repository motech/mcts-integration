package org.motechproject.mcts.integration.batch;

import java.util.HashMap;

import org.motechproject.event.MotechEvent;
import org.motechproject.mcts.utils.MCTSBatchConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;

public class MctsJobService {
	
	@Autowired
	private MotechSchedulerService schedulerService;
	
	@Autowired PropertyReader propertyReader;
	
	
	public void schedule_fixture_job() {
		String cronExpression = propertyReader.getCronExpression();
		HashMap<String, Object> parameters = new HashMap<>();
	    parameters.put(MotechSchedulerService.JOB_ID_KEY, String.format("%s","FIXTURE"));
	    MotechEvent motechEvent = new MotechEvent(MCTSBatchConstants.EVENT_SUBJECT, parameters);
	    CronSchedulableJob providerSyncCronJob = new CronSchedulableJob(motechEvent, cronExpression);
        schedulerService.scheduleJob(providerSyncCronJob);	
	}
}
