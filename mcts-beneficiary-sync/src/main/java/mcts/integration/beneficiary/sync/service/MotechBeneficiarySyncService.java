package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.domain.Beneficiary;
import mcts.integration.beneficiary.sync.request.BeneficiaryDetails;
import mcts.integration.beneficiary.sync.request.BeneficiaryRequest;
import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MotechBeneficiarySyncService implements BeneficiarySyncService {
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
        BeneficiaryRequest beneficiaryRequest = mapToBeneficiaryRequest(beneficiariesToSync);
        mctsHttpClientService.syncTo(beneficiaryRequest);
    }

    private BeneficiaryRequest mapToBeneficiaryRequest(List<Beneficiary> beneficiariesToSync) {
        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        Integer stateId = beneficiarySyncSettings.getStateId();
        for (Beneficiary beneficiary : beneficiariesToSync) {
            beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(stateId, beneficiary.getMctsId(), beneficiary.getServiceType()));

        }
        return beneficiaryRequest;
    }
}
