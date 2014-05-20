package org.motechproject.mcts.integration.batch;

import java.util.HashMap;

import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.motechproject.mcts.utils.CronJobScheduleParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Client class to schedule mcts job with batch module
 * @author Naveen
 *
 */
public class MctsJobSchedule {
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);
	 private RestTemplate restTemplate;
	 private BatchServiceUrlGenerator batchServiceUrlGenerator;
	 @Autowired
	 public MctsJobSchedule(@Qualifier("mctsRestTemplate") RestTemplate restTemplate, BatchServiceUrlGenerator batchServiceUrlGenerator) {
	        this.restTemplate = restTemplate;
	        this.batchServiceUrlGenerator = batchServiceUrlGenerator;
	    }
   
	    /**
	     * method to schedule <code>batch job</code>
	     * @param jobName Name of the <code>job</code> to be scheduled
	     * @param cronExpression <code>cron expression</code> for the job to be scheduled
	     */
		public void scheduleJob(String jobName,String cronExpression) {
	        LOGGER.info("Started service to schedule mcts job with batch");
	        HttpHeaders httpHeaders = new HttpHeaders();
	        CronJobScheduleParameters params = new CronJobScheduleParameters();
	        params.setCronExpression(cronExpression);
	        params.setJobName(jobName);
	        params.setParamsMap(new HashMap<String, String>());
			HttpEntity httpEntity = new HttpEntity(params, httpHeaders);
	        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            try {
            	restTemplate.postForObject(batchServiceUrlGenerator.getScheduleBatchUrl(), httpEntity, String.class);
            }
            catch(Exception e) {
            	LOGGER.info(e.getMessage());
            }
	       

	    }
	    
	    
	  
}
