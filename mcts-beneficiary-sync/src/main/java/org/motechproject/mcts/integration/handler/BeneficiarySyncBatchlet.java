package org.motechproject.mcts.integration.handler;

import javax.batch.api.Batchlet;

import org.motechproject.mcts.utils.MctsConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
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

    @Override
    public String process() {

        ResponseEntity<String> response;
        ResponseEntity<String> loginResponse = getLogin();

        if (loginResponse.getStatusCode().value()
                / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE_3XX) {
            LOGGER.debug("Matching Urls: "
                    + propertyReader.getMotechLoginRedirectUrl() + " & "
                    + loginResponse.getHeaders().getLocation().toString());
            if (propertyReader.getMotechLoginRedirectUrl().equals(
                    loginResponse.getHeaders().getLocation().toString())) {
                LOGGER.info("Successfully logged in to Motech-Platform");
                try {
                    response = restTemplate.getForEntity(propertyReader
                            .getMctsSyncFromLoginUrl(), String.class);

                    return response.toString();

                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    if (e instanceof HttpServerErrorException) {
                        throw new HttpServerErrorException(
                                ((HttpServerErrorException) e).getStatusCode());
                    } else {
                        throw new RuntimeException();
                    }

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
        ResponseEntity<String> response = null;
        LOGGER.debug("Login Url is: "
                + propertyReader.getMotechPlatformLoginUrl());
        LOGGER.debug("Login Params are: "
                + propertyReader.getMotechPlatformLoginForm());
        try {

            response = restTemplate.postForEntity(propertyReader
                    .getMotechPlatformLoginUrl(), propertyReader
                    .getMotechPlatformLoginForm(), String.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        if (response != null) {
            LOGGER.debug("Login Response [StatusCode: "
                    + response.getStatusCode() + "]");
            LOGGER.debug("Login Response [RedirectUrl: "
                    + response.getHeaders().getLocation() + "]");
        }
        return response;
    }

}
