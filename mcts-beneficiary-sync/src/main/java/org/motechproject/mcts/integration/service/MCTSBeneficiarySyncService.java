/**
 * Class to send <code>Get</code> Updates request to MCTS, <code>Add</code> the received updates to database and <code>Notify</code> Hub
 */
package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.HubTransaction;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
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
	private CareDataService careDataService;
	@Autowired
	private XmlStringToObjectConverter xmlStringToObject;

	private String outputFileLocation;
	private Date startDate;
	private Date endDate;

	/**
	 * Main Method to send <code>Get</code> Updates request to MCTS, <code>Add</code> the received updates to database and <code>Notify</code> Hub
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
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
		addToDbData(newDataSet); //adds updates received to db
		//writeToFile(beneficiaryData); //Writes the Updates received to a file
		setHubTransactionDates();
		notifyHub(); //Notify the hub about the Updates received
	}

	/**
	 * Send the sync request to <code>MCTS</code>
	 * @param startDate
	 * @param endDate
	 * @return String of XML of the updates received from MCTS
	 */
	protected String syncFrom(DateTime startDate, DateTime endDate) {
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.putAll(propertyReader
				.getDefaultBeneficiaryListQueryParams());
		requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
		requestBody.add("ToDate", endDate.toString(DATE_FORMAT));
		return mctsHttpClientService.syncFrom(requestBody);
	}

	/**
	 * Add the updates received from MCTS to database table <code>mctsPregnantMother</code>
	 * @param newDataSet: 
	 * @throws BeneficiaryException 
	 */
	private void addToDbData(NewDataSet newDataSet) throws BeneficiaryException {
		LOGGER.info(String.format("Started writing to db for %s records",
				newDataSet.getRecords().size()));
		int count = 0;
		startDate = new Date(); //Sets the startDate when data started to add to db to be send to Hub to query db
		for (Record record : newDataSet.getRecords()) {
			MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
			mctsPregnantMother = mapRecordToMctsPregnantMother(record);
			if (mctsPregnantMother != null) {
				careDataService.saveOrUpdate(mctsPregnantMother);
				count++;
				LOGGER.info(String.format(
						"MctsPregnantMother [%s] added to db",
						mctsPregnantMother.getMctsId()));
			} else
				LOGGER.error("SKIPPED Adding this record to Database");
		}
		endDate = new Date(); //Sets the endDate when data ended to be added to db to be send to Hub to query db
		LOGGER.info(String.format("Added %s records to db of %s records.",
				count, newDataSet.getRecords().size()));
	}
	
	/**
	 * Method to change <code>isLatest</code> field of <code>MctsPregnatMother</code> for the mothers whose
	 * updates were already published
	 *//*
	@Deprecated
	private void changeIsLatestToFalse(){
		List<MctsPregnantMother> mctsPregnantMothers = careDataService.findListOfEntitiesByField(MctsPregnantMother.class, "isLatest", true);
		for (MctsPregnantMother mctsPregnantMother: mctsPregnantMothers){
			mctsPregnantMother.setIsLatest(false);
			careDataService.saveOrUpdate(mctsPregnantMother);
		}
	}*/

	/**
	 * Map the <code>Record</code> object received from MCTS to <code>MctsPregnatMother</code> object to be added to db 
	 * @param record
	 * @return MctsPregnantMother
	 * @throws BeneficiaryException 
	 * @throws NumberFormatException 
	 */
	private MctsPregnantMother mapRecordToMctsPregnantMother(Record record) throws NumberFormatException, BeneficiaryException {
		MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
		LOGGER.info(record.toString());
		MctsHealthworker mctsHealthworkerByAshaId = null;
		MctsHealthworker mctsHealthworkerByAnmId = null;
		MctsVillage mctsVillage = null;
		MctsSubcenter mctsSubcenter = null;
		mctsHealthworkerByAshaId = careDataService.findEntityByField(
				MctsHealthworker.class, "healthworkerId",
				Integer.parseInt(record.getASHAID()));
		//Checks if HealthWorker exist in db...if not then skip adding the particular record to db and throws an error message
		if (mctsHealthworkerByAshaId == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getASHAID(), record.getBeneficiaryID()));
			return null;
		}
		mctsHealthworkerByAnmId = careDataService.findEntityByField(
				MctsHealthworker.class, "healthworkerId",
				Integer.parseInt(record.getANMID()));
		//Checks if HealthWorker exist in db...if not then skip adding the particular record to db and throws an error message
		if (mctsHealthworkerByAnmId == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getANMID(), record.getBeneficiaryID()));
			return null;
		}
		mctsVillage = careDataService.findEntityByField(MctsVillage.class,
				"villageId", Integer.parseInt(record.getVillageID()));
		//Checks if Village exist in db...if not then skip adding the particular record to db and throws an error message
		if (mctsVillage == null) {
			LOGGER.error(String
					.format("Village with VillageId: %s for Mcts record: %s doesNot exist in DataBase",
							record.getVillageID(), record.getBeneficiaryID()));
			return null;
		}
		mctsSubcenter = careDataService.findEntityByField(
				MctsSubcenter.class, "subcenterId",
				Integer.parseInt(record.getSubCentreID()));
		//Checks if SubCenter exist in db...if not then skip adding the particular record to db and throws an error message
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
		mctsPregnantMother.setCreationTime(startDate);
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
		//Parse the LmpDate to dd-mm-YYYY format and throws an error if not in correct format
		try {
			mctsPregnantMother.setLmpDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getLMPDate()));
			LOGGER.debug("LMP Date is: "
					+ mctsPregnantMother.getLmpDate().toString());
		} catch (ParseException e) {
			LOGGER.error(String
					.format("Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getLMPDate(), record.getBeneficiaryID()));
			return null;
		}
		//Parse the BirthDate to dd-mm-YYYY format and throws an error if not in correct format
		try {
			mctsPregnantMother.setBirthDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getBirthdate()));
			LOGGER.debug("Birth Date is: "
					+ mctsPregnantMother.getBirthDate().toString());
		} catch (ParseException e) {
			LOGGER.error(String
					.format("Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getBirthdate(), record.getBeneficiaryID()));
			return null;
		}
		return mctsPregnantMother;
	}

	/**
	 * Write the Beneficiary Data to a new XML file with timeStamp
	 * @param beneficiaryData
	 * @throws Exception
	 */
	@Deprecated
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

	/**
	 * Sets startDate and endDate to be sent to hub
	 * @throws BeneficiaryException 
	 */
	protected void setHubTransactionDates() throws BeneficiaryException{
		HubTransaction hubTransaction = new HubTransaction();
		hubTransaction.setStartDate(startDate);
		hubTransaction.setEndDate(endDate);
		hubTransaction.setIsNotified(false);
		careDataService.saveOrUpdate(hubTransaction);
	}
	
	/**
	 * Notifies the Hub when the Updates received from Mcts with Url to call Back
	 * @throws Exception 
	 */
	protected void notifyHub() throws Exception {
		String updateUrl = propertyReader.getBenificiaryUpdateTopicUrlForHub();
		LOGGER.info("Sending Notification to Hub about Updates at Topic url"
				+ updateUrl);
		publisher.publish(updateUrl);
	}
}
