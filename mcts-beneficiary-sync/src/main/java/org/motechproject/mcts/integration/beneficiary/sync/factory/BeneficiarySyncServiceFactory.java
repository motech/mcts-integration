package org.motechproject.mcts.integration.beneficiary.sync.factory;

import java.util.HashMap;
import java.util.Map;

import org.motechproject.mcts.integration.constants.SyncType;
import org.motechproject.mcts.integration.service.BeneficiarySyncService;
import org.motechproject.mcts.integration.service.MCTSBeneficiarySyncService;
import org.motechproject.mcts.integration.service.MotechBeneficiarySyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class BeneficiarySyncServiceFactory {

    private Map<SyncType, BeneficiarySyncService> serviceMappings;

    private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncServiceFactory.class);


    @Autowired
    public BeneficiarySyncServiceFactory(MotechBeneficiarySyncService motechBeneficiarySyncService, MCTSBeneficiarySyncService MCTSBeneficiarySyncService) {
        serviceMappings = new HashMap<>();
        serviceMappings.put(SyncType.UPDATE, motechBeneficiarySyncService);
        serviceMappings.put(SyncType.GET, MCTSBeneficiarySyncService);
        LOGGER.info("BeneficiarySyncServiceFactory Bean initialized");
    }

    public BeneficiarySyncService getBeneficiarySyncService(SyncType syncType) {
        return serviceMappings.get(syncType);
    }
}
