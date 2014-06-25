/**
 * Class to post Updates to MCTS and also to post Http request to Mcts to send updates
 */
package org.motechproject.mcts.integration.service;

import org.springframework.http.HttpStatus;
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
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.motechproject.mcts.integration.commcare.Data;

@Service
public class MCTSHttpClientService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    public MCTSHttpClientService(@Qualifier("mctsRestTemplate") RestTemplate restTemplate, PropertyReader propertyReader) {
        this.restTemplate = restTemplate;
        this.propertyReader = propertyReader;
    }

    /**
     * Method to post the updates to MCTS
     * @param beneficiaryRequest
     * @return
     */
    public HttpStatus syncTo(BeneficiaryRequest beneficiaryRequest) {
        LOGGER.info("Syncing beneficiary data to MCTS.");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(beneficiaryRequest, httpHeaders);
      
        ResponseEntity<String> response = restTemplate.postForEntity(propertyReader.getUpdateRequestUrl(), httpEntity, String.class);
        if (response != null)
            LOGGER.info(String.format("Sync done successfully. Response [StatusCode %s] : %s", response.getStatusCode(), response.getBody()));
        return response.getStatusCode();
       
        
    }
    
    public HttpStatus syncToCommcare(Data data) {
    	HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
        
        ResponseEntity<String> response = restTemplate.postForEntity(propertyReader.getUpdateRequestUrl(), httpEntity, String.class);
        if (response != null)
            LOGGER.info(String.format("Sync done successfully. Response [StatusCode %s] : %s", response.getStatusCode(), response.getBody()));
        return response.getStatusCode();

        
    }

    /**
     * Method to post http request to Mcts to send the updates
     * @param requestBody
     * @return
     */
    public String syncFrom(MultiValueMap<String, String> requestBody) {
        LOGGER.info("Syncing beneficiary data from MCTS at [Url: " + propertyReader.getBeneficiaryListRequestUrl() +"]");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        //TODO get the object instead of XML String. Also we are not checking if the response
        //is in XML/JSON or any other format. We are deserializing to object assuming it is an XML 
        
        ResponseEntity<String> response = restTemplate.exchange(propertyReader.getBeneficiaryListRequestUrl(), HttpMethod.POST, httpEntity, String.class);

        if (response == null)
            return null;

        String responseBody = response.getBody();
        LOGGER.info(String.format("Sync done successfully. Response [StatusCode %s] : %s", response.getStatusCode(), responseBody));
        return responseBody;
    }
}
