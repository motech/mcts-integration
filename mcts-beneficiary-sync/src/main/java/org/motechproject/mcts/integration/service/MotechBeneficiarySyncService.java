package org.motechproject.mcts.integration.service;

import java.io.File;
import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.ObjectToXML;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MotechBeneficiarySyncService {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MotechBeneficiarySyncService.class);

	@Autowired
	private CareDataService careDataService;

	@Autowired
	private MCTSHttpClientService mctsHttpClientService;

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private Publisher publisher;
	
	@Autowired
	private ObjectToXML objectToXML;

	private String outputXMLFileLocation;

	@Autowired
	public MotechBeneficiarySyncService(CareDataService careDataService,
			MCTSHttpClientService mctsHttpClientService,
			PropertyReader propertyReader) {
		this.careDataService = careDataService;
		this.mctsHttpClientService = mctsHttpClientService;
		this.propertyReader = propertyReader;
	}

	public void syncBeneficiaryData(DateTime startDate, DateTime endDate) {
		List<Beneficiary> beneficiariesToSync = getBeneficiariesToSync(
				startDate, endDate);
		LOGGER.info(String.format(
				"Found %s beneficiary records to sync to MCTS",
				beneficiariesToSync.size()));
		if (beneficiariesToSync.isEmpty()) {
			LOGGER.info("No records found to sync. Not sending service update request to MCTS");
			return;
		}
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		beneficiaryRequest = mapToBeneficiaryRequest(beneficiariesToSync);
		HttpStatus httpStatus = syncTo(beneficiaryRequest);
		if (httpStatus.value() / 100 == 2) {
			writeSyncDataToFile(beneficiaryRequest);
			updateSyncedBeneficiaries(beneficiariesToSync);
			notifyHub(beneficiaryRequest);
		}
	}

	protected List<Beneficiary> getBeneficiariesToSync(DateTime startDate,
			DateTime endDate) {
		return careDataService.getBeneficiariesToSync(startDate, endDate);
	}

	protected HttpStatus syncTo(BeneficiaryRequest beneficiaryRequest) {
		return mctsHttpClientService.syncTo(beneficiaryRequest);
	}

	protected void writeSyncDataToFile(BeneficiaryRequest beneficiaryRequest) {
		outputXMLFileLocation = String.format("%s_%s.xml", propertyReader
				.getUpdateXmlOutputFileLocation(),
				DateTime.now().toString("yyyy-MM-dd") + "T"
						+ DateTime.now().toString("HH:mm"));
		String outputURLFileLocation = String.format("%s_%s.txt",
				propertyReader.getUpdateUrlOutputFileLocation(), DateTime.now()
						.toString("yyyy-MM-dd")
						+ "T"
						+ DateTime.now().toString("HH:mm"));
		LOGGER.info("Write Sync Data to File: " + outputXMLFileLocation
				+ " & Urls and Headers to file: " + outputURLFileLocation);
		try {
			File xmlFile = new File(outputXMLFileLocation);
			File updateRequestUrl = new File(outputURLFileLocation);
			objectToXML.writeToXML(beneficiaryRequest,
					BeneficiaryRequest.class, xmlFile, updateRequestUrl);
		} catch (Exception e) {
			LOGGER.error("File Not Found");
		}
	}

	private void notifyHub(BeneficiaryRequest beneficiaryRequest) {
		LOGGER.info("Notifying Hub to Publish the Updates at url"
				+ getHubSyncToUrl());
		publisher.publish(getHubSyncToUrl(), beneficiaryRequest.toString());
	}

	private void updateSyncedBeneficiaries(List<Beneficiary> beneficiariesToSync) {
		LOGGER.info("Updating database with %s Synced Beneficiaries "
				+ beneficiariesToSync.size());
		careDataService.updateSyncedBeneficiaries(beneficiariesToSync);
	}

	protected BeneficiaryRequest mapToBeneficiaryRequest(
			List<Beneficiary> beneficiariesToSync) {
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		Integer stateId = propertyReader.getStateId();
		for (Beneficiary beneficiary : beneficiariesToSync) {
			beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(
					stateId, beneficiary.getMctsId(), beneficiary
							.getServiceType(), beneficiary
							.getServiceDeliveryDate(), beneficiary
							.getMobileNumber(), beneficiary.getHbLevelStr()));
		}
		return beneficiaryRequest;
	}

	public String getHubSyncToUrl() {
		return propertyReader.getHubSyncToUrl() + outputXMLFileLocation;
	}
}
