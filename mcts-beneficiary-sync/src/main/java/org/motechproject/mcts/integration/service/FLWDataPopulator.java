package org.motechproject.mcts.integration.service;

import java.io.FileReader;

import org.motechproject.mcts.integration.hibernate.model.MctsFlwData;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Service method to read flw CSV file and populate the database
 * @author aman
 *
 */
@Transactional
@Repository
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class FLWDataPopulator {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSHttpClientService.class);
	
	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
	private CareDataRepository careDataRepository;
	
	/**
	 * Method to populate table mcts_HealthWorker 
	 * @throws Exception
	 */
	public void populateFLWData() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					"/home/aman/Downloads/FLW.csv"),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);
			FLWDataCSV flwDataCSV = new FLWDataCSV();
			int count = 0;
			while ((flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null) {
				System.out.println("count" + count++);
				int subcentreId = flwDataCSV.getPHC_ID();
				
				MctsPhc mctsPhc = careDataRepository
						.getMctsPhc(subcentreId);
				if(mctsPhc != null) {
					int healthworkerId = flwDataCSV.getId();
					String name = flwDataCSV.getName();
					String contact_No = flwDataCSV.getContact_No();
					char sex = flwDataCSV.getSex().charAt(0);
					String type = flwDataCSV.getType();

					MctsHealthworker mctsHealthworker = new MctsHealthworker(
							mctsPhc, healthworkerId, name, sex, type);
					mctsHealthworker.setContactNo(contact_No);
					careDataRepository.saveOrUpdate(mctsHealthworker);
				}
				
				else {
						LOGGER.error("invalid phc id in row"+count);
				}
				

			}

		}

		finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

	/**
	 * Method to populate table mcts_flw_master
	 * @throws Exception
	 */
	public void flwDataPopulator() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					"/home/aman/Downloads/FLW.csv"),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);
			FLWDataCSV flwDataCSV = new FLWDataCSV();
			while ((flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null) {

				String districtId = flwDataCSV.getDistrict_ID().toString();
				String talukaId = flwDataCSV.getTaluka_ID().toString();
				String healthBlockId = flwDataCSV.getHealthBlock_ID()
						.toString();
				String subCentreId = flwDataCSV.getSubCentre_ID().toString();
				String villageId = flwDataCSV.getVillage_ID().toString();
				String name = flwDataCSV.getName();
				String sex = flwDataCSV.getSex();
				String type = flwDataCSV.getType();
				String aadharNo = flwDataCSV.getAadhar_No();
				String husbandName = flwDataCSV.getHusband_Name();
				String gfAdress = flwDataCSV.getGF_Address();
				String healthWorkerId = flwDataCSV.getId().toString();
				String contact_No = flwDataCSV.getContact_No();
				String phcId = flwDataCSV.getPHC_ID().toString();
				
				String status = "1";
				String comments = " ";
				MctsPhc mctsPhc = careDataRepository
						.getMctsPhc(flwDataCSV.getPHC_ID());
				if(mctsPhc == null) {
					status = "0";
					comments = "Invalid phc id";
				}
				MctsFlwData mctsFlwData = new MctsFlwData(districtId, talukaId,
						healthBlockId, subCentreId, villageId, name,
						sex, type, aadharNo, husbandName, gfAdress,
						healthWorkerId, contact_No, phcId, status, comments);
				careDataRepository.saveOrUpdate(mctsFlwData);

			}

		}

		finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}

	}
}
