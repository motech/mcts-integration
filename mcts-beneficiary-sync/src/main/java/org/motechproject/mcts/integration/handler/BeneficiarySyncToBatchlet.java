package org.motechproject.mcts.integration.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.batch.api.Batchlet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeneficiarySyncToBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncToBatchlet.class);

    private HttpClient commonsHttpClient;
  
    private PropertyReader propertyReader;

    public HttpClient getCommonsHttpClient() {
        return commonsHttpClient;
    }

    public void setCommonsHttpClient(HttpClient commonsHttpClient) {
        this.commonsHttpClient = commonsHttpClient;
    }

    public PropertyReader getPropertyReader() {
        return propertyReader;
    }

    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    public String process() throws Exception {
        String response = getRequest(propertyReader.getMctsSyncToLoginUrl());
        LOGGER.info("Returned response from mcts 0.21.1",response);
        return response;

    }

    @Override
    public void stop() throws Exception {

    }

    String getRequest(String requestUrl) {
        HttpMethod getMethod = buildRequest(requestUrl);

        try {
            commonsHttpClient.executeMethod(getMethod);
            InputStream responseBodyAsStream = getMethod
                    .getResponseBodyAsStream();
            return IOUtils.toString(responseBodyAsStream);
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request to mcts 0.21.1: "
                    + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }

        return null;
    }
    
    private HttpMethod buildRequest(String url) {
        HttpMethod requestMethod = new GetMethod(url);
        authenticateMctsLogin();
        return requestMethod;
    }
    
    private void authenticateMctsLogin() {
        commonsHttpClient.getParams().setAuthenticationPreemptive(true);
        String userName = propertyReader.getMctsUserName();
        String password = propertyReader.getMctsPassword();
        LOGGER.debug("loginurl for mcts" + propertyReader.getMctsSyncToLoginUrl());
        commonsHttpClient.getState().setCredentials(
                new AuthScope(null, -1, null, null),
                new UsernamePasswordCredentials(userName, password));
    }

}
