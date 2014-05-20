package org.motechproject.mcts.integration.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObject;
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
	@Autowired
	private CareDataRepository careDataRepository;
	@Autowired
	private XmlStringToObject xmlStringToObject;

	private String outputFileLocation;

	@Autowired
	public MCTSBeneficiarySyncService(
			MCTSHttpClientService mctsHttpClientService,
			PropertyReader propertyReader) {
		this.mctsHttpClientService = mctsHttpClientService;
		this.propertyReader = propertyReader;
	}

	public void syncBeneficiaryData(DateTime startDate, DateTime endDate)
			throws Exception {
		String beneficiaryData = syncFrom(startDate, endDate);
		NewDataSet newDataSet = null;
		try{
			newDataSet = xmlStringToObject.stringXmlToObject(NewDataSet.class, beneficiaryData);
		} catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new Exception(e);
		}
		LOGGER.info("Get Updates Request Sent To MCTS");
		if (beneficiaryData == null) {
			LOGGER.info("No New Updates Received");
			return;
		}
		addToDbData(newDataSet);
		writeToFile(beneficiaryData);
		notifyHub(beneficiaryData);
	}
	
	private void addToDbData(NewDataSet newDataSet) {
		LOGGER.info(String.format("Started writing to db for %s records",
				newDataSet.getRecords().size()));
		int count = 0;
		for (Record record : newDataSet.getRecords()) {
			MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
			try {
				mctsPregnantMother = mapRecordToMctsPregnantMother(record);
				careDataRepository.saveOrUpdate(mctsPregnantMother);
				count++;
				LOGGER.info(String.format(
						"MctsPregnantMother [%s] added to db",
						mctsPregnantMother.getMctsId()));
			} catch (Exception e) {
				LOGGER.error(e.getMessage()
						+ "Skipped Adding this record to Database");
			}
		}
		LOGGER.info(String.format("Added %s records to db of %s records.",
				count, newDataSet.getRecords().size()));
	}

	private MctsPregnantMother mapRecordToMctsPregnantMother(Record record)
			throws Exception {
		Date date = new Date();
		MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();

		MctsHealthworker mctsHealthworkerByAshaId = null;
		MctsHealthworker mctsHealthworkerByAnmId = null;
		MctsVillage mctsVillage = null;
		MctsSubcenter mctsSubcenter = null;
		try {
			mctsHealthworkerByAshaId = careDataRepository.findEntityByField(
					MctsHealthworker.class, "healthworkerId",
					Integer.parseInt(record.getASHAID()));
		} catch (NullPointerException e) {
			throw new Exception(
					String.format(
							"HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getASHAID(), record.getBeneficiaryID()));
		}
		try {
			mctsHealthworkerByAnmId = careDataRepository.findEntityByField(
					MctsHealthworker.class, "healthworkerId",
					Integer.parseInt(record.getANMID()));
		} catch (NullPointerException e) {
			throw new Exception(
					String.format(
							"HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getANMID(), record.getBeneficiaryID()));
		}
		try {
			mctsVillage = careDataRepository.findEntityByField(
					MctsVillage.class, "villageId", record.getVillageID());
		} catch (NullPointerException e) {
			throw new Exception(
					String.format(
							"Village with VillageId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getVillageID(), record.getBeneficiaryID()));
		}
		try {
			mctsSubcenter = careDataRepository
					.findEntityByField(MctsSubcenter.class, "subcenterId",
							record.getSubCentreID());
		} catch (NullPointerException e) {
			throw new Exception(
					String.format(
							"SubCenter with SubCenter: %s for Mcts record: %s doesNot exist in DataBase",
							record.getSubCentreID(), record.getBeneficiaryID()));
		}

		mctsPregnantMother.setMctsHealthworkerByAnmId(mctsHealthworkerByAnmId);
		mctsPregnantMother
				.setMctsHealthworkerByAshaId(mctsHealthworkerByAshaId);
		mctsPregnantMother
				.setBeneficiaryAddress(record.getBeneficiaryAddress());
		mctsPregnantMother.setCategory(record.getCategory());
		mctsPregnantMother.setCreationTime(date);
		mctsPregnantMother.setEconomicStatus(record.getEconomicStatus());
		mctsPregnantMother.setEidNumber(record.getEIDNumber());
		mctsPregnantMother.setEmail(record.getEmail());
		mctsPregnantMother.setFatherHusbandName(record.getFatherHusbandName());
		mctsPregnantMother.setGender(record.getGender().charAt(0));
		mctsPregnantMother.setMctsId(record.getBeneficiaryID());
		mctsPregnantMother.setMobileNo(record.getMobileno());
		mctsPregnantMother.setName(record.getBeneficiaryName());
		mctsPregnantMother.setPincode(record.getPinCode());
		mctsPregnantMother.setMctsSubcenter(mctsSubcenter);
		mctsPregnantMother.setTown(record.getTown());
		mctsPregnantMother.setType(record.getBeneficiaryType());
		mctsPregnantMother.setUidNumber(record.getUIDNumber());
		mctsPregnantMother.setMctsVillage(mctsVillage);
		mctsPregnantMother.setWard(record.getWard());
		try {
			mctsPregnantMother.setLmpDate(new SimpleDateFormat("dd-MM-yyyy",
					Locale.ENGLISH).parse(record.getLMPDate()));
		} catch (ParseException e) {
			throw new Exception(
					String.format(
							"Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getLMPDate(), record.getBeneficiaryID()));
		}
		try {
			mctsPregnantMother.setBirthDate(new SimpleDateFormat("dd-MM-yyyy",
					Locale.ENGLISH).parse(record.getBirthdate()));
		} catch (ParseException e) {
			throw new Exception(
					String.format(
							"Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getBirthdate(), record.getBeneficiaryID()));
		}
		return mctsPregnantMother;
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
		this.outputFileLocation = String.format("%s_%s.xml", propertyReader
				.getSyncRequestOutputFileLocation(),
				DateTime.now().toString("yyyy-MM-dd") + "T"
						+ DateTime.now().toString("HH:mm"));
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
		LOGGER.info("HUB Notified Successfully");
	}

	public String getHubSyncFromUrl() {
		return propertyReader.getHubSyncFromUrl() + this.outputFileLocation;
	}

}
