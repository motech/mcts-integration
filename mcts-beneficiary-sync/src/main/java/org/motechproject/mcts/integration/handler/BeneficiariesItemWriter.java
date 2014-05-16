package org.motechproject.mcts.integration.handler;

import java.io.File;
import java.util.List;

import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.integration.service.CareDataService;
import org.springframework.batch.item.ItemWriter;

public class BeneficiariesItemWriter implements ItemWriter<BeneficiaryRequest>{
	
	    private CareDataService careDataService;
	   // private BeneficiarySyncSettings beneficiarySyncSettings;
	@Override
	public void write(List<? extends BeneficiaryRequest> beneficiaryRequest)
			throws Exception {
		//String outputXMLFileLocation = String.format("%s_%s.xml", beneficiarySyncSettings.getUpdateXmlOutputFileLocation(), DateTime.now());
		//String outputURLFileLocation = String.format("%s_%s.txt", beneficiarySyncSettings.getUpdateUrlOutputFileLocation(), DateTime.now());
		//GenerateBeneficiaryToSyncRequestFiles generateBeneficiaryToSyncXML = new GenerateBeneficiaryToSyncRequestFiles();
		try {
	        //	File xmlFile = new File(outputXMLFileLocation);
	        //	File updateRequestUrl = new File(outputURLFileLocation);
			//	generateBeneficiaryToSyncXML.writeBeneficiaryToXML(beneficiaryRequest, BeneficiaryRequest.class, xmlFile, updateRequestUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//careDataService.updateSyncedBeneficiaries();
	}

}
