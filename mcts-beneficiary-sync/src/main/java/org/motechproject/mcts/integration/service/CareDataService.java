package org.motechproject.mcts.integration.service;

import motech.care.data.domain.Beneficiary;
import motech.care.data.domain.MCTSPregnantMother;
import motech.care.data.domain.MCTSPregnantMotherServiceUpdate;
import motech.care.data.domain.MotherCase;
import motech.care.data.repository.CareDataRepository;
import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class CareDataService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataService.class);

    private CareDataRepository careDataRepository;

    public CareDataService() {
    }

    @Autowired
    public CareDataService(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        return careDataRepository.getBeneficiariesToSync(startDate, endDate);
    }

    public void mapMotherCaseToMCTSPregnantMother(String caseId, String mctsId) {
        MotherCase motherCase = careDataRepository.findEntityByField(MotherCase.class, "caseId", caseId);
        if (motherCase == null) {
            LOGGER.info(String.format("MCTS Pregnant Mother not updated. Mother case not found for Case Id: %s", caseId));
            return;
        }

        MCTSPregnantMother mctsPregnantMother = getExistingOrNewMCTSPregnantMother(mctsId, motherCase);
        careDataRepository.saveOrUpdate(mctsPregnantMother);
    }

    private MCTSPregnantMother getExistingOrNewMCTSPregnantMother(String mctsId, MotherCase motherCase) {
        MCTSPregnantMother mctsPregnantMother = careDataRepository.findEntityByField(MCTSPregnantMother.class, "motherCase", motherCase);

        if (mctsPregnantMother != null) {
            LOGGER.info(String.format("MCTS Pregnant Mother already exists with MCTS Id: %s for Mother Case: %s. Updating it with new MCTS Id: %s.", mctsPregnantMother.getMctsId(), motherCase.getCaseId(), mctsId));
            mctsPregnantMother.updateMctsId(mctsId);
        } else {
            LOGGER.info(String.format("Creating MCTS Pregnant Mother with MCTS Id: %s for Mother Case: %s.", mctsId, motherCase.getCaseId()));
            mctsPregnantMother = new MCTSPregnantMother(mctsId, motherCase);
        }
        return mctsPregnantMother;
    }

    public void updateSyncedBeneficiaries(List<Beneficiary> syncedBeneficiaries) {
        LOGGER.info(String.format("Updating %s beneficiaries as updated to MCTS", syncedBeneficiaries.size()));
        DateTime serviceUpdateTime = DateTime.now();
        for (Beneficiary syncedBeneficiary : syncedBeneficiaries) {
            MCTSPregnantMother mctsPregnantMother = careDataRepository.load(MCTSPregnantMother.class, syncedBeneficiary.getMctsPregnantMotherId());
            MCTSPregnantMotherServiceUpdate mctsPregnantMotherServiceUpdate =
                    new MCTSPregnantMotherServiceUpdate(mctsPregnantMother, syncedBeneficiary.getServiceType(), syncedBeneficiary.getServiceDeliveryDate(), new Timestamp(serviceUpdateTime.getMillis()));
            careDataRepository.saveOrUpdate(mctsPregnantMotherServiceUpdate);
        }
    }
}
