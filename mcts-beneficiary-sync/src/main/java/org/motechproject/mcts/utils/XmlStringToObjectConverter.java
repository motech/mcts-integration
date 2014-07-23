package org.motechproject.mcts.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryError;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XmlStringToObjectConverter {

	private final static Logger LOGGER = LoggerFactory.getLogger(XmlStringToObjectConverter.class);
	private static ObjectMapper objectMapper = null;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	public static ObjectMapper getObjectMapper() {
    	return objectMapper;
    }
	@SuppressWarnings("unchecked")
	public static <T> T stringXmlToObject(Class<T> clazz, String data) throws BeneficiaryException  {
		StringReader reader = new StringReader(data);
		JAXBContext jc;
		LOGGER.debug(data);
		Object object;
		try {
			jc = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
	        object = unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
		    throw new BeneficiaryException(ApplicationErrors.JAXB_PARSING_ERROR,e,e.getMessage());
		}
		
		return (T) object;
	}
	
	public static <T> T unmarshal(String json, Class<T> klass) { 
		try {
			return objectMapper.readValue(json, klass);
		} catch (JsonParseException e) {
			LOGGER.error("Not able to unmarshal:{}", json, e);
			return null;
		} catch (JsonMappingException e) {
			LOGGER.error("Not able to unmarshal:{}", json, e);
			return null;
		} catch (IOException e) {
			LOGGER.error("Not able to unmarshal:{}", json, e);
			return null;
		}
	}
	
	
}
