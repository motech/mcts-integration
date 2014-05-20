package org.motechproject.mcts.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlStringToObject {

	public <T> T stringXmlToObject(Class<T> clazz, String data) throws Exception{
		InputStream is = new ByteArrayInputStream(data.getBytes());
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(clazz);
		} catch (JAXBException e) {
			throw new Exception(String.format("Invalid Content Received. The Content Received is:\n %s. \nExiting", data), e);
		}
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (clazz.cast(unmarshaller.unmarshal(is)));		
	}
}
