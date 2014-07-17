/**
 * Class to send Notificaion to Hub about the Updates
 */
package org.motechproject.mcts.integration.service;

import java.io.UnsupportedEncodingException;



import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.utils.MCTSEventConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Publisher {

	@Autowired
	private PropertyReader propertyReader;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HttpAgent httpAgentServiceOsgi;
	private final static String MODE = "publish";
	private final static Logger LOGGER = LoggerFactory
			.getLogger(Publisher.class);
	private static String URL;

	//TODO Uncomment Later
	/*@MotechListener(subjects=MCTSEventConstants.EVENT_BENEFICIARIES_ADDED)
	public void handleEvent(MotechEvent motechEvent) throws BeneficiaryException{
		String url = (String)motechEvent.getParameters().get(MCTSEventConstants.PARAM_PUBLISHER_URL);
		publish(url);
	}*/
	/**
	 * Method to <code>Publish</code> updates to hub along with
	 * <code>callBack URL</code> and to <code>retry</code> sending notifications
	 * if failed.
	 * 
	 * @param url
	 * @throws BeneficiaryException
	 */
	public void publish(String url) throws BeneficiaryException{
		setUrl(url);
		ResponseEntity<String> response = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		int maxRetryCount = propertyReader.getMaxNumberOfPublishRetryCount();
		int retryCount = -1;
		do {
			LOGGER.info(String.format("Notifying Hub for %s time at url: %s",
					retryCount + 2, URL));
			response = notifyHub();
			retryCount++;
			if (response.getStatusCode().value() / 100 == 2) {
				LOGGER.info(String
						.format("Hub Notified Successfully after %s retries. Response [StatusCode %s] : %s",
								retryCount, response.getStatusCode(),
								response.getBody()));
				return;
			}
			// try {
			// Thread.sleep(propertyReader.getHubRetryInterval());
			// } catch (InterruptedException e) {
			// LOGGER.debug(e.getMessage());
			// }
		} while (retryCount < maxRetryCount
				&& response.getStatusCode().value() / 100 != 2);

		if (response.getStatusCode().value() / 100 == 2)
			LOGGER.info(String
					.format("Hub Notified Successfully with %s retries. Response [StatusCode %s] : %s",
							retryCount, response.getStatusCode(),
							response.getBody()));
		else {
			String error = String
					.format("Notification to Hub failed. Response [StatusCode %s] : %s",
							response.getStatusCode(), response.getBody());
			LOGGER.error(error);
			throw new BeneficiaryException(ApplicationErrors.CONNECTION_ERROR, error);
		}
	}

	/**
	 * Method to set the url at which Hub is to be Notified
	 * 
	 * @param url
	 * @throws UnsupportedEncodingException
	 */
	private void setUrl(String url){
		URL = String.format("%s%s%s%s%s", propertyReader.getHubBaseUrl(),
				"?hub.mode=", MODE, "&hub.url=", url);
	}

	/**
	 * <code>Posts</code> the Http entity to Hub with <code>callBack Url</code>
	 * and <code>ContentType</code> as passed in argument
	 * 
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	private ResponseEntity<String> notifyHub() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		ResponseEntity<String> loginResponse = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		ResponseEntity<String> response = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		loginResponse = getLogin();
		LOGGER.debug("Matching Urls: " + propertyReader.getMotechLoginRedirectUrl() + " & " + loginResponse.getHeaders().getLocation().toString());
		if (propertyReader.getMotechLoginRedirectUrl().equals(
				loginResponse.getHeaders().getLocation().toString())) {
			LOGGER.info("Succefully logged in to Motech-Platform");
			LOGGER.debug("Notify hub at the url: " + getUrl());
			LOGGER.info("Sending HUb the Notification");
			try {
				/*response = restTemplate.postForEntity(getUrl(), httpEntity,
						String.class);*/
				response = (ResponseEntity<String>) httpAgentServiceOsgi.executeWithReturnTypeSync(getUrl(), httpEntity, Method.POST);
				
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			LOGGER.error(String
					.format("Login to Motech Platform failed. Response [StatusCode %s] : [RedirectUrl %s]",
							loginResponse.getStatusCode(),
							loginResponse.getHeaders().getLocation()));
		}
		return response;
	}

	/**
	 * Login to Motech Platform Server
	 */
	private ResponseEntity<String> getLogin() {
		LOGGER.info("Trying to login to Motech Platform");
		ResponseEntity<String> response = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		LOGGER.debug("Login Url is: " + propertyReader.getMotechPlatformLoginUrl());
		LOGGER.debug("Login Params are: " + propertyReader.getMotechPlatformLoginForm());
		try {
			/*response = restTemplate.postForEntity(
					propertyReader.getMotechPlatformLoginUrl(), propertyReader.getMotechPlatformLoginForm(), String.class);*/
			response = (ResponseEntity<String>) httpAgentServiceOsgi.executeWithReturnTypeSync(propertyReader.getMotechPlatformLoginUrl(), propertyReader.getMotechPlatformLoginForm(), Method.POST);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.debug("Login Response [StatusCode: " + response.getStatusCode()
				+ "]");
		LOGGER.debug("Login Response [RedirectUrl: "
				+ response.getHeaders().getLocation() + "]");
		return response;
	}

	/**
	 * Returns the url at which hub is to be notified
	 * 
	 * @return
	 */
	public String getUrl() {
		return URL;
	}
}
