package mcts.integration.beneficiary.sync.service;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import mcts.integration.beneficiary.sync.request.BeneficiaryDetails;
import mcts.integration.beneficiary.sync.request.BeneficiaryRequest;
import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import mcts.integration.beneficiary.sync.util.GenerateBeneficiaryToSyncRequestFiles;
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
    private BeneficiarySyncSettings beneficiarySyncSettings;

    @Autowired
    public MotechBeneficiarySyncService(CareDataService careDataService, MCTSHttpClientService mctsHttpClientService, BeneficiarySyncSettings beneficiarySyncSettings) {
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
        BeneficiaryRequest beneficiaryRequest = mapToBeneficiaryRequest(beneficiariesToSync);
        mctsHttpClientService.syncTo(beneficiaryRequest);
        GenerateBeneficiaryToSyncRequestFiles generateBeneficiaryToSyncXML = new GenerateBeneficiaryToSyncRequestFiles();
        try {
        	File xmlFile = new File(getClass().getClassLoader().getResource("UpdateRequest.xml").toURI());
        	File updateRequestUrl = new File(getClass().getClassLoader().getResource("UpdateRequestUrl.txt").toURI());
			generateBeneficiaryToSyncXML.writeBeneficiaryToXML(beneficiaryRequest, BeneficiaryRequest.class, xmlFile, updateRequestUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
