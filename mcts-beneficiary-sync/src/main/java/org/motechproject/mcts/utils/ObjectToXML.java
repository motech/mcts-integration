package org.motechproject.mcts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Component;

@Component
public class ObjectToXML {

	private final String username = "mcts-MOTECH";
	private String password;
	private final String operation = "Insert";
	private final String contentType = "text/xml";

	public void writeToXML(Object dataToWrite,
			Class<?> classInstance, File xmlFile, File textFile) throws Exception {

		if (dataToWrite.getClass() != classInstance) {
			throw new Exception(
					"Passed Object and Class don't match. Can't write to XML.");
		} else {
			JAXBContext jaxbContext = JAXBContext.newInstance(classInstance);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(dataToWrite, xmlFile);
			writeUrlToFile(xmlFile, textFile);
		}
	}

	/**
	 * This method write the mcts url and headers to the txetfile
	 * @param xmlFile : the xmlfile whose content:length is to be calculated
	 * @param updateRequestUrlFile : file to which headers and url are to be written
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String writeUrlToFile(File xmlFile, File updateRequestUrlFile) throws IOException, URISyntaxException {
		//TODO: This method to be removed during actual implementation.
		FileReader fileReader = new FileReader(xmlFile);
		password = Encryption.encryptWithTimeInSeconds("mcts-MOTECH@123");
		BufferedReader bufferReader = new BufferedReader(fileReader);
		String xmlContent = bufferReader.readLine();
		int contentLength = xmlContent.getBytes("UTF-8").length;
    	PrintWriter printWriter = new PrintWriter(updateRequestUrlFile);
		StringBuilder requestURL = new StringBuilder();
		try {
			requestURL
					.append(String
							.format("%s%s%s%s%s%s\n\n",
									"http://10.24.103.51/MOTECH_Service_Updation/Default.aspx?url=",
									username, "&Sec_Code=", password, "&Op=",
									operation));
			requestURL.append(String.format("%s%s\n", "Content-Length: ",
					contentLength));
			requestURL.append(String.format("%s%s", "Content-Type: ",
					contentType));
			printWriter.println(requestURL);
		} finally {
			printWriter.close();
		}
return requestURL.toString();
	}
	
	
	
	
}