package org.motechproject.mcts.integration.batch;


import java.util.HashMap;

import org.motechproject.event.MotechEvent;
import org.motechproject.mcts.utils.MCTSBatchConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class MctsJobService {
    
    private static final Logger logger = LoggerFactory.getLogger("mcts-job-service");

    @Autowired
    public MctsJobService(PropertyReader propertyReader, MotechSchedulerService schedulerServiceOsgi) {
        String fixtureCronExpression = propertyReader.getCronExpression();
        logger.info(String.format("Setting up cron job for fixture with cron expression %s", fixtureCronExpression));
        scheduleCronJob(schedulerServiceOsgi, fixtureCronExpression, MCTSBatchConstants.EVENT_SUBJECT, String.format("%s", "FIXTURE"));
        
        String archiveCronExpression = propertyReader.getArchiveCronExpression();
        logger.info(String.format("Setting up cron job for archive with cron expression %s", archiveCronExpression));
        scheduleCronJob(schedulerServiceOsgi, archiveCronExpression, MCTSBatchConstants.ARCHIVE_EVENT_SUBJECT, String.format("%s", "ARCHIVE"));
    }

    private void scheduleCronJob(MotechSchedulerService motechSchedulerService, String cronExpression, String eventSubject, String jobIdKey) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(MotechSchedulerService.JOB_ID_KEY, jobIdKey);
        CronSchedulableJob scheduleCronJob = new CronSchedulableJob(new MotechEvent(eventSubject, parameters), cronExpression);
        motechSchedulerService.scheduleJob(scheduleCronJob);
    }
}
