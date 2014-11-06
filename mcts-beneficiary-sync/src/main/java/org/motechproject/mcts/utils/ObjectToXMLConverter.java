/**
 * Method to convert an object to xml
 */
package org.motechproject.mcts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.springframework.stereotype.Service;

@Service
public final class ObjectToXMLConverter {

    private ObjectToXMLConverter() {

    }

    private static final String USERNAME = "mcts-MOTECH";
    private static String password;
    private static final String OPERATION = "Insert";
    private static final String CONTENT_TYPE = "text/xml";

    /**
     * Converts the received object to XML and return as String
     *
     * @param dataToWrite
     * @param classInstance
     * @return
     * @throws Exception
     */
    public static String converObjectToXml(Object dataToWrite,
            Class<?> classInstance) {
        if (dataToWrite.getClass() != classInstance) {
            throw new BeneficiaryException(
                    ApplicationErrors.CLASS_AND_OBJECT_DOES_NOT_MATCH);
        } else {
            try {
                JAXBContext jaxbContext = JAXBContext
                        .newInstance(classInstance);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                // Set UTF-8 Encoding
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

                // The below code will take care of avoiding the conversion of <
                // to &lt; and > to &gt; etc
                StringWriter stringWriter = new StringWriter();
                jaxbMarshaller.marshal(dataToWrite, stringWriter);
                return stringWriter.toString();
            } catch (PropertyException e) {
                throw new BeneficiaryException(
                        ApplicationErrors.PROPERTY_ERROR, e);
            } catch (JAXBException e) {
                throw new BeneficiaryException(ApplicationErrors.JAXB_ERROR, e);
            }

        }
    }

    /**
     * This method write the mcts url and headers to the txetfile
     *
     * @param xmlFile
     *            : the xmlfile whose content:length is to be calculated
     * @param updateRequestUrlFile
     *            : file to which headers and url are to be written
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws BeneficiaryException
     */
    @Deprecated
    public static String writeUrlToFile(File xmlFile, File updateRequestUrlFile)
            throws IOException, URISyntaxException {
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
                                    USERNAME, "&Sec_Code=", password, "&Op=",
                                    OPERATION));
            requestURL.append(String.format("%s%s\n", "Content-Length: ",
                    contentLength));
            requestURL.append(String.format("%s%s", "Content-Type: ",
                    CONTENT_TYPE));
            printWriter.println(requestURL);
        } finally {
            printWriter.close();
            bufferReader.close();
        }
        return requestURL.toString();
    }

}
