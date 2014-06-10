package org.motechproject.mcts.integration.service;

//import org.motechproject.mcts.integration.hibernate.model.Locationdata;
import java.io.File;
import java.io.FileReader;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

@Transactional
@Repository

//@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class LocationDataPopulator {

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
	 * Method to populate locations such as state, district, taluka, block, phc, subcentre, village
	 * @param file
	 * @throws Exception
	 */
	public void populateLocations(File file) throws Exception {
		
			ICsvBeanReader beanReader = null;
			//String filePath= "/home/aman/Downloads/location.csv";
			
			
			LocationDataCSV locationCSV = new LocationDataCSV();
			try{
			beanReader = new CsvBeanReader(new FileReader(file),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);
			int count = 0;
			while((locationCSV = beanReader.read(LocationDataCSV.class, header))!=null) {

				
					System.out.println("count" + count++);
					
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
					
					
					
					MctsState mctsState = careDataRepository.findEntityByField(MctsState.class, "stateId", stateId);
					if(mctsState == null) {
						mctsState = new MctsState(stateId,StateName);
						careDataRepository.saveOrUpdate(mctsState);
					}
					
					MctsDistrict mctsDistrict = careDataRepository.findEntityByField(MctsDistrict.class, "disctrictId", disctrictId);
					if(mctsDistrict == null) {	
						mctsDistrict = new MctsDistrict(mctsState,
								disctrictId, disctrictName);
						careDataRepository.saveOrUpdate(mctsDistrict);
					}
							
					MctsTaluk mctsTaluk = careDataRepository.findEntityByField(MctsTaluk.class, "talukId", talukId);
					if(mctsTaluk == null) {
						mctsTaluk = new MctsTaluk(mctsDistrict,
								talukId,
								talukaName);
						careDataRepository.saveOrUpdate(mctsTaluk);
					}
					
					MctsHealthblock mctsHealthblock = careDataRepository.findEntityByField(MctsHealthblock.class, "healthblockId", healthblockId);
					if(mctsHealthblock == null) {
						mctsHealthblock = new MctsHealthblock(mctsTaluk,healthblockId,healthblockName);
						careDataRepository.saveOrUpdate(mctsHealthblock);
					}
					
					MctsPhc mctsPhc = careDataRepository.findEntityByField(MctsPhc.class, "phcId", phcId);
					if(mctsPhc == null) {
						mctsPhc = new MctsPhc(mctsHealthblock,phcId,phcName);
						careDataRepository.saveOrUpdate(mctsPhc);
					}
					
					MctsSubcenter mctsSubcenter = careDataRepository.findEntityByField(MctsSubcenter.class, "subcenterId", subcenterId);
					if(mctsSubcenter == null) {
						mctsSubcenter = new MctsSubcenter(mctsPhc,subcenterId,subcentreName);
						careDataRepository.saveOrUpdate(mctsSubcenter);
					}
							
					MctsVillage mctsVillage = careDataRepository.findEntityByField(MctsVillage.class, "villageId", villageId);
					if(mctsVillage == null) {
						mctsVillage = new MctsVillage(mctsTaluk,mctsSubcenter,villageId,villageName);
						careDataRepository.saveOrUpdate(mctsVillage);
					}
		
			}
		} 
		finally {
				if (beanReader != null) {
					beanReader.close();
					saveLocationData(file);
				}
			}
		
		
	}
	
	
	/**
	 * Method to populate location master table
	 */
	public void saveLocationData(File file) throws Exception {
		
		ICsvBeanReader beanReader = null;
		//String filePath= "/home/aman/Downloads/location.csv";
		
		
		LocationDataCSV locationCSV = new LocationDataCSV();
		try{
		beanReader = new CsvBeanReader(new FileReader(file),
				CsvPreference.STANDARD_PREFERENCE);
		final String[] header = beanReader.getHeader(true);
		int count = 0;
		while((locationCSV = beanReader.read(LocationDataCSV.class, header))!=null) {
				
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
			
				MctsLocationMaster mctsLocationMaster = new MctsLocationMaster(stateId, state, disctrictId, disctrictName, talukId, talukaName, healthblockId, healthblockName, phcId, phcName, subcenterId, subcentreName, villageId, villageName, status, comments);
				careDataRepository.saveOrUpdate(mctsLocationMaster);
			}

		}
		
		finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	
		
	}
	
}
