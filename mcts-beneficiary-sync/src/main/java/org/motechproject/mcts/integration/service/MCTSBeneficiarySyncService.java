/**
 * Class to send <code>Get</code> Updates request to MCTS, <code>Add</code> the received updates to database and <code>Notify</code> Hub
 */
package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
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
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMaster;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;

import org.motechproject.mcts.integration.repository.CareDataRepository;

import org.motechproject.mcts.utils.MCTSEventConstants;

import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.transliteration.service.TransliterationService;
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
	private CareDataService careDataService;
	@Autowired CareDataRepository careDataRepository;
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
		// TODO to be added hubNotification for 0.24
		// notifyHub(); //Notify the hub about the Updates received
		if (count != 0) {
			// Throws MotechEvent to Notify Updates Added to Database
			HashMap<String, Object> parameters = new HashMap<>();
			parameters.put(MCTSEventConstants.PARAM_PUBLISHER_URL,
					propertyReader.getBenificiaryUpdateTopicUrlForHub());
			MotechEvent motechEvent = new MotechEvent(
					MCTSEventConstants.EVENT_BENEFICIARIES_ADDED, parameters);
			eventRelay.sendEventMessage(motechEvent);
			return String.format(
					"%s New Updates Received and %s Processed Successfully",
					newDataSet.getRecords().size(), count);
		} else
			return String
					.format("All %s received updates are in error. None added to database.",
							newDataSet.getRecords().size());

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

			//check whether this is already present in th database

			mctsPregnantMother = matchRecordToMctsPregnantMother(record,
					startDate);
			if (mctsPregnantMother != null) {
				careDataService.saveOrUpdate(mctsPregnantMother);
				count++;
				LOGGER.info(String.format(
						"MctsPregnantMother [%s] added to db",
						mctsPregnantMother.getMctsId()));
			} else
				addToMctsPregnantMotherMaster(record, startDate);
			LOGGER.error("SKIPPED Adding this record to Database");
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
	private MctsPregnantMother matchRecordToMctsPregnantMother(Record record,
			Date startDate) throws BeneficiaryException {
		String beneficiaryId = record.getBeneficiaryID();
		MctsPregnantMother mctsPregnantMother = careDataService
				.findEntityByField(MctsPregnantMother.class, "mctsId",
						beneficiaryId);
		LOGGER.info(record.toString());
		// checks that Beneficiary Id should be 18 char long
		if (beneficiaryId != null && beneficiaryId.length() == 18) {
			// checks if beneficiary already present in db with same mctsId
			if (record.getBeneficiaryName().length() > 0) {
				if (mctsPregnantMother == null) {
					mctsPregnantMother = mapRecordToMctsPregnantMother(record,
							startDate, mctsPregnantMother, beneficiaryId);
				} else {
					boolean check = checkMctsPregnantMotherWithRecord(record,
							mctsPregnantMother);
					if (check == true) {
						mctsPregnantMother = mapRecordToMctsPregnantMother(
								record, startDate, mctsPregnantMother,
								beneficiaryId);
						HashMap<String, Object> parameters = new HashMap<>();
						parameters.put(
								MCTSEventConstants.PARAM_BENEFICIARY_KEY,
								mctsPregnantMother);
						MotechEvent motechEvent = new MotechEvent(
								MCTSEventConstants.EVENT_BENEFICIARY_UPDATED);
						eventRelay.sendEventMessage(motechEvent);// Throws a
																	// motechEvent
																	// with
																	// Updated
																	// Beneficiary
					} else {
						LOGGER.error(String
								.format("Changes in either AshaId, AnmId or subCentreId. Hence skiping adding to the DataBase"));
						return null;
					}
				}
			} else {
				LOGGER.error("Beneficiary name cannot be null. Data not added to Db");
				return null;
			}
		} else {
			LOGGER.error("Beneficiary Id is invalid. Data not added to Db");
			return null;
		}
		return mctsPregnantMother;
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

		if (mctsPregnantMother == null) {//if mctsPregnantMother is null then creates a new else updates the existing
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
		try {
			mctsPregnantMother.setLmpDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getLMPDate()));
			LOGGER.debug("LMP Date is: "
					+ mctsPregnantMother.getLmpDate().toString());
		} catch (ParseException e) {
			LOGGER.error(String
					.format("Invalid LMP Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getLMPDate(), beneficiaryId));
		} catch (NullPointerException e) {
			LOGGER.error(String.format("Null value received in LMP Date Field"));
		}
		// Parse the BirthDate to dd-mm-YYYY format and logs an error if not
		// in correct format
		try {
			mctsPregnantMother.setBirthDate(new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH).parse(record.getBirthdate()));
			LOGGER.debug("Birth Date is: "
					+ mctsPregnantMother.getBirthDate().toString());
		} catch (ParseException e) {
			LOGGER.error(String
					.format("Invalid Birth Date[%s] for Beneficiary Record: %s. Correct format is dd-mm-yyyy",
							record.getBirthdate(), beneficiaryId));
		} catch (NullPointerException e) {
			LOGGER.error(String
					.format("Null value received in BirthDate Date Field"));
		}
		return mctsPregnantMother;


	}

	/**
	 * Checks whetehr asha/anm/subcenter changed
	 * @param record
	 * @param mctsPregnantMother
	 * @return
	 */
	private boolean checkMctsPregnatMotherWithRecord(Record record,
			MctsPregnantMother mctsPregnantMother) {
		boolean s = true;
		MctsHealthworker anmWorker = mctsPregnantMother
				.getMctsHealthworkerByAnmId();
		MctsHealthworker ashaWorker = mctsPregnantMother
				.getMctsHealthworkerByAshaId();
		MctsSubcenter subCentre = mctsPregnantMother.getMctsSubcenter();
		if ((anmWorker != null) && (ashaWorker != null) && (subCentre != null)) {
			String recordAnmId = record.getANMID();
			String recordAshaId = record.getASHAID();
			String recordSubcenterId = record.getSubCentreID();
			if(recordAnmId == null || !recordAnmId.equals(String.valueOf(anmWorker
					.getHealthworkerId()))){
				s = false;
			}
			if(recordAshaId == null || !recordAshaId.equals(String.valueOf(ashaWorker
					.getHealthworkerId()))){
				s = false;
			}
			if(recordSubcenterId == null || !recordSubcenterId.equals(String.valueOf(subCentre.getSubcenterId()))){
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
	private boolean checkMctsPregnantMotherWithRecord(Record record,
			MctsPregnantMother mctsPregnantMother) {
		boolean check = false;
		MctsHealthworker AnmWorker = mctsPregnantMother
				.getMctsHealthworkerByAnmId();
		MctsHealthworker AshaWorker = mctsPregnantMother
				.getMctsHealthworkerByAshaId();
		MctsSubcenter subCentre = mctsPregnantMother.getMctsSubcenter();
		if ((AnmWorker != null) && (AshaWorker != null) && (subCentre != null)) {
			if ((record.getANMID() == String.valueOf(AnmWorker
					.getHealthworkerId()))
					&& (record.getASHAID() == String.valueOf(AshaWorker
							.getHealthworkerId()) && (record.getSubCentreID() == String
							.valueOf(subCentre.getSubcenterId())))) {
				check = true;
			} else {
				check = false;
			}
		}
		return check;

	}

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
		MctsPregnantMotherMaster mctsPregnantMotherMaster = new MctsPregnantMotherMaster();
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
		int healthWorkerId = validateAndReturnAsInt("mctsHealthWorkerId",
				mctsHealthWorkerId);
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
	 * Validate if the string is int or not && return by converting it to int
	 * 
	 * @param field
	 * @param id
	 * @return
	 * @throws BeneficiaryException
	 */
	private int validateAndReturnAsInt(String field, String id)
			throws BeneficiaryException {
		//TODO
		if (id.isEmpty()==false && id != null/* && id.substring(1, id.length()-1).matches("//d+")*/) {


			return Integer.parseInt(id);
		} else
			throw new BeneficiaryException(ApplicationErrors.INVALID_ARGUMENT,
					String.format("Value received for [%s : %s] is invalid",
							field, id));
	}

	/**
	 * Maps record to a unique location and returns the <code>Location</code>
	 * 
	 * @param locationCSV
	 * @return
	 * @throws BeneficiaryException
	 */
	private Location getUniqueLocationMap(Record locationCSV)
			throws BeneficiaryException {
		Location location = new Location();
		location.setMctsState(careDataService.findEntityByField(
				MctsState.class, "stateId", Integer.parseInt(record.getStateID())));
		try {
			// sets District
			HashMap<String, Object> params = new HashMap<String, Object>();

			params.put("mctsState", location.getMctsState());

			params.put(
					"disctrictId",
					validateAndReturnAsInt("disctrictId",
							locationCSV.getDistrictID()));
			location.setMctsDistrict(careDataService
					.findListOfEntitiesByMultipleField(MctsDistrict.class,
							params).get(0));
			// sets Taluka
			params = new HashMap<String, Object>();
			params.put("mctsDistrict", location.getMctsDistrict());
			params.put(
					"talukId",
					validateAndReturnAsInt("talukId", locationCSV.getTehsilID()));
			location.setMctsTaluk(careDataService
					.findListOfEntitiesByMultipleField(MctsTaluk.class, params)
					.get(0));
			// sets Village
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			params.put(
					"villageId",
					validateAndReturnAsInt("villageId",
							locationCSV.getVillageID()));
			location.setMctsVillage(careDataService
					.findListOfEntitiesByMultipleField(MctsVillage.class,
							params).get(0));
			// sets HealthBlock
			params = new HashMap<String, Object>();
			params.put("mctsTaluk", location.getMctsTaluk());
			params.put(
					"healthblockId",
					validateAndReturnAsInt("healthblockId",
							locationCSV.getBlockID()));
			location.setMctsHealthblock(careDataService
					.findListOfEntitiesByMultipleField(MctsHealthblock.class,
							params).get(0));
			// sets Phc
			params = new HashMap<String, Object>();
			params.put("mctsHealthblock", location.getMctsHealthblock());
			params.put(
					"phcId",
					validateAndReturnAsInt("phcId", locationCSV.getFacilityID()));
			location.setMctsPhc(careDataService
					.findListOfEntitiesByMultipleField(MctsPhc.class, params)
					.get(0));
			// sets SubCenter
			params = new HashMap<String, Object>();
			params.put("mctsPhc", location.getMctsPhc());
			params.put(
					"subcenterId",
					validateAndReturnAsInt("subcenterId",
							locationCSV.getSubCentreID()));
			location.setMctsSubcenter(careDataService
					.findListOfEntitiesByMultipleField(MctsSubcenter.class,
							params).get(0));
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Invalid Location Code Received"), e);
			// TODO throw proper Exception
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
