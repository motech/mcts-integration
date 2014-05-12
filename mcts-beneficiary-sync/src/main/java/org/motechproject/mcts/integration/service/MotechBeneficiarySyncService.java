package org.motechproject.mcts.integration.service;

import java.io.File;
import java.util.List;

import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.GenerateBeneficiaryToSyncRequestFiles;
import org.motechproject.mcts.utils.PropertyReader;

import motech.care.data.domain.Beneficiary;
import motech.care.data.service.CareDataService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotechBeneficiarySyncService implements BeneficiarySyncService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MotechBeneficiarySyncService.class);

    private CareDataService careDataService;
    private MCTSHttpClientService mctsHttpClientService;
    private PropertyReader beneficiarySyncSettings;
    private BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
    private String outputXMLFileLocation;
    private Publisher publisher = new Publisher();

    @Autowired
    public MotechBeneficiarySyncService(CareDataService careDataService, MCTSHttpClientService mctsHttpClientService, PropertyReader beneficiarySyncSettings) {
        this.careDataService = careDataService;
        this.mctsHttpClientService = mctsHttpClientService;
        this.beneficiarySyncSettings = beneficiarySyncSettings;
    }

    public void syncBeneficiaryData(DateTime startDate, DateTime endDate) {
        List<Beneficiary> beneficiariesToSync = careDataService.getBeneficiariesToSync(startDate, endDate);
        if(beneficiariesToSync.isEmpty()) {
            LOGGER.info("No records found to sync. Not sending service update request to MCTS");
            return;
        }

        LOGGER.info(String.format("Found %s beneficiary records to sync to MCTS", beneficiariesToSync.size()));
        this.beneficiaryRequest = mapToBeneficiaryRequest(beneficiariesToSync);
        mctsHttpClientService.syncTo(beneficiaryRequest);
        outputXMLFileLocation = String.format("%s_%s.xml", beneficiarySyncSettings.getUpdateXmlOutputFileLocation(), DateTime.now().toString("yyyy-MM-dd") + "T" + DateTime.now().toString("HH:mm"));
        String outputURLFileLocation = String.format("%s_%s.txt", beneficiarySyncSettings.getUpdateUrlOutputFileLocation(), DateTime.now().toString("yyyy-MM-dd") + "T" + DateTime.now().toString("HH:mm"));
        GenerateBeneficiaryToSyncRequestFiles generateBeneficiaryToSyncXML = new GenerateBeneficiaryToSyncRequestFiles();
        try {
        	File xmlFile = new File(outputXMLFileLocation);
        	File updateRequestUrl = new File(outputURLFileLocation);
			generateBeneficiaryToSyncXML.writeBeneficiaryToXML(beneficiaryRequest, BeneficiaryRequest.class, xmlFile, updateRequestUrl);
		} catch (Exception e) {
			LOGGER.error("File Not Found");
			//throw new Exception(e);
		}
        LOGGER.info("Notifying Hub to Publish the Updates at url" + getHubSyncToUrl());
        	publisher.publish(getHubSyncToUrl(), beneficiaryRequest.toString());
        careDataService.updateSyncedBeneficiaries(beneficiariesToSync);
    }

    private BeneficiaryRequest mapToBeneficiaryRequest(List<Beneficiary> beneficiariesToSync) {
    	BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        Integer stateId = beneficiarySyncSettings.getStateId();
        for (Beneficiary beneficiary : beneficiariesToSync) {
            beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(stateId, 
            	beneficiary.getMctsId(),
            	beneficiary.getServiceType(),
            	beneficiary.getServiceDeliveryDate(),
            	beneficiary.getMobileNumber(),
            	beneficiary.getHbLevelStr()));
        }
        return beneficiaryRequest;
    }
    
    public String getHubSyncToUrl(){
    	return beneficiarySyncSettings.getHubSyncToUrl() + outputXMLFileLocation;
    }
}
