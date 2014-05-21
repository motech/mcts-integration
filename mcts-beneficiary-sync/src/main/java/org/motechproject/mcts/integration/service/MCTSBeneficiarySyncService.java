package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Transactional
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

	public void syncBeneficiaryData(DateTime startDate, DateTime endDate)
			throws Exception {
		String beneficiaryData = syncFrom(startDate, endDate);
		if (beneficiaryData == null) {
			LOGGER.info("No New Updates Received. Exiting");
			return;
		}
		NewDataSet newDataSet = null;
		try {
			newDataSet = xmlStringToObject.stringXmlToObject(NewDataSet.class,
					beneficiaryData);
			if (newDataSet.getRecords().size() == 0) {
				LOGGER.info("No New Updates Received. Exiting");
				return;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new Exception(e);
		}
		addToDbData(newDataSet);
		writeToFile(beneficiaryData);
		notifyHub(beneficiaryData);
	}

	private void addToDbData(NewDataSet newDataSet){
		LOGGER.info(String.format("Started writing to db for %s records",
				newDataSet.getRecords().size()));
		int count = 0;
		for (Record record : newDataSet.getRecords()) {
			MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
				mctsPregnantMother = mapRecordToMctsPregnantMother(record);
				if (mctsPregnantMother != null){
					careDataRepository.saveOrUpdate(mctsPregnantMother);
					count++;
					LOGGER.info(String.format(
							"MctsPregnantMother [%s] added to db",
							mctsPregnantMother.getMctsId()));
				}
				else
					LOGGER.error("SKIPPED Adding this record to Database");
		}
		LOGGER.info(String.format("Added %s records to db of %s records.",
				count, newDataSet.getRecords().size()));
	}

	private MctsPregnantMother mapRecordToMctsPregnantMother(Record record){
		Date date = new Date();
		MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();

		LOGGER.info(record.toString());
		MctsHealthworker mctsHealthworkerByAshaId = null;
		MctsHealthworker mctsHealthworkerByAnmId = null;
		MctsVillage mctsVillage = null;
		MctsSubcenter mctsSubcenter = null;
		mctsHealthworkerByAshaId = careDataRepository.findEntityByField(
				MctsHealthworker.class, "healthworkerId",
				Integer.parseInt(record.getASHAID()));
		if (mctsHealthworkerByAshaId == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getASHAID(), record.getBeneficiaryID()));
			return null;
		}
		mctsHealthworkerByAnmId = careDataRepository.findEntityByField(
				MctsHealthworker.class, "healthworkerId",
				Integer.parseInt(record.getANMID()));
		if (mctsHealthworkerByAnmId == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getANMID(), record.getBeneficiaryID()));
			return null;
		}
		mctsVillage = careDataRepository.findEntityByField(MctsVillage.class,
				"villageId", Integer.parseInt(record.getVillageID()));
		if (mctsVillage == null) {
			LOGGER.error(String
					.format("Village with VillageId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getVillageID(), record.getBeneficiaryID()));
			return null;
		}
		mctsSubcenter = careDataRepository.findEntityByField(
				MctsSubcenter.class, "subcenterId",
				Integer.parseInt(record.getSubCentreID()));
		if (mctsSubcenter == null) {
			LOGGER.error(String
					.format("SubCenter with SubCenter: %s for Mcts record: %s doesNot exist in DataBase",
							record.getSubCentreID(), record.getBeneficiaryID()));
			return null;
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
			mctsPregnantMother.setLmpDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getLMPDate()));
		} catch (ParseException e) {
			LOGGER.error(
					String.format(
							"Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getLMPDate(), record.getBeneficiaryID()));
			return null;
		}
		try {
			mctsPregnantMother.setBirthDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getBirthdate()));
		} catch (ParseException e) {
			LOGGER.error(
					String.format(
							"Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getBirthdate(), record.getBeneficiaryID()));
			return null;
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

	protected void writeToFile(String beneficiaryData) throws Exception {
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
			throw new Exception(
					String.format(
							"Cannot write MCTS beneficiary details response to file: %s. Reason %s",
							outputFileLocation, e.getMessage()));
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
