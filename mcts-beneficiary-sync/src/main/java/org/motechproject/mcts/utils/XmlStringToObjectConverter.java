package org.motechproject.mcts.utils;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XmlStringToObjectConverter {

	private final static Logger LOGGER = LoggerFactory.getLogger(XmlStringToObjectConverter.class);

	@SuppressWarnings("unchecked")
	public static <T> T stringXmlToObject(Class<T> clazz, String data) throws Exception{
		StringReader reader = new StringReader(data);
		JAXBContext jc;
		LOGGER.debug(data);
		try {
			jc = JAXBContext.newInstance(clazz);
		} catch (JAXBException e) {
			throw new Exception(String.format("Invalid Content Received. The Content Received is:\n %s. \nExiting", data), e);
		}
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object object = unmarshaller.unmarshal(reader);
		return (T) object;
	}
}
