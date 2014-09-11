package org.motechproject.mcts.integration.handler;

import javax.batch.api.Batchlet;

import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.utils.MctsConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BeneficiarySyncBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncBatchlet.class);

    private PropertyReader propertyReader;
    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PropertyReader getPropertyReader() {
        return propertyReader;
    }

    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }
    @Autowired
    private MctsRepository careDataRepository;

    @Override
    public String process() {

        ResponseEntity<String> loginResponse = new ResponseEntity<>(
                HttpStatus.BAD_REQUEST);
        ResponseEntity<String> response = new ResponseEntity<>(
                HttpStatus.BAD_REQUEST);
        loginResponse = getLogin();

        if (loginResponse.getStatusCode().value()
                / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE) {
            LOGGER.debug("Matching Urls: "
                    + propertyReader.getMotechLoginRedirectUrl() + " & "
                    + loginResponse.getHeaders().getLocation().toString());
            if (propertyReader.getMotechLoginRedirectUrl().equals(
                    loginResponse.getHeaders().getLocation().toString())) {
                LOGGER.info("Successfully logged in to Motech-Platform");
                try {

                    response = restTemplate.getForObject(
                            propertyReader.getMctsSyncFromLoginUrl(),
                            ResponseEntity.class);

                    return response.toString();

                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public void stop() {

    }

    ResponseEntity<String> getLogin() {
        LOGGER.info("Trying to login to Motech Platform");
        ResponseEntity<String> response = new ResponseEntity<String>(
                HttpStatus.BAD_REQUEST);
        LOGGER.debug("Login Url is: "
                + propertyReader.getMotechPlatformLoginUrl());
        LOGGER.debug("Login Params are: "
                + propertyReader.getMotechPlatformLoginForm());
        try {

            response = restTemplate.postForEntity(
                    propertyReader.getMotechPlatformLoginUrl(),
                    propertyReader.getMotechPlatformLoginForm(), String.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.debug("Login Response [StatusCode: " + response.getStatusCode()
                + "]");
        LOGGER.debug("Login Response [RedirectUrl: "
                + response.getHeaders().getLocation() + "]");
        return response;
    }

}
