package org.motechproject.mcts.integration.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.http.HttpException;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Service to call the fixture stub and get data
 * 
 * @author aman
 * 
 */
@Service
public class StubDataService {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FixtureDataService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private HttpAgent httpAgentServiceOsgi;

	@Autowired
	private HttpClient commonsHttpClient;

	/*
	 * @Autowired private HttpAgent httpAgentServiceOsgi;
	 */

	/**
	 * Method to call the stub controller and get the data
	 * 
	 * @return
	 */
	public Data getFixtureData() {
		ResponseEntity<String> loginResponse = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);

		String response = getRequest(propertyReader.getFixtureLoginUrl());
		LOGGER.debug("response"+response);
		Data data = (Data) XmlStringToObjectConverter.unmarshal(response,
				Data.class);
		// ResponseEntity<DataList> response = (ResponseEntity<DataList>)
		// httpAgentServiceOsgi.executeWithReturnTypeSync(url, null,
		// Method.GET);
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
		LOGGER.debug("username"+propertyReader.getFixtureUserName());
		LOGGER.debug("password"+propertyReader.getFixturePassword());
		LOGGER.debug("loginurl"+propertyReader.getFixtureLoginUrl());
		commonsHttpClient.getState().setCredentials(
				new AuthScope(null, -1, null, null),
				new UsernamePasswordCredentials(propertyReader
						.getFixtureUserName(), propertyReader
						.getFixturePassword()));
	}

	private String getRequest(String requestUrl) {

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
