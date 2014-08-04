package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMatchStatusLookup;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClosedFormProcessor implements FormProcessor {

    @Autowired
    private CareDataService careDataService;
    @Autowired
    private MCTSFormUpdateService mctsFormUpdateService;
    private static final Logger LOGGER = LoggerFactory
            .getLogger("ClosedCaseProcessor");

    @Override
    public void process(Map<String, String> motherForm)
            throws BeneficiaryException {
        MctsPregnantMother mctsPregnantMother = (MctsPregnantMother) careDataService
                .findEntityByField(MctsPregnantMother.class,
                        "mctsPersonaCaseUId", motherForm.get("caseId"));
        MctsPregnantMotherMatchStatusLookup matchStatus = careDataService
                .findEntityByField(MctsPregnantMotherMatchStatusLookup.class,
                        "name", motherForm.get("mctsMatch"));
        mctsPregnantMother.setHhNumber(motherForm.get("hhNumber"));
        mctsPregnantMother.setFamilyNumber(motherForm.get("familyNumber"));
        mctsPregnantMother.setMctsPregnantMotherMatchStatus(matchStatus);
        careDataService.saveOrUpdate(mctsPregnantMother);
        mctsFormUpdateService.updateMctsPregnantMotherForm(mctsPregnantMother
                .getId());
        LOGGER.info("successfully handled the closed case with case Id: "
                + motherForm.get("caseId") + " hhNumber"
                + motherForm.get("hhNumber") + " and family number"
                + motherForm.get("familyNumber"));

    }

}
