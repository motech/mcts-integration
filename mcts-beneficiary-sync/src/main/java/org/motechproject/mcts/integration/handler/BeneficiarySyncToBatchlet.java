package org.motechproject.mcts.integration.handler;

import javax.batch.api.Batchlet;
import org.motechproject.mcts.utils.MctsConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BeneficiarySyncToBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncToBatchlet.class);

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

    public String process() throws Exception {

        ResponseEntity<String> loginResponse = new ResponseEntity<>(
                HttpStatus.BAD_REQUEST);
        ResponseEntity<String> response = new ResponseEntity<>(
                HttpStatus.BAD_REQUEST);
        loginResponse = getLogin();

        if (loginResponse.getStatusCode().value()
                / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE_3XX) {
            LOGGER.debug("Matching Urls: "
                    + propertyReader.getMotechLoginRedirectUrl() + " & "
                    + loginResponse.getHeaders().getLocation().toString());
            if (propertyReader.getMotechLoginRedirectUrl().equals(
                    loginResponse.getHeaders().getLocation().toString())) {
                LOGGER.info("Succefully logged in to Motech-Platform");
                try {

                    response = restTemplate.getForObject(
                            propertyReader.getMctsSyncToLoginUrl(),
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
    public void stop() throws Exception {

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