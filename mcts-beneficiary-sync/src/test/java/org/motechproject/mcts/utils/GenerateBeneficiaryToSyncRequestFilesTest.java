package org.motechproject.mcts.utils;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mock;

public class GenerateBeneficiaryToSyncRequestFilesTest {

	@Mock
	GenerateBeneficiaryToSyncRequestFiles generateFiles;
	
	@Mock
	Encryption encryption;
	private final String username = "mcts-MOTECH";
	private final String operation = "Insert";
	private final String contentType = "text/xml";
	private final String outputFile = "/home/beehyv/workspace/UpdateRequestUrlTest.txt";
	private GenerateBeneficiaryToSyncRequestFiles generateBeneficiaryToSyncRequestFiles = new GenerateBeneficiaryToSyncRequestFiles();
	
	
	@Test
	public void shouldGenerateBeneficiarySyncRequestFile() throws IOException, URISyntaxException {
		File xmlFile = new File(getClass().getClassLoader().getResource("TestServiceUpdates.xml").toURI());
		File txtFile = new File(getClass().getClassLoader().getResource("TestUpdateRequestUrl.txt").toURI());
		System.out.println(generateBeneficiaryToSyncRequestFiles.generateBeneficiarySyncRequestFile(xmlFile, txtFile));

	}
	
}
