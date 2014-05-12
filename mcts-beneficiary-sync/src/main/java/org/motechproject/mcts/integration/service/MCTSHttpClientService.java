package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class MCTSHttpClientService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);

    private RestTemplate restTemplate;
    private PropertyReader beneficiarySyncSettings;
    private Publisher publisher = new Publisher();
    private final static String HUB_SYNC_TO_URL = "k";
    private final static String HUB_SYNC_FROM_URL = "k";

    @Autowired
    public MCTSHttpClientService(@Qualifier("mctsRestTemplate") RestTemplate restTemplate, PropertyReader beneficiarySyncSettings) {
        this.restTemplate = restTemplate;
        this.beneficiarySyncSettings = beneficiarySyncSettings;
    }

    public void syncTo(BeneficiaryRequest beneficiaryRequest) {
        LOGGER.info("Syncing beneficiary data to MCTS.");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(beneficiaryRequest, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(beneficiarySyncSettings.getUpdateRequestUrl(), httpEntity, String.class);
        if (response != null)
            LOGGER.info(String.format("Sync done successfully. Response [StatusCode %s] : %s", response.getStatusCode(), response.getBody()));
        
    }

    public String syncFrom(MultiValueMap<String, String> requestBody) {
        LOGGER.info("Syncing beneficiary data from MCTS.");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(beneficiarySyncSettings.getBeneficiaryListRequestUrl(), HttpMethod.POST, httpEntity, String.class);

        if (response == null)
            return null;

        String responseBody = response.getBody();
        LOGGER.info(String.format("Sync done successfully. Response [StatusCode %s] : %s", response.getStatusCode(), responseBody));
        return responseBody;
    }
}
