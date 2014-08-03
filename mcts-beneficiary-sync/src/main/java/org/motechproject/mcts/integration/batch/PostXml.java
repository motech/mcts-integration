package org.motechproject.mcts.integration.batch;

import java.io.File;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Class to post the
 * job configuration xml file to batch
 *
 * @author Naveen
 *
 */
@Service
public class PostXml {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MCTSHttpClientService.class);

    private RestTemplate restTemplate;
    private HttpAgent httpAgentServiceOsgi;
    private MultiValueMap<String, Object> formData;
    private BatchServiceUrlGenerator batchServiceUrlGenerator;

    @Autowired
    public PostXml(@Qualifier("mctsRestTemplate") RestTemplate restTemplate,
            BatchServiceUrlGenerator batchServiceUrlGenerator,
            HttpAgent httpAgentServiceOsgi) {
        this.restTemplate = restTemplate;
        this.batchServiceUrlGenerator = batchServiceUrlGenerator;
        this.httpAgentServiceOsgi = httpAgentServiceOsgi;
    }

    /**
     * Method to post job configuration xml file to <code>batch</code> module
     *
     * @param file
     */
    public void sendXml(File file) {
        LOGGER.info("Started service to post xml to batch");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        formData = new LinkedMultiValueMap<String, Object>();
        formData.add("jobName", "mcts-new-job");
        formData.add("file", file);
        restTemplate.getMessageConverters().add(
                new MappingJacksonHttpMessageConverter());
        restTemplate.getMessageConverters().add(
                new StringHttpMessageConverter());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                formData, httpHeaders);
        httpAgentServiceOsgi.executeSync(
                batchServiceUrlGenerator.getUploadXmlUrl(), requestEntity,
                Method.POST);

    }

}
