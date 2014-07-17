/**
 * Class to send <code>Get</code> Updates request to MCTS, <code>Add</code> the received updates to database and <code>Notify</code> Hub
 */
package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.HubTransaction;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.utils.BeneficiaryValidator;
import org.motechproject.mcts.utils.DateValidator;
import org.motechproject.mcts.utils.MCTSEventConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.transliteration.service.TransliterationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Transactional
@Service
public class MCTSBeneficiarySyncService {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSBeneficiarySyncService.class);
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
	private TransliterationService transliterationService;
	@Autowired
	private LocationDataPopulator locationDataPopulator;
	@Autowired
	private EventRelay eventRelay;

	/**
	 * Main Method to send <code>Get</code> Updates request to MCTS,
	 * <code>Add</code> the received updates to database and <code>Notify</code>
	 * Hub
	 * 
	 * @param startDate
	 * @param endDate
	 * @throws BeneficiaryException
	 */

	public String syncBeneficiaryData(DateTime startDate, DateTime endDate)

	throws BeneficiaryException {
		NewDataSet newDataSet = syncFrom(startDate, endDate);
		if (newDataSet == null) {
			LOGGER.info("No New Updates Received. Exiting");
			return "No New Updates received";
		}
		int count = addToDbData(newDataSet); // adds updates received to db
		// writeToFile(beneficiaryData); //Writes the Updates received to a file
		// notifyHub(); //Notify the hub about the Updates received
		if (count != 0) {
			// Throws MotechEvent to Notify Updates Added to Database
			HashMap<String, Object> parameters = new HashMap<>();
			parameters.put(MCTSEventConstants.PARAM_PUBLISHER_URL,
					propertyReader.getBenificiaryUpdateTopicUrlForHub());
			MotechEvent motechEvent = new MotechEvent(
					MCTSEventConstants.EVENT_BENEFICIARIES_ADDED, parameters);
			eventRelay.sendEventMessage(motechEvent);
			return String
					.format("%s New Updates Received and %s added to the DB Successfully",
							newDataSet.getRecords().size(), count);
		}

		else {
			return String
					.format("All %s received updates are already present in database or have some error. None added to database.",
							newDataSet.getRecords().size());
		}

	}

	/**
	 * Send the sync request to <code>MCTS</code>
	 * 
	 * @param startDate
	 * @param endDate
	 * @return String of XML of the updates received from MCTS
	 */
	protected NewDataSet syncFrom(DateTime startDate, DateTime endDate) {
		LOGGER.info("Creating Request Body To Be Sent To MCTS");
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.putAll(propertyReader
				.getDefaultBeneficiaryListQueryParams());
		requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
		requestBody.add("ToDate", endDate.toString(DATE_FORMAT));
		return mctsHttpClientService.syncFrom(requestBody);
	}

	/**
	 * Add the updates received from MCTS to database table
	 * <code>mctsPregnantMother</code>
	 * 
	 * @param newDataSet
	 * @return
	 * @throws BeneficiaryException
	 */
	private int addToDbData(NewDataSet newDataSet) throws BeneficiaryException {

		LOGGER.info(String.format("Started writing to db for %s records",
				newDataSet.getRecords().size()));
		int count = 0;
		Date startDate = new Date(); // Sets the startDate when data started to
										// add to db to be send to Hub to query
										// db
		for (Record record : newDataSet.getRecords()) {
			MctsPregnantMother mctsPregnantMother = new MctsPregnantMother();
			String beneficiaryId = record.getBeneficiaryID();

			// check whether this is already present in th database
			if (BeneficiaryValidator.isValidateBeneficiary(record) == true) {
				mctsPregnantMother = careDataService.findEntityByField(
						MctsPregnantMother.class, "mctsId", beneficiaryId);
				// checks if beneficiary already present in db with same mctsId
				if (mctsPregnantMother == null) {
					mctsPregnantMother = mapRecordToMctsPregnantMother(record,
							startDate, mctsPregnantMother, beneficiaryId);
					if (mctsPregnantMother != null) {
						count++;
						careDataService.saveOrUpdate(mctsPregnantMother);
						LOGGER.info(String.format(
								"MctsPregnantMother [%s] added to db",
								mctsPregnantMother.getMctsId()));
					}
				} else {
					mctsPregnantMother = updateRecordToMctsPregnantMother(
							record, startDate, mctsPregnantMother,
							beneficiaryId);
				}
			} else {
				LOGGER.error("Not a Valid Beneficiary");
				addToMctsPregnantMotherMaster(record, startDate);
				LOGGER.error("SKIPPED Adding this record to Database");
			}

			if (mctsPregnantMother == null) {
				addToMctsPregnantMotherMaster(record, startDate);
				LOGGER.error("SKIPPED Adding this record to Database");
			}
		}
		Date endDate = new Date(); // Sets the endDate when data ended to be
									// added to db to be send to Hub to query db
		setHubTransactionDates(startDate, endDate);
		LOGGER.info(String.format("Added %s records to db of %s records.",
				count, newDataSet.getRecords().size()));
		return count;
	}

	/**
	 * 
	 * @param record
	 * @param startDate
	 * @return
	 * @throws BeneficiaryException
	 */

	private MctsPregnantMother updateRecordToMctsPregnantMother(Record record,
			Date startDate, MctsPregnantMother mctsPregnantMother,
			String beneficiaryId) throws BeneficiaryException {

		LOGGER.info(record.toString());

		boolean check = checkMctsPregnantMotherWithRecord(record,
				mctsPregnantMother);
		if (check == true) {
			mctsPregnantMother = mapRecordToMctsPregnantMother(record,
					startDate, mctsPregnantMother, beneficiaryId);
			careDataService.saveOrUpdate(mctsPregnantMother);
			HashMap<String, Object> parameters = new HashMap<>();
			parameters.put(MCTSEventConstants.PARAM_BENEFICIARY_KEY,
					mctsPregnantMother.getId());
			MotechEvent motechEvent = new MotechEvent(
					MCTSEventConstants.EVENT_BENEFICIARY_UPDATED, parameters);
			eventRelay.sendEventMessage(motechEvent);// Throws a
														// motechEvent
														// with Updated
			// Beneficiary

			return mctsPregnantMother;
		} else {
			LOGGER.error(String
					.format("Changes in either AshaId, AnmId or subCentreId. Hence skiping adding to the DataBase"));
			return null;
		}

	}

	/**
	 * Map the <code>Record</code> object received from MCTS to
	 * <code>MctsPregnatMother</code> object to be added to db
	 * 
	 * @param record
	 * @return MctsPregnantMother
	 * @throws BeneficiaryException
	 */
	private MctsPregnantMother mapRecordToMctsPregnantMother(Record record,
			Date startDate, MctsPregnantMother mctsPregnantMother,
			String beneficiaryId) throws BeneficiaryException {

		if (mctsPregnantMother == null) {// if mctsPregnantMother is null then
											// creates a new else updates the
											// existing
			mctsPregnantMother = new MctsPregnantMother();
		}

		String gender = record.getGender();
		String beneficiaryName = record.getBeneficiaryName();
		addLocationToDbIfNotPresent(record);
		Location location = getUniqueLocationMap(record);
		mctsPregnantMother.setMctsVillage(location.getMctsVillage());
		mctsPregnantMother.setMctsSubcenter(location.getMctsSubcenter());
		mctsPregnantMother.setMctsHealthworkerByAshaId(getHealthWorkerId(
				record.getASHAID(), location, "ASHA"));
		mctsPregnantMother.setMctsHealthworkerByAnmId(getHealthWorkerId(
				record.getANMID(), location, "ANM"));
		mctsPregnantMother
				.setBeneficiaryAddress(record.getBeneficiaryAddress());
		mctsPregnantMother.setCategory(record.getCategory());
		mctsPregnantMother.setCreationTime(startDate);
		mctsPregnantMother.setEconomicStatus(record.getEconomicStatus());
		mctsPregnantMother.setEidNumber(record.getEIDNumber());
		mctsPregnantMother.setEmail(record.getEmail());
		mctsPregnantMother.setFatherHusbandName(record.getFatherHusbandName());
		mctsPregnantMother.setHindiFatherHusbandName(transliterate(record
				.getFatherHusbandName()));
		if (gender != null && gender.length() > 0) {
			mctsPregnantMother.setGender(gender.charAt(0));
		}
		mctsPregnantMother.setMctsId(beneficiaryId);
		mctsPregnantMother.setMobileNo(record.getMobileno());
		// checks that beneficiary name cannot be null
		if (beneficiaryName != null && beneficiaryName.length() > 0) {
			mctsPregnantMother.setName(beneficiaryName);
			mctsPregnantMother.setHindiName(transliterate(beneficiaryName));
		} else {
			LOGGER.error(String.format(
					"Beneficiary Name Cannot be null for MctsId[%s]",
					beneficiaryId));
			return null;
		}
		mctsPregnantMother.setPincode(record.getPinCode());
		mctsPregnantMother.setTown(record.getTown());
		mctsPregnantMother.setType(record.getBeneficiaryType());
		mctsPregnantMother.setUidNumber(record.getUIDNumber());
		mctsPregnantMother.setWard(record.getWard());
		// Parse the LmpDate to dd-mm-YYYY format and logs an error if not
		// in correct format

		String lmp = record.getLMPDate();
		if (lmp == null || lmp.isEmpty()) {
			LOGGER.error(String.format("Null value received in LMP Date Field"));
		}
		else {
			Date lmpDate = DateValidator.checkDateInFormat(lmp, "dd-MM-yyyy");
			mctsPregnantMother.setLmpDate(lmpDate);
			LOGGER.debug("lmp date : " + lmp);
			LOGGER.debug("LMP Date is: " + mctsPregnantMother.getLmpDate());
			if (lmpDate == null) {
				LOGGER.error(String
						.format("Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
								record.getLMPDate(), beneficiaryId));
			}
		}
		
		// Parse the BirthDate to dd-mm-YYYY format and logs an error if not
		// in correct format
		String dob = record.getBirthdate();
		if (dob == null || (dob.isEmpty())) {
			LOGGER.error(String
					.format("Null value received in BirthDate Date Field"));
		} else {

			Date dobDate = DateValidator.checkDateInFormat(dob, "dd-MM-yyyy");
			mctsPregnantMother.setBirthDate(dobDate);
			LOGGER.debug("birth date : " + dob);
			LOGGER.debug("Birth Date is: "
					+ mctsPregnantMother.getBirthDate().toString());
			if (dobDate == null) {
				LOGGER.error(String
						.format("Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
								record.getBirthdate(), beneficiaryId));
			}
		}
		return mctsPregnantMother;

	}

	/**
	 * Checks whetehr asha/anm/subcenter changed
	 * 
	 * @param record
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	private boolean checkMctsPregnantMotherWithRecord(Record record,
			MctsPregnantMother mctsPregnantMother) throws BeneficiaryException {
		boolean s = true;
		MctsHealthworker anmWorker = mctsPregnantMother
				.getMctsHealthworkerByAnmId();
		MctsHealthworker ashaWorker = mctsPregnantMother
				.getMctsHealthworkerByAshaId();
		addLocationToDbIfNotPresent(record);
		Location location = getUniqueLocationMap(record);
		MctsSubcenter recordSubcentre = location.getMctsSubcenter();
		MctsSubcenter subCentre = mctsPregnantMother.getMctsSubcenter();
		Integer subcentreId = subCentre.getId();

		if ((anmWorker != null) && (ashaWorker != null) && (subCentre != null)) {
			String recordAnmId = record.getANMID();
			String recordAshaId = record.getASHAID();
			Integer recordSubcenterId = recordSubcentre.getId();
			if (recordAnmId == null
					|| !recordAnmId.equals(String.valueOf(anmWorker
							.getHealthworkerId()))) {
				s = false;
			}
			if (recordAshaId == null
					|| !recordAshaId.equals(String.valueOf(ashaWorker
							.getHealthworkerId()))) {
				s = false;
			}
			if (subcentreId != recordSubcenterId) {
				s = false;
			}

		} else {
			s = false;
		}
		return s;

	}

	/**
	 * Method to check if ANMID or ASHAID or SUbCenterId is updated from
	 * existing record
	 * 
	 * @param record
	 * @param mctsPregnantMother
	 * @return <code>true</code> if none of the above is changed else
	 *         <code>false</code>
	 */

	/**
	 * This is to save the data as it is received from mcts maps record to
	 * MctsPregnatMotherMaster and add to db
	 * 
	 * @param record
	 * @param startDate
	 * @throws BeneficiaryException
	 */
	private void addToMctsPregnantMotherMaster(Record record, Date startDate)
			throws BeneficiaryException {
		MctsPregnantMotherErrorLog mctsPregnantMotherMaster = new MctsPregnantMotherErrorLog();
		mctsPregnantMotherMaster.setaNMID(record.getANMID());
		mctsPregnantMotherMaster.setaSHAID(record.getASHAID());
		mctsPregnantMotherMaster.setBeneficiaryAddress(record
				.getBeneficiaryAddress());
		mctsPregnantMotherMaster.setBeneficiaryID(record.getBeneficiaryID());
		mctsPregnantMotherMaster
				.setBeneficiaryName(record.getBeneficiaryName());
		mctsPregnantMotherMaster
				.setBeneficiaryType(record.getBeneficiaryType());
		mctsPregnantMotherMaster.setBirthdate(record.getBirthdate());
		mctsPregnantMotherMaster.setBlockID(record.getBlockID());
		mctsPregnantMotherMaster.setBlockName(record.getBlockName());
		mctsPregnantMotherMaster.setCategory(record.getCategory());
		mctsPregnantMotherMaster.setDistrictID(record.getDistrictID());
		mctsPregnantMotherMaster.setDistrictName(record.getDistrictName());
		mctsPregnantMotherMaster.setEconomicStatus(record.getEconomicStatus());
		mctsPregnantMotherMaster.seteIDNumber(record.getEIDNumber());
		mctsPregnantMotherMaster.setEmail(record.getEmail());
		mctsPregnantMotherMaster.setFacilityID(record.getFacilityID());
		mctsPregnantMotherMaster.setFacilityName(record.getFacilityName());
		mctsPregnantMotherMaster.setFatherHusbandName(record
				.getFatherHusbandName());
		mctsPregnantMotherMaster.setGender(record.getGender());
		mctsPregnantMotherMaster.setlMPDate(record.getLMPDate());
		mctsPregnantMotherMaster.setlMPDate(record.getMobileno());
		mctsPregnantMotherMaster.setPinCode(record.getPinCode());
		mctsPregnantMotherMaster.setStateID(record.getStateID());
		mctsPregnantMotherMaster.setStateName(record.getStateName());
		mctsPregnantMotherMaster.setSubCentreID(record.getSubCentreID());
		mctsPregnantMotherMaster.setSubCentreName(record.getSubCentreName());
		mctsPregnantMotherMaster.setTehsilID(record.getTehsilID());
		mctsPregnantMotherMaster.setTehsilName(record.getTehsilName());
		mctsPregnantMotherMaster.setTown(record.getTown());
		mctsPregnantMotherMaster.setuIDNumber(record.getUIDNumber());
		mctsPregnantMotherMaster.setVillageID(record.getVillageID());
		mctsPregnantMotherMaster.setVillageName(record.getVillageName());
		mctsPregnantMotherMaster.setWard(record.getWard());
		mctsPregnantMotherMaster.setCreationTime(startDate);
		careDataService.saveOrUpdate(mctsPregnantMotherMaster);
	}

	/**
	 * map record to <code>LocationCSV</code> and adds to db if not present
	 * 
	 * @param record
	 * @throws BeneficiaryException
	 */
	private void addLocationToDbIfNotPresent(Record record)
			throws BeneficiaryException {

		LocationDataCSV locationDataCSV = new LocationDataCSV();
		locationDataCSV.setStateID(record.getStateID());
		locationDataCSV.setState(record.getStateName());
		locationDataCSV.setDCode(record.getDistrictID());
		locationDataCSV.setDistrict(record.getDistrictName());
		locationDataCSV.setVCode(record.getVillageID());
		locationDataCSV.setVillage(record.getVillageName());
		locationDataCSV.setSUBCenter(record.getSubCentreName());
		locationDataCSV.setSID(record.getSubCentreID());
		locationDataCSV.setBID(record.getBlockID());
		locationDataCSV.setBlock(record.getBlockName());
		locationDataCSV.setTCode(record.getTehsilID());
		locationDataCSV.setTaluka_Name(record.getTehsilName());
		locationDataCSV.setPHC(record.getFacilityName());
		locationDataCSV.setPID(record.getFacilityID());
		locationDataPopulator.addLocationToDb(locationDataCSV, false);
	}

	/**
	 * Maps MctsHealthworkerId to MotechHealthworkerId Creates new HealthWorker
	 * if the healthworker by Id passed doesnot exist and return its Id
	 * 
	 * @param mctsHealthWorkerId
	 * @return
	 * @throws BeneficiaryException
	 */
	private MctsHealthworker getHealthWorkerId(String mctsHealthWorkerId,
			Location location, String type) throws BeneficiaryException {
		Integer healthWorkerId = IntegerValidator.validateAndReturnAsInt(
				"mctsHealthWorkerId", mctsHealthWorkerId);
		if (healthWorkerId == null) {
			return null;
		}
		MctsHealthworker mctsHealthworker = careDataService.findEntityByField(
				MctsHealthworker.class, "healthworkerId", healthWorkerId);
		// Checks if HealthWorker exist in db...if not then logs an error
		// message and creates a new healthworker and return it
		if (mctsHealthworker == null) {
			LOGGER.error(String
					.format("HealthWorker with HealthworkerId: %s doesNot exist in DataBase. Adding new record in HealthWorker Table",
							mctsHealthWorkerId));
			mctsHealthworker = new MctsHealthworker();
			mctsHealthworker.setHealthworkerId(healthWorkerId);

			mctsHealthworker.setMctsPhc(location.getMctsPhc());
			mctsHealthworker.setMctsSubcenter(location.getMctsSubcenter());
			mctsHealthworker.setMctsVillage(location.getMctsVillage());
			mctsHealthworker.setName("asd");
			mctsHealthworker.setSex('F');
			mctsHealthworker.setType(type);
			mctsHealthworker.setStatus(false);
			careDataService.saveOrUpdate(mctsHealthworker);
			return careDataService.findEntityByField(MctsHealthworker.class,
					"healthworkerId", healthWorkerId);
		} else {
			return mctsHealthworker;
		}

	}

	/**
	 * Maps record to a unique location and returns the <code>Location</code>
	 * 
	 * @param record
	 * @return
	 * @throws BeneficiaryException
	 */
	private Location getUniqueLocationMap(Record record)
			throws BeneficiaryException {
		Location location = new Location();
		location.setMctsState(careDataService.findEntityByField(
				MctsState.class,
				"stateId",
				IntegerValidator.validateAndReturnAsInt("stateId",
						record.getStateID())));
		try {
			// sets District
			HashMap<String, Object> params = new HashMap<String, Object>();

			params.put("mctsState", location.getMctsState());

			params.put("disctrictId", IntegerValidator.validateAndReturnAsInt(
					"disctrictId", record.getDistrictID()));
			location.setMctsDistrict(careDataService
					.findListOfEntitiesByMultipleField(MctsDistrict.class,
							params).get(0));
			// sets Taluka
			params = new HashMap<String, Object>();
			params.put("mctsDistrict", location.getMctsDistrict());
			params.put(
					"talukId",
					IntegerValidator.validateAndReturnAsInt("talukId",
							record.getTehsilID()));
			location.setMctsTaluk(careDataService
					.findListOfEntitiesByMultipleField(MctsTaluk.class, params)
					.get(0));
			// sets Village
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			if (record.getVillageID() != null && !record.getVillageID().isEmpty()) {
				params.put(
						"villageId",
						IntegerValidator.validateAndReturnAsInt("villageId",
								record.getVillageID()));
				location.setMctsVillage(careDataService
						.findListOfEntitiesByMultipleField(MctsVillage.class,
								params).get(0));
			}
			
			// sets HealthBlock
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			params.put("healthblockId", IntegerValidator
					.validateAndReturnAsInt("healthblockId",
							record.getBlockID()));
			location.setMctsHealthblock(careDataService
					.findListOfEntitiesByMultipleField(MctsHealthblock.class,
							params).get(0));
			// sets Phc
			params = new HashMap<String, Object>();
			params.put("mctsHealthblock", location.getMctsHealthblock());
			params.put(
					"phcId",
					IntegerValidator.validateAndReturnAsInt("phcId",
							record.getFacilityID()));
			location.setMctsPhc(careDataService
					.findListOfEntitiesByMultipleField(MctsPhc.class, params)
					.get(0));
			// sets SubCenter
			params = new HashMap<String, Object>();
			params.put("mctsPhc", location.getMctsPhc());
			params.put("subcenterId", IntegerValidator.validateAndReturnAsInt(
					"subcenterId", record.getSubCentreID()));
			location.setMctsSubcenter(careDataService
					.findListOfEntitiesByMultipleField(MctsSubcenter.class,
							params).get(0));
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Invalid Location Code Received"), e);
			throw new BeneficiaryException(ApplicationErrors.NUMBERS_MISMATCH,e);
		}
		return location;
	}

	/**
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	public String transliterate(String word) throws BeneficiaryException {
		LOGGER.info("Creating new transliteration");
		String hindi = transliterationService.transliterate(word);
		LOGGER.debug(String.format("Tranlation for [%s] received is [%s]",
				word, hindi));
		return hindi;
	}

	/**
	 * Write the Beneficiary Data to a new XML file with timeStamp
	 * 
	 * @param beneficiaryData
	 * @throws BeneficiaryException
	 */
	@Deprecated
	protected void writeToFile(String beneficiaryData)
			throws BeneficiaryException {
		String outputFileLocation = String.format("%s_%s.xml", propertyReader
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
			String error = String
					.format("Cannot write MCTS beneficiary details response to file: %s. Reason %s",
							outputFileLocation, e.getMessage());
			LOGGER.error(error);
			throw new BeneficiaryException(
					ApplicationErrors.FILE_READING_WRTING_FAILED, error);
		}
	}

	/**
	 * Sets startDate and endDate to be sent to hub
	 * 
	 * @throws BeneficiaryException
	 */
	protected void setHubTransactionDates(Date startDate, Date endDate)
			throws BeneficiaryException {
		HubTransaction hubTransaction = new HubTransaction();
		hubTransaction.setStartDate(startDate);
		hubTransaction.setEndDate(endDate);
		hubTransaction.setIsNotified(false);
		careDataService.saveOrUpdate(hubTransaction);
	}

	/**
	 * Notifies the Hub when the Updates received from Mcts with Url to call
	 * Back
	 * 
	 * @throws BeneficiaryException
	 */
	protected void notifyHub() throws BeneficiaryException {
		String updateUrl = propertyReader.getBenificiaryUpdateTopicUrlForHub();
		LOGGER.info("Sending Notification to Hub about Updates at Topic url"
				+ updateUrl);
		publisher.publish(updateUrl);
	}

	
}
