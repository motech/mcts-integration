/**
 * Class to send Notificaion to Hub about the Updates
 */
package org.motechproject.mcts.integration.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
	private final static String MODE = "publish";
	private final static Logger LOGGER = LoggerFactory
			.getLogger(Publisher.class);
	private static String URL;

	/**
	 * Method to <code>Publish</code> updates to hub along with <code>callBack URL</code> and
	 * to <code>retry</code> sending notifications if failed.
	 * @param url
	 * @throws Exception 
	 */
	public void publish(String url) throws Exception {
		setUrl(url);
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
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
//			try {
//				Thread.sleep(propertyReader.getHubRetryInterval());
//			} catch (InterruptedException e) {
//				LOGGER.debug(e.getMessage());
//			}
		} while (retryCount < maxRetryCount
				&& response.getStatusCode().value() / 100 != 2);

		if (response.getStatusCode().value() / 100 == 2)
			LOGGER.info(String
					.format("Hub Notified Successfully with %s retries. Response [StatusCode %s] : %s",
							retryCount, response.getStatusCode(),
							response.getBody()));
		else
			{LOGGER.error(String
					.format("Notification to Hub failed. Response [StatusCode %s] : %s",
							response.getStatusCode(), response.getBody()));
			throw new Exception(String
					.format("Notification to Hub failed. Response [StatusCode %s] : %s",
							response.getStatusCode(), response.getBody()));
			}
	}

	/**
	 * Method to set the url at which Hub is to be Notified
	 * @param url
	 * @throws UnsupportedEncodingException 
	 */
	private void setUrl(String url) throws UnsupportedEncodingException {
		URL = String.format("%s%s%s%s%s", propertyReader.getHubBaseUrl(),
				"?hub.mode=", URLEncoder.encode(MODE, "UTF-8"), "&hub.url=", URLEncoder.encode(url, "UTF-8"));
	}

	/**
	 * <code>Posts</code> the Http entity to Hub with <code>callBack Url</code> and <code>ContentType</code> as passed in argument
	 * @param contentType
	 * @return
	 * @throws Exception 
	 */
	private ResponseEntity<String> notifyHub(){
		//TODO edit the headers
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		LOGGER.debug("Notify hub at the url: " + getUrl());
		try{
		response = restTemplate.postForEntity(getUrl(),
				httpEntity, String.class);}
		catch (Exception e){
			LOGGER.error(e.getMessage());
		}
		return response;
	}
	
	/**
	 * Returns the url at which hub is to be notified
	 * @return
	 */
	public String getUrl() {
		return URL;
	}
}
