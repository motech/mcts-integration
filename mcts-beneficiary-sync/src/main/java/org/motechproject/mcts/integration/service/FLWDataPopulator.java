package org.motechproject.mcts.integration.service;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.motechproject.mcts.integration.hibernate.model.FlwData;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

@Transactional
@Repository
@ContextConfiguration(locations={"classpath:*Context.xml"})
@TransactionConfiguration(transactionManager="txManager", defaultRollback=true)
public class FLWDataPopulator {
	
	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
    private CareDataRepository careDataRepository;
	
	public void populateFLWData() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader("/home/aman/Downloads/FLW.csv"), CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);
			FLWDataCSV flwDataCSV = new FLWDataCSV();
			 while( (flwDataCSV = beanReader.read(FLWDataCSV.class, header)) != null ) {
				 FlwData flwData = new FlwData((int)careDataRepository.getNextKey(),flwDataCSV.getId(), flwDataCSV.getDistrict_ID(), flwDataCSV.getTaluka_ID(), flwDataCSV.getHealthBlock_ID(), flwDataCSV.getSubCentre_ID(), flwDataCSV.getVillage_ID(), flwDataCSV.getName(), flwDataCSV.getSex(), flwDataCSV.getType(), flwDataCSV.getAadhar_No(), flwDataCSV.getHusband_Name(), flwDataCSV.getGF_Address(),flwDataCSV.getContact_No(),flwDataCSV.getPHC_ID());
				 careDataRepository.saveOrUpdate(flwData);

			 }
		}
		
		finally {
            if( beanReader != null ) {
                    beanReader.close();
            }
	}

}
}


