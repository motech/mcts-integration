/**
 * Class to Fetch updates from Motech Db and Send them to Mcts
 */
package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
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

	private String outputXMLFileLocation;

	@Autowired
	public MotechBeneficiarySyncService(CareDataService careDataService,
			MCTSHttpClientService mctsHttpClientService,
			PropertyReader propertyReader) {
		this.careDataService = careDataService;
		this.mctsHttpClientService = mctsHttpClientService;
		this.propertyReader = propertyReader;
	}

	/**
	 * Fetches Updates from <code>Motech</code> db, post the updates to Mcts
	 * Updates the <code>mcts_pregnant_mother_service_updates</code> table with the updates sent to Mcts 
	 * @param startDate
	 * @param endDate
	 * @throws FileNotFoundException 
	 * @throws BeneficiaryException 
	 */
	public void syncBeneficiaryData(DateTime startDate, DateTime endDate) throws FileNotFoundException, BeneficiaryException {
		List<Beneficiary> beneficiariesToSync = getBeneficiariesToSync(
				startDate, endDate);//gets the updates from the Motech Database
		LOGGER.info(String.format(
				"Found %s beneficiary records to sync to MCTS",
				beneficiariesToSync.size()));
		if (beneficiariesToSync.isEmpty()) {
			//TODO send this message to the client
			LOGGER.info("No records found to sync. Not sending service update request to MCTS");
			return;
		}
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		beneficiaryRequest = mapToBeneficiaryRequest(beneficiariesToSync);
		HttpStatus httpStatus = syncTo(beneficiaryRequest);//sends Updates to Mcts
		//if httpStatus returned from Mcts is 2** then update the mcts_pregnant_mother_service_updates table
		if (httpStatus.value() / 100 == 2) {
			writeSyncDataToFile(beneficiaryRequest); //Writes the updates sent to Mcts to a file
			updateSyncedBeneficiaries(beneficiariesToSync);
		}
	}

	/**
	 * Calls the Method from <code>CareDataService</code> class to get the <code>Beneficiaries</code> Updates to be sent to Mcts
	 * @param startDate
	 * @param endDate
	 * @return <code>List of Beneficiaries</code> to be sent to Mcts
	 */
	protected List<Beneficiary> getBeneficiariesToSync(DateTime startDate,
			DateTime endDate) {
		return careDataService.getBeneficiariesToSync(startDate, endDate);
	}

	/**
	 * Maps the List of Beneficiaries received from Database to <code>BeneficiaryRequest</code> to be sent to Mcts
	 * @param beneficiariesToSync
	 * @return
	 */
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

	/**
	 * Calls the Method from <code>MCTSHttpClientService</code> class to send the updates to Mcts
	 * @param beneficiaryRequest
	 * @return
	 */
	protected HttpStatus syncTo(BeneficiaryRequest beneficiaryRequest) {
		return mctsHttpClientService.syncTo(beneficiaryRequest);
	}
	
	/**
	 * Write the updates to be sent to Mcts in a xml file
	 * @param beneficiaryRequest
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("deprecation")
	protected void writeSyncDataToFile(BeneficiaryRequest beneficiaryRequest) throws FileNotFoundException {
		//TODO: To be removed in future and post updates directly to Mcts
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
		File xmlFile = new File(outputXMLFileLocation);
		File updateRequestUrl = new File(outputURLFileLocation);
		PrintWriter printWriter = new PrintWriter(xmlFile);
		try {
			String data = ObjectToXMLConverter.converObjectToXml(beneficiaryRequest,
					BeneficiaryRequest.class);
			LOGGER.info("Updates Received are:\n" + data);
			printWriter.println(data);
			ObjectToXMLConverter.writeUrlToFile(xmlFile, updateRequestUrl);
		} catch (Exception e) {
			LOGGER.error("File Not Found");
		}finally {
			printWriter.close();
		}
	}
	
	/**
	 * Calls <code>updateSyncedBeneficiaries</code> from <code>CareDataService</code> class to 
	 * update the <code>mcts_pregnant_mother_service_updates</code> table with the updates sent to Mcts 
	 * @param beneficiariesToSync List of beneficiaries sent to Mcts
	 * @throws BeneficiaryException 
	 */
	private void updateSyncedBeneficiaries(List<Beneficiary> beneficiariesToSync) throws BeneficiaryException {
		LOGGER.info("Updating database with %s Synced Beneficiaries "
				+ beneficiariesToSync.size());
		careDataService.updateSyncedBeneficiaries(beneficiariesToSync);
	}
}
