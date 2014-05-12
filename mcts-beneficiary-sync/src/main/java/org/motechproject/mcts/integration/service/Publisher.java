package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Publisher {

	private final static String MODE="publish";
	private final static Logger LOGGER = LoggerFactory.getLogger(Publisher.class);
	private PropertyReader beneficiarySyncSettings;
	private RestTemplate restTemplate;
	private static String URL;
	private static String DATA;
	
	public void publish(String url, String data){
		DATA = data;
		setUrl(url);
		LOGGER.info("Syncing beneficiary data to MCTS.");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(getUrl(), httpEntity, String.class);
        if (response != null)
            LOGGER.info(String.format("Hub Notified Successfully. Response [StatusCode %s] : %s", response.getStatusCode(), response.getBody()));
	}
	
	private void setUrl(String url)
	{
		URL= String.format("%s%s%s%s%s",beneficiarySyncSettings.getHubBaseUrl(), "/hub?hub.mode=", MODE, "&hub.url=", url);
	}
	
	public String getUrl(){
		return URL;
	}
	
	public String getData(){
		return this.DATA;
	}
}
