package org.motechproject.mcts.integration.batch;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Client class to trigger scheduled job with batch
 *
 * @author Naveen
 *
 */
public class MctsJobTrigger {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MctsJobTrigger.class);
    private RestTemplate restTemplate;
    private HttpAgent httpAgentServiceOsgi;
    private BatchServiceUrlGenerator batchServiceUrlGenerator;

    @Autowired
    public MctsJobTrigger(
            /*@Qualifier("mctsRestTemplate")*/ RestTemplate restTemplate,
            BatchServiceUrlGenerator batchServiceUrlGenerator,
            HttpAgent httpAgentServiceOsgi) {
        this.restTemplate = restTemplate;
        this.batchServiceUrlGenerator = batchServiceUrlGenerator;
        this.httpAgentServiceOsgi = httpAgentServiceOsgi;
    }

    /**
     * Method to trigger <code>job</code> scheduled with <batch> module
     */
    public void triggerJob() {
        LOGGER.info("Started service to trigger mcts job with batch");
        restTemplate.getMessageConverters().add(
                new StringHttpMessageConverter());
        try {
            httpAgentServiceOsgi.executeSync(
                    batchServiceUrlGenerator.getTriggerJobUrl(), null,
                    Method.GET);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

    }

}
