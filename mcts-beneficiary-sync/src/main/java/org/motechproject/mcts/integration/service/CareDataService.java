package org.motechproject.mcts.integration.service;


import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherServiceUpdate;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CareDataService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataService.class);

    @Autowired
    private CareDataRepository careDataRepository;

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        return careDataRepository.getBeneficiariesToSync(startDate, endDate);
    }

    public void mapMotherCaseToMctsPregnantMother(String caseId, String mctsId) {
        MotherCase motherCase = careDataRepository.findEntityByField(MotherCase.class, "caseId", caseId);
        if (motherCase == null) {
            LOGGER.info(String.format("MCTS Pregnant Mother not updated. Mother case not found for Case Id: %s", caseId));
            return;
        }

        MctsPregnantMother MctsPregnantMother = getExistingOrNewMctsPregnantMother(mctsId, motherCase);
        careDataRepository.saveOrUpdate(MctsPregnantMother);
    }

    private MctsPregnantMother getExistingOrNewMctsPregnantMother(String mctsId, MotherCase motherCase) {
        MctsPregnantMother MctsPregnantMother = careDataRepository.findEntityByField(MctsPregnantMother.class, "motherCase", motherCase);

        if (MctsPregnantMother != null) {
            LOGGER.info(String.format("MCTS Pregnant Mother already exists with MCTS Id: %s for Mother Case: %s. Updating it with new MCTS Id: %s.", MctsPregnantMother.getMctsId(), motherCase.getCaseId(), mctsId));
            MctsPregnantMother.setMctsId(mctsId);
        } else {
            LOGGER.info(String.format("Creating MCTS Pregnant Mother with MCTS Id: %s for Mother Case: %s.", mctsId, motherCase.getCaseId()));
            MctsPregnantMother = new MctsPregnantMother();
            MctsPregnantMother.setMctsId(mctsId);
            MctsPregnantMother.setCaseId(motherCase.getId());
       }
        return MctsPregnantMother;
    }

    public void updateSyncedBeneficiaries(List<Beneficiary> syncedBeneficiaries) {
        LOGGER.info(String.format("Updating %s beneficiaries as updated to MCTS", syncedBeneficiaries.size()));
        DateTime serviceUpdateTime = DateTime.now();
        for (Beneficiary syncedBeneficiary : syncedBeneficiaries) {
            MctsPregnantMother MctsPregnantMother = careDataRepository.load(MctsPregnantMother.class, syncedBeneficiary.getMctsPregnantMotherId());
            MctsPregnantMotherServiceUpdate mctsPregnantMotherServiceUpdate =
                    new MctsPregnantMotherServiceUpdate();
            mctsPregnantMotherServiceUpdate.setMctsPregnantMother(MctsPregnantMother);
            mctsPregnantMotherServiceUpdate.setServiceDeliveryDate(syncedBeneficiary.getServiceDeliveryDate());
            mctsPregnantMotherServiceUpdate.setServiceType(Short.valueOf(syncedBeneficiary.getServiceType().toString()));
            mctsPregnantMotherServiceUpdate.setServiceUpdateTime(new Timestamp(serviceUpdateTime.getMillis()));
            
            careDataRepository.saveOrUpdate(mctsPregnantMotherServiceUpdate);
        }
    }
}
