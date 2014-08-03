package org.motechproject.mcts.integration.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to call the fixture stub and get data
 *
 * @author aman
 *
 */
@Service
public class StubDataService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FixtureDataService.class);

    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    private HttpClient commonsHttpClient;

    /**
     * Method to call the stub controller and get the data
     *
     * @return
     */
    public Data getFixtureData() {
        String response = getRequest(propertyReader.getFixtureLoginUrl());
        LOGGER.debug("response" + response);
        Data data = (Data) XmlStringToObjectConverter.unmarshal(response,
                Data.class);
        LOGGER.info("returnvalue : "
                + data.getObjects().get(0).getFixtureType() + " returnVal");
        LOGGER.info("returnvalue : "
                + data.getObjects().get(0).getFields().getGroupId()
                        .getFieldList().get(0).getFieldValue() + " returnVal");
        return data;
    }

    private HttpMethod buildRequest(String url) {
        HttpMethod requestMethod = new GetMethod(url);
        authenticate();
        return requestMethod;
    }

    private void authenticate() {
        commonsHttpClient.getParams().setAuthenticationPreemptive(true);
        LOGGER.debug("username" + propertyReader.getFixtureUserName());
        LOGGER.debug("password" + propertyReader.getFixturePassword());
        LOGGER.debug("loginurl" + propertyReader.getFixtureLoginUrl());
        commonsHttpClient.getState().setCredentials(
                new AuthScope(null, -1, null, null),
                new UsernamePasswordCredentials(propertyReader
                        .getFixtureUserName(), propertyReader
                        .getFixturePassword()));
    }

     String getRequest(String requestUrl) {

        HttpMethod getMethod = buildRequest(requestUrl);

        try {
            commonsHttpClient.executeMethod(getMethod);
            InputStream responseBodyAsStream = getMethod
                    .getResponseBodyAsStream();
            return IOUtils.toString(responseBodyAsStream);
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request to CommCare: "
                    + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }

        return null;
    }

}
