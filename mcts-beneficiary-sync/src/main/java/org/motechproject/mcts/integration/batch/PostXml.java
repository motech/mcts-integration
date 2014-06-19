package org.motechproject.mcts.integration.batch;

import java.io.File;
import java.util.Map;

import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Class to post the <code>job<code> configuration xml file to <code>batch</code>  
 * @author Naveen
 *
 */
@Service
public class PostXml {
	  private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);

	    private RestTemplate restTemplate;
	   // private BeneficiarySyncSettings beneficiarySyncSettings;
	    private MultiValueMap<String, Object> formData;
	    private BatchServiceUrlGenerator batchServiceUrlGenerator;
	    @Autowired
	    public PostXml(@Qualifier("mctsRestTemplate") RestTemplate restTemplate,BatchServiceUrlGenerator batchServiceUrlGenerator) {
	        this.restTemplate = restTemplate;
	    }
	    
	    /**
	     * Method to post job configuration xml file to <code>batch</code> module
	     * @param file
	     */
	    public void sendXml(File file) {
	        LOGGER.info("Started service to post xml to batch");
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
	        formData = new LinkedMultiValueMap<String, Object>();
	        formData.add("jobName", "mcts-new-job");
	        formData.add("file", file);
	        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData,httpHeaders);
	        restTemplate.postForEntity(batchServiceUrlGenerator.getUploadXmlUrl(), requestEntity, Map.class);
	      
	    }

}
