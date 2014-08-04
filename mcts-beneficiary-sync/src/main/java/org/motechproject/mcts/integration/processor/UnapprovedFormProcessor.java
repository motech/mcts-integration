package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMatchStatusLookup;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnapprovedFormProcessor implements FormProcessor {

    @Autowired
    private CareDataService careDataService;
    private static final Logger LOGGER = LoggerFactory
            .getLogger("UnapprovedFormProcessor");

    @Override
    public void process(Map<String, String> motherInfo) {
        MctsPregnantMother mctsPregnantMother = (MctsPregnantMother) careDataService
                .findEntityByField(MctsPregnantMother.class,
                        "mctsPersonaCaseUId", motherInfo.get("caseId"));

        MotherCase motherCase = careDataService.findEntityByField(
                MotherCase.class, "caseId", motherInfo.get("pregnancyId"));
        if (motherCase == null) {
            LOGGER.error(String
                    .format("Received case doesn't have Mother case with with case Id = %s",
                            motherInfo.get("pregnancyId")));
            return;
        }
        MctsPregnantMotherMatchStatusLookup matchStatus = careDataService
                .findEntityByField(MctsPregnantMotherMatchStatusLookup.class,
                        "name", motherInfo.get("mctsMatch"));
        mctsPregnantMother.setMctsPregnantMotherMatchStatus(matchStatus);
        mctsPregnantMother.setMotherCase(motherCase);

        careDataService.saveOrUpdate(mctsPregnantMother);

    }

}
