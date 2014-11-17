package org.motechproject.mcts.integration.client;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UpdateCommcareHQ {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UpdateCommcareHQ.class);
    private RestTemplate restTemplate;
    private MultiValueMap<String, Object> formData;
    private HttpAgent httpAgentServiceOsgi;
    private BatchServiceUrlGenerator batchServiceUrlGenerator = new BatchServiceUrlGenerator();

    @Autowired
    public UpdateCommcareHQ(RestTemplate restTemplate,
            BatchServiceUrlGenerator batchServiceUrlGenerator) {
        this.restTemplate = restTemplate;
        this.batchServiceUrlGenerator = batchServiceUrlGenerator;
    }

    public void sendUpdate() {
        LOGGER.info("Started service to upload case xml to stub");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        formData = new LinkedMultiValueMap<String, Object>();
        formData.add("xml_submission_file", new FileSystemResource(
                "C:\\Users\\Rakesh\\Desktop\\update_commcare.xml"));
        restTemplate.getMessageConverters().add(
                new MappingJacksonHttpMessageConverter());
        restTemplate.getMessageConverters().add(
                new StringHttpMessageConverter());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                formData, httpHeaders);
        httpAgentServiceOsgi.executeSync(
                batchServiceUrlGenerator.getCaseUploadUrl(), requestEntity,
                Method.POST);

    }

}
