package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.hibernate.model.MotherCaseMctsAuthorizedStatusLookup;
import org.motechproject.mcts.integration.service.CareDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovedFormProcessor implements FormProcessor {

    @Autowired
    private CareDataService careDataService;

    @Override
    public void process(Map<String, String> motherForm) {
        MotherCase motherCase = careDataService.findEntityByField(
                MotherCase.class, "caseId", motherForm.get("caseId"));
        MctsPregnantMother mctsPregnantMother = careDataService
                .getMctsPregnantMotherFromCaseId(Integer.toString(motherCase
                        .getId()));
        MotherCaseMctsAuthorizedStatusLookup authorizeStatus = careDataService
                .findEntityByField(MotherCaseMctsAuthorizedStatusLookup.class,
                        "name", motherForm.get("authorized"));
        mctsPregnantMother.setMotherCaseMctsAuthorizedStatus(authorizeStatus);

        careDataService.saveOrUpdate(mctsPregnantMother);
    }

}
