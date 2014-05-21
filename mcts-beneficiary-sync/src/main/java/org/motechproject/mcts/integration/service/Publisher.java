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
	private static String DATA;

	public void publish(String url, String data) {
		DATA = data;
		setUrl(url);
		LOGGER.info("Syncing beneficiary data to MCTS.");

		ResponseEntity<String> response = null;
		int maxRetryCount = propertyReader.getMaxNumberOfPublishRetryCount();
		int retryCount = -1;
		do {
			LOGGER.info(String.format("Notify Hub for %s time at url %s.",
					retryCount, URL));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (retryCount == maxRetryCount
				|| response.getStatusCode().value() / 100 == 2);

		if (response.getStatusCode().value() / 100 == 2)
			LOGGER.info(String
					.format("Hub Notified Successfully after %s retries. Response [StatusCode %s] : %s",
							retryCount, response.getStatusCode(),
							response.getBody()));
		else
			LOGGER.error(String
					.format("Notification to Hub failed. Response [StatusCode %s] : %s",
							response.getStatusCode(), response.getBody()));
	}

	private ResponseEntity<String> notify(MediaType contentType) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(contentType);
		HttpEntity httpEntity = new HttpEntity(httpHeaders);
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl(),
				httpEntity, String.class);
		return response;
	}

	private void setUrl(String url) {
		URL = String.format("%s%s%s%s%s", propertyReader.getHubBaseUrl(),
				"?hub.mode=", MODE, "&hub.url=", url);
	}

	public String getUrl() {
		return URL;
	}

	public String getData() {
		return DATA;
	}
}
