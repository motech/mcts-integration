/**
 * Class to post Updates to MCTS and also to post Http request to Mcts to send updates
 */
package org.motechproject.mcts.integration.service;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.integration.commcare.CloseData;
import org.motechproject.mcts.integration.commcare.Data;
import org.motechproject.mcts.integration.commcare.UpdateData;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class MCTSHttpClientService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MCTSHttpClientService.class);

    @Autowired
    private PropertyReader propertyReader;
    @Autowired
    private HttpAgent httpAgentServiceOsgi;

    private String postUrl;
    
    @Autowired
    public MCTSHttpClientService(PropertyReader propertyReader,
            HttpAgent httpAgentServiceOsgi) {
        this.propertyReader = propertyReader;
        this.httpAgentServiceOsgi = httpAgentServiceOsgi;
        postUrl = propertyReader.getCommcareCasePostUrl();
        
    }

    /**
     * Method to post the updates to MCTS
     *
     * @param beneficiaryRequest
     * @return
     * @throws BeneficiaryException
     */
    public HttpStatus syncTo(BeneficiaryRequest beneficiaryRequest) {
        LOGGER.info("Syncing beneficiary data to MCTS.");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(beneficiaryRequest, httpHeaders);

        ResponseEntity<String> response = (ResponseEntity<String>) httpAgentServiceOsgi
                .executeWithReturnTypeSync(
                        propertyReader.getUpdateRequestUrl(), httpEntity,
                        Method.POST);
        if (response != null) {
            LOGGER.info(String.format(
                    "Sync done successfully. Response [StatusCode %s] : %s",
                    response.getStatusCode(), response.getBody()));
        }
        return response.getStatusCode();

    }

    public HttpStatus syncToCommcare(Data data) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
        ResponseEntity<String> response = (ResponseEntity<String>) httpAgentServiceOsgi
                .executeWithReturnTypeSync(postUrl,
                        httpEntity, Method.POST);

        if (response != null) {
            LOGGER.info(String
                    .format("Sync done successfully for Creating the Xml. Response [StatusCode %s] : %s",
                            response.getStatusCode(), response.getBody()));
        }

        return response.getStatusCode();

    }

    /**
     * Method to post http request to Mcts to send the updates
     *
     * @param requestBody
     * @return
     * @throws BeneficiaryException
     * @throws Exception
     */
    public NewDataSet syncFrom(MultiValueMap<String, String> requestBody) {
        LOGGER.info("Syncing beneficiary data from MCTS at [Url: "
                + propertyReader.getBeneficiaryListRequestUrl() + "]");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        ResponseEntity<String> response = (ResponseEntity<String>) httpAgentServiceOsgi
                .executeWithReturnTypeSync(
                        propertyReader.getBeneficiaryListRequestUrl(),
                        httpEntity, Method.POST);
        if (response == null) {
            return null;
        }

        String responseString = response.getBody();
        NewDataSet responseBody = XmlStringToObjectConverter.stringXmlToObject(
                NewDataSet.class, responseString);
        LOGGER.info(String.format(
                "Sync done successfully. Response [StatusCode %s] : %s",
                response.getStatusCode(), responseBody));
        return responseBody;
    }

    public HttpStatus syncToCommcareUpdate(UpdateData data) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
        ResponseEntity<String> response = (ResponseEntity<String>) httpAgentServiceOsgi
                .executeWithReturnTypeSync(postUrl,
                        httpEntity, Method.POST);

        if (response != null) {
            LOGGER.info(String
                    .format("Sync done successfully for Updating the Xml. Response [StatusCode %s] : %s",
                            response.getStatusCode(), response.getBody()));
        }
        return response.getStatusCode();
    }

    public HttpStatus syncToCloseCommcare(CloseData data) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_XML);
        HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
        ResponseEntity<String> response = (ResponseEntity<String>) httpAgentServiceOsgi
                .executeWithReturnTypeSync(postUrl,
                        httpEntity, Method.POST);

        if (response != null) {
            LOGGER.info(String
                    .format("Sync done successfully for Creating the Xml. Response [StatusCode %s] : %s",
                            response.getStatusCode(), response.getBody()));
        }

        return response.getStatusCode();
    }
}
