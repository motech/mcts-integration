/**
 * Interface to translate a word from one to another lanuage
 * @author Mohit
 */
package org.motechproject.mcts.integration.service;

import java.nio.charset.Charset;

import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransliterationServiceImpl{

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TransliterationServiceImpl.class);
	
	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Translate a word from english to hindi by calling transliteratioon api of motech-platform-server
	 * @param word
	 * @return hindi translation of input word
	 */
	public String englishToHindi(String word) {
		LOGGER.debug("String received to be transliterated: " + word);
	String url = propertyReader.getTransliterationUrl(word);
		HttpHeaders httpHeaders = new HttpHeaders();
		MediaType mediaType = new MediaType("text", "xml", Charset.forName("UTF-8"));
		httpHeaders.setContentType(mediaType);
		HttpEntity<String> httpEntity = new HttpEntity<String>("parameters",
				httpHeaders);
		ResponseEntity<String> response = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		ResponseEntity<String> loginResponse = new ResponseEntity<String>(
				HttpStatus.BAD_REQUEST);
		loginResponse = getLogin();
		LOGGER.debug("Matching Urls: "
				+ propertyReader.getMotechLoginRedirectUrl() + " & "
				+ loginResponse.getHeaders().getLocation().toString());
		if (propertyReader.getMotechLoginRedirectUrl().equals(
				loginResponse.getHeaders().getLocation().toString())) {
			LOGGER.info("Succefully logged in to Motech-Platform");
			try {
				response = restTemplate.exchange(url, HttpMethod.GET,
						httpEntity, String.class);
				LOGGER.debug(String
						.format("Response Received from Tranliterator [StatusCode: %s] [ContentType: %s, Charset: %s]: %s",
								response.getStatusCode(), response.getHeaders().getContentType(), response.getHeaders().getContentType().getCharSet(), response.getBody()));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			LOGGER.error(String
					.format("Login to Motech Platform failed. Response [StatusCode %s] : [RedirectUrl %s]",
							loginResponse.getStatusCode(), loginResponse
									.getHeaders().getLocation()));
		}
		return response.getBody();

/*		TransliterationController transliterationController = new TransliterationController();
		return transliterationController.handleIncoming(word);*/ }

	/**
	 * Login to Motech Platform Server
	 */
	private ResponseEntity<String> getLogin() {
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
