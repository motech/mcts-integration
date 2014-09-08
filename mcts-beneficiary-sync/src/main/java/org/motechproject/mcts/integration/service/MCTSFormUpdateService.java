package org.motechproject.mcts.integration.service;

import java.util.List;

import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherCaseAuthorisedStatus;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherMatchStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Matches the mctsIDs of MCTS Pregnant Mother with mcts Ids of
 * AwwRegisterMotherForm and MotherEditForm and updates the caseIds, match
 * status and authorised status
 *
 * @author aman
 *
 */
@Transactional
@Service
public class MCTSFormUpdateService {


    private static final Logger LOGGER = LoggerFactory
            .getLogger(MCTSFormUpdateService.class);

    @Autowired
    private CareDataRepository careDataRepository;

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }
/*
    *//**
     * Matches the mctsIDs of MCTS Pregnant Mother with mcts Ids of
     * AwwRegisterMotherForm and updates the caseIds, match status and
     * authorised status
     *//*
    public void updateMCTSStatusesfromRegForm() {

        List<Object[]> caseIdmctsId = careDataRepository.getmctsIdcaseId();
        for (int i = 0; i < caseIdmctsId.size(); i++) {
            LOGGER.info((String) caseIdmctsId.get(i)[1]);
            int caseId = (int) caseIdmctsId.get(i)[0];
            String mctsId = (String) caseIdmctsId.get(i)[1];

            String query = "update MctsPregnantMother m set m.motherCase.id = '"
                    + caseId
                    + "', m.mctsPregnantMotherMatchStatus.id ='"
                    + MCTSPregnantMotherMatchStatus.YES.getId()
                    + "', m.motherCaseMctsAuthorizedStatus.id='"
                    + MCTSPregnantMotherCaseAuthorisedStatus.PENDING.getId()
                    + "' where m.mctsId='" + mctsId + "'";

            careDataRepository.updateQuery(query);
            updateMCTSStatusfromEditForm();

        }

    }

    *//**
     * Matches the mctsIDs of MCTS Pregnant Mother with mcts Ids of
     * MotherEditForm and updates the caseIds, match status and authorised
     * status. In case mctsID is matched with both AwwRegisterMotherForm and
     * MotherEditForm data from MotherEditForm will take the preference
     *//*
    public void updateMCTSStatusfromEditForm() {
        List<Object[]> caseIdmctsId = careDataRepository
                .getmctsIdcaseIdfromEditForm();
        for (int i = 0; i < caseIdmctsId.size(); i++) {
            int caseId = (int) caseIdmctsId.get(i)[0];
            String mctsId = (String) caseIdmctsId.get(i)[1];

            String query = "update MctsPregnantMother m set m.motherCase.id = '"
                    + caseId
                    + "', m.mctsPregnantMotherMatchStatus.id ='"
                    + MCTSPregnantMotherMatchStatus.YES.getId()
                    + "', m.motherCaseMctsAuthorizedStatus.id='"
                    + MCTSPregnantMotherCaseAuthorisedStatus.PENDING.getId()
                    + "' where m.mctsId='" + mctsId + "'";

            careDataRepository.updateQuery(query);

        }

    }*/

    public void updateMctsPregnantMotherForm(int primaryId) {
        MctsPregnantMother mother = careDataRepository
                .getMotherFromPrimaryId(primaryId);
        if (mother != null) {
            String hhNumber = mother.getHhNumber();
            String familyNumber = mother.getFamilyNumber();
            if ((hhNumber != null) && (familyNumber != null)) {
                Integer hhNum = IntegerValidator.validateAndReturnAsInt("hhNumber", hhNumber); 
                Integer familyNum = IntegerValidator.validateAndReturnAsInt("familyNumber", familyNumber);
                String ownerId = mother.getOwnerId();
                MotherCase motherCase = careDataRepository
                        .matchMctsPersonawithMotherCase(hhNum, familyNum, ownerId);
                if (motherCase != null) {
                    mother.setMotherCase(motherCase);
                    careDataRepository.saveOrUpdate(mother);
                }
            }  else {
                LOGGER.info("empty fields : either hhNumber or familyNumber is empty");
            }

        }
    }
}
