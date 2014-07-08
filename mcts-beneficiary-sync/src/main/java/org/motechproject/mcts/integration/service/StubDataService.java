package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
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
	
	/*@Autowired
	private HttpAgent httpAgentServiceOsgi;
*/
	
	
	/**
	 * Method to call the stub controller and get the data
	 * 
	 * @return
	 */
	public Data getFixtureData() {
		
		String url = propertyReader.getStubUrl();
		ResponseEntity<String> response = restTemplate.getForEntity(url,
				String.class);
		Data data = (Data)XmlStringToObjectConverter.unmarshal(response.getBody(), Data.class);
		//ResponseEntity<DataList> response = (ResponseEntity<DataList>) httpAgentServiceOsgi.executeWithReturnTypeSync(url, null, Method.GET);
		LOGGER.info("returnvalue : "
				+ data.getObjects().get(0).getFixtureType()
				+ " returnVal");
		LOGGER.info("returnvalue : "
				+ data.getObjects().get(0).getFields()
						.getGroupId().getFieldList().get(0).getFieldValue() + " returnVal");
		return data;
	}

}
