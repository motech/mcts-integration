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

	private final static Logger LOGGER = LoggerFactory.getLogger(BeneficiarySyncBatchlet.class);
	
	@Autowired
	JobTriggerService triggerServ;
	
	@MotechListener(subjects = "BATCH_JOB_TRIGGERED")
    public void handleEvent(MotechEvent event) {
		LOGGER.debug("Handling batch_Start event in mcts");
		String jobName = event.getParameters().get("Job_Name").toString();
		LOGGER.debug("The job name us " + jobName);
		try {
			triggerServ.triggerJob("syncFrom");
		} catch(BatchException e){
			LOGGER.error(e.getMessage(), e);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
//		ClassLoader contextClassLoader = getClass().getClassLoader();
//		
//		try {
//			BatchJobClassLoader testLoader = new BatchJobClassLoader(contextClassLoader);
//			Thread.currentThread().setContextClassLoader(testLoader);
//		String jobName = event.getParameters().get("Job_Name").toString();
//		Properties props = (Properties) event.getParameters().get("Params");
//		jsrJobOperator.start(jobName, props);
//		
//		} 
//		catch(Exception e) {
//			e.printStackTrace();
//			System.out.println(e);
//		} finally {
//			Thread.currentThread().setContextClassLoader(contextClassLoader);
//		}
//		
		
	}
}
