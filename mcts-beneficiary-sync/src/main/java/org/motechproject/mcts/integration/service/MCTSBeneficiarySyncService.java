package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class MCTSBeneficiarySyncService {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSHttpClientService.class);
	private static final String DATE_FORMAT = "dd-MM-yyyy";

	@Autowired
	private MCTSHttpClientService mctsHttpClientService;
	@Autowired
	private PropertyReader propertyReader;
	@Autowired
	private Publisher publisher;

	private String outputFileLocation;

	@Autowired
	public MCTSBeneficiarySyncService(
			MCTSHttpClientService mctsHttpClientService,
			PropertyReader propertyReader) {
		this.mctsHttpClientService = mctsHttpClientService;
		this.propertyReader = propertyReader;
	}

	public void syncBeneficiaryData(DateTime startDate, DateTime endDate) {
		String beneficiaryData = syncFrom(startDate, endDate);
		LOGGER.info("Get Updates Request Sent To MCTS");
		if (beneficiaryData == null) {
			LOGGER.info("No New Updates Received");
			return;
		}
		writeToFile(beneficiaryData);
		notifyHub(beneficiaryData);
		LOGGER.info("HUB Notified Successfully");
	}

	protected String syncFrom(DateTime startDate, DateTime endDate) {
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.putAll(propertyReader
				.getDefaultBeneficiaryListQueryParams());
		requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
		requestBody.add("ToDate", endDate.toString(DATE_FORMAT));
		return mctsHttpClientService.syncFrom(requestBody);
	}

	protected void writeToFile(String beneficiaryData) {
		this.outputFileLocation = String.format("%s_%s",
				propertyReader.getSyncRequestOutputFileLocation(),
				DateTime.now());
		try {
			FileUtils.writeStringToFile(new File(outputFileLocation),
					beneficiaryData);
			LOGGER.info(String.format(
					"MCTS beneficiary details response is added to file %s",
					outputFileLocation));
		} catch (IOException e) {
			LOGGER.error(
					String.format(
							"Cannot write MCTS beneficiary details response to file: %s",
							outputFileLocation), e);
			return;
		}
	}

	protected void notifyHub(String beneficiaryData) {
		LOGGER.info("Sending Notification to Hub to Publish the Updates at url"
				+ getHubSyncFromUrl());
		publisher.publish(getHubSyncFromUrl(), beneficiaryData);
	}

	public String getHubSyncFromUrl() {
		return propertyReader.getHubSyncFromUrl() + this.outputFileLocation;
	}

}
