/**
 * Class to send Notificaion to Hub about the Updates
 */
package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
	 */
	public void publish(String url) {
		setUrl(url);
		ResponseEntity<String> response = null;
		int maxRetryCount = propertyReader.getMaxNumberOfPublishRetryCount();
		int retryCount = -1;
		do {
			LOGGER.info(String.format("Notifying Hub for %s time at url: %s",
					retryCount + 2, URL));
			try {
				response = notify(MediaType.APPLICATION_FORM_URLENCODED);
				retryCount++;
			} catch (Exception e) {
				retryCount++;
			}
			if (response.getStatusCode().value() / 100 == 2) {
				LOGGER.info(String
						.format("Hub Notified Successfully after %s retries. Response [StatusCode %s] : %s",
								retryCount, response.getStatusCode(),
								response.getBody()));
				return;
			}
			try {
				Thread.sleep(propertyReader.getHubRetryInterval());
			} catch (InterruptedException e) {
				LOGGER.debug(e.getMessage());
			}
		} while (retryCount == maxRetryCount
				|| response.getStatusCode().value() / 100 == 2);

		if (response.getStatusCode().value() / 100 == 2)
			LOGGER.info(String
					.format("Hub Notified Successfully with %s retries. Response [StatusCode %s] : %s",
							retryCount, response.getStatusCode(),
							response.getBody()));
		else
			LOGGER.error(String
					.format("Notification to Hub failed. Response [StatusCode %s] : %s",
							response.getStatusCode(), response.getBody()));
	}

	/**
	 * Method to set the url at which Hub is to be Notified
	 * @param url
	 */
	private void setUrl(String url) {
		URL = String.format("%s%s%s%s%s", propertyReader.getHubBaseUrl(),
				"?hub.mode=", MODE, "&hub.url=", url);
	}

	/**
	 * <code>Posts</code> the Http entity to Hub with <code>callBack Url</code> and <code>ContentType</code> as passed in argument
	 * @param contentType
	 * @return
	 */
	private ResponseEntity<String> notify(MediaType contentType) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(contentType);
		HttpEntity httpEntity = new HttpEntity(httpHeaders);
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl(),
				httpEntity, String.class);
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
