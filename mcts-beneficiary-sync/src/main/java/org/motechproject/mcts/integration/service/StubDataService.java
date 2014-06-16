package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.model.DataList;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service to call the fixture stub and get data 
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

	
	
	/**
	 * Method to call the stub controller and get the data
	 * 
	 * @return
	 */
	public DataList getFixtureData() {
		
		String url = propertyReader.getStubUrl();
		restTemplate.getMessageConverters().add(
				new MappingJacksonHttpMessageConverter());
		ResponseEntity<DataList> response = restTemplate.getForEntity(url,
				DataList.class);
		LOGGER.info("returnvalue : "
				+ response.getBody().getDataList().get(0).getFixtureType()
				+ " returnVal");
		LOGGER.info("returnvalue : "
				+ response.getBody().getDataList().get(0).getFields()
						.getGroupId() + " returnVal");
		return response.getBody();
	}

}
