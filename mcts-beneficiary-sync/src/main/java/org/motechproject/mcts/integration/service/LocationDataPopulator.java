package org.motechproject.mcts.integration.service;

//import org.motechproject.mcts.integration.hibernate.model.Locationdata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsLocationMaster;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

@Transactional
@Service
// @TransactionConfiguration(transactionManager = "txManager", defaultRollback =
// true)
public class LocationDataPopulator {
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(LocationDataPopulator.class);

	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
	private CareDataRepository careDataRepository;
	
//	@Autowired
//	private CSVFileReader cSVFileReader;
	
	

	
	public LocationDataPopulator() {

	}

	/**
	 * Method to populate locations such as state, district, taluka, block, phc,
	 * subcentre, village
	 * 
	 * @param file
	 * @throws BeneficiaryException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public void populateLocations(MultipartFile file)
			throws BeneficiaryException {

		File newFile = null;
		
		ICsvBeanReader beanReader = null;
		// String filePath= "/home/aman/Downloads/location.csv";

		LocationDataCSV locationCSV = new LocationDataCSV();
		try {
			
			byte[] bytes = file.getBytes();
			LOGGER.info("thispath" + System.getProperty("java.io.tmpdir"));
			String path = System.getProperty("java.io.tmpdir");
			newFile = new File(path + "/beneficiary.xml");
			FileOutputStream out = new FileOutputStream(newFile);
			out.write(bytes);
			LOGGER.info("size" + newFile.getTotalSpace());
			LOGGER.info("temp path" + newFile.getAbsolutePath());

			beanReader = new CsvBeanReader(new FileReader(newFile),
					CsvPreference.STANDARD_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			int count = 0;
			LOGGER.info("Writing locations to database");
			while ((locationCSV = beanReader
					.read(LocationDataCSV.class, header)) != null) {
				
				addLocationToDb(locationCSV, true);
			}

		}

		catch (FileNotFoundException e) {
			throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND,
					e.getMessage());
		} catch (IOException e) {
			throw new BeneficiaryException(
					ApplicationErrors.FILE_READING_WRTING_FAILED,
					e.getMessage());
		}
		catch (SuperCsvReflectionException e) {
			throw new BeneficiaryException(ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS,e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new BeneficiaryException(ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH,e.getMessage());
		}

		finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					throw new BeneficiaryException(
							ApplicationErrors.FILE_CLOSING_FAILED,
							e.getMessage());
				}
			}
			saveLocationData(newFile);
		}

	}
	
	/**
	 * @throws BeneficiaryException 
	 * 
	 */
	public void addLocationToDb(LocationDataCSV locationCSV, boolean status) throws BeneficiaryException{
		int stateId = locationCSV.getStateID();
		String StateName = locationCSV.getState();
		int disctrictId = locationCSV.getDCode();
		String disctrictName = locationCSV.getDistrict();
		int talukId = locationCSV.getTCode();
		String talukaName = locationCSV.getTaluka_Name();
		int healthblockId = locationCSV.getBID();
		String healthblockName = locationCSV.getBlock();
		int phcId = locationCSV.getPID();
		String phcName = locationCSV.getPHC();
		int subcenterId = locationCSV.getSID();
		String subcentreName = locationCSV.getSUBCenter();
		int villageId = locationCSV.getVCode();
		String villageName = locationCSV.getVillage();

		
		MctsState mctsState = careDataRepository.findEntityByField(
				MctsState.class, "stateId", stateId);
		if (mctsState == null) {
			mctsState = new MctsState(stateId, StateName);
			mctsState.setStatus(false);
		}
		if (!mctsState.getStatus()) {
			mctsState.setStatus(status);
			careDataRepository.saveOrUpdate(mctsState);
		}
			
			
		
		MctsDistrict mctsDistrict = careDataRepository
				.findUniqueDistrict(disctrictId,mctsState.getId());
		if (mctsDistrict == null ) {
			mctsDistrict = new MctsDistrict(mctsState, disctrictId,
					disctrictName);
			mctsDistrict.setStatus(false);
		}
		if (!mctsDistrict.getStatus()) {
			mctsDistrict.setStatus(status);
			careDataRepository.saveOrUpdate(mctsDistrict);
		}

		MctsTaluk mctsTaluk = careDataRepository.findUniqueTaluk(talukId,mctsDistrict.getId());
		if (mctsTaluk == null) {
			mctsTaluk = new MctsTaluk(mctsDistrict, talukId, talukaName);
			mctsTaluk.setStatus(false);
		}
		if (!mctsTaluk.getStatus()) {
			mctsTaluk.setStatus(status);
			careDataRepository.saveOrUpdate(mctsTaluk);
		}
		
		MctsHealthblock mctsHealthblock = careDataRepository
				.findUniqueHealthBlock(healthblockId,mctsTaluk.getId());
		if (mctsHealthblock == null) {
			mctsHealthblock = new MctsHealthblock(mctsTaluk,
					healthblockId, healthblockName);
			mctsHealthblock.setStatus(false);
		}
		if (!mctsHealthblock.getStatus()) {
			mctsHealthblock.setStatus(status);
			careDataRepository.saveOrUpdate(mctsHealthblock);
		}

		MctsPhc mctsPhc = careDataRepository.findUniquePhc(phcId,mctsHealthblock.getId());
		if (mctsPhc == null) {
			mctsPhc = new MctsPhc(mctsHealthblock, phcId, phcName);
			mctsPhc.setStatus(false);
		}
		if (!mctsPhc.getStatus()) {
			mctsPhc.setStatus(status);
			careDataRepository.saveOrUpdate(mctsPhc);
		}
		
		MctsSubcenter mctsSubcenter = careDataRepository
				.findUniqueSubcentre(subcenterId,mctsPhc.getId());
		if (mctsSubcenter == null) {
			mctsSubcenter = new MctsSubcenter(mctsPhc, subcenterId,
					subcentreName);
			mctsSubcenter.setStatus(false);
		}
		if (!mctsSubcenter.getStatus()) {
			mctsSubcenter.setStatus(status);
			careDataRepository.saveOrUpdate(mctsSubcenter);
		}
		
		MctsVillage mctsVillage = careDataRepository.findUniqueVillage(villageId,mctsSubcenter.getId(),mctsTaluk.getId());
		if (mctsVillage == null) {
			mctsVillage = new MctsVillage(mctsTaluk, mctsSubcenter,
					villageId, villageName);
			mctsVillage.setStatus(false);
		}
		if (!mctsVillage.getStatus()) {
			mctsVillage.setStatus(status);
			careDataRepository.saveOrUpdate(mctsVillage);
		}
	}

	/**
	 * Method to populate location master table
	 * 
	 * @throws BeneficiaryException
	 * @throws IOException
	 */
	public void saveLocationData(File file) throws BeneficiaryException {

		ICsvBeanReader beanReader = null;
		// String filePath= "/home/aman/Downloads/location.csv";

		LocationDataCSV locationCSV = new LocationDataCSV();
		try {

			beanReader = new CsvBeanReader(new FileReader(file),
					CsvPreference.STANDARD_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			int count = 0;
			while ((locationCSV = beanReader
					.read(LocationDataCSV.class, header)) != null) {

				System.out.println("count" + count++);
				String stateId = locationCSV.getStateID().toString();
				String state = locationCSV.getState();
				String disctrictId = locationCSV.getDCode().toString();
				String disctrictName = locationCSV.getDistrict();
				String talukId = locationCSV.getTCode().toString();
				String talukaName = locationCSV.getTaluka_Name();
				String healthblockId = locationCSV.getBID().toString();
				String healthblockName = locationCSV.getBlock();
				String phcId = locationCSV.getPID().toString();
				String phcName = locationCSV.getPHC();
				String subcenterId = locationCSV.getSID().toString();
				String subcentreName = locationCSV.getSUBCenter();
				String villageId = locationCSV.getVCode().toString();
				String villageName = locationCSV.getVillage();
				String status = "1";
				String comments = " ";

				MctsLocationMaster mctsLocationMaster = new MctsLocationMaster(
						stateId, state, disctrictId, disctrictName, talukId,
						talukaName, healthblockId, healthblockName, phcId,
						phcName, subcenterId, subcentreName, villageId,
						villageName, status, comments);
				careDataRepository.saveOrUpdate(mctsLocationMaster);
			}
		}

		catch (FileNotFoundException e) {
			throw new BeneficiaryException(ApplicationErrors.FILE_NOT_FOUND,
					e.getMessage());
		} catch (IOException e) {
			throw new BeneficiaryException(
					ApplicationErrors.FILE_READING_WRTING_FAILED,
					e.getMessage());
		}
		catch (SuperCsvReflectionException e) {
			throw new BeneficiaryException(ApplicationErrors.CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS,e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new BeneficiaryException(ApplicationErrors.NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH,e.getMessage());
		}

		finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					throw new BeneficiaryException(
							ApplicationErrors.FILE_CLOSING_FAILED,
							e.getMessage());
				}
			}
		}

	}

}
