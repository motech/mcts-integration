package org.motechproject.mcts.integration.handler;

import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.service.JobTriggerService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchEventHandler {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BatchEventHandler.class);

    @Autowired
    private JobTriggerService jobTriggerService;

    @MotechListener(subjects = "BATCH_JOB_TRIGGERED")
    public void handleEvent(MotechEvent event) {
        LOGGER.debug("Handling batch_Start event in mcts");
        String jobName = event.getParameters().get("Job_Name").toString();
        LOGGER.debug("The job name is: " + jobName);
        try {
            jobTriggerService.triggerJob(jobName);
        } catch (BatchException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
