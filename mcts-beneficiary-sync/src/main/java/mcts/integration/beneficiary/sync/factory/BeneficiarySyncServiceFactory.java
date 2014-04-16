package mcts.integration.beneficiary.sync.factory;

import mcts.integration.beneficiary.sync.launcher.SyncType;
import mcts.integration.beneficiary.sync.service.BeneficiarySyncService;
import mcts.integration.beneficiary.sync.service.MCTSBeneficiarySyncService;
import mcts.integration.beneficiary.sync.service.MotechBeneficiarySyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class BeneficiarySyncServiceFactory {

    private Map<SyncType, BeneficiarySyncService> serviceMappings;

    @Autowired
    public BeneficiarySyncServiceFactory(MotechBeneficiarySyncService motechBeneficiarySyncService, MCTSBeneficiarySyncService MCTSBeneficiarySyncService) {
        serviceMappings = new HashMap<>();
        serviceMappings.put(SyncType.UPDATE, motechBeneficiarySyncService);
        serviceMappings.put(SyncType.GET, MCTSBeneficiarySyncService);
    }

    public BeneficiarySyncService getBeneficiarySyncService(SyncType syncType) {
        return serviceMappings.get(syncType);
    }
}
