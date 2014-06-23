package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMatchStatus;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.hibernate.model.MotherCaseMctsAuthorizedStatus;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MctsPersonaCase implements CaseProcesssor {
	
	@Autowired
	CareDataService careDataService;
	private static final Logger logger = LoggerFactory.getLogger("MctsPersonaCase");

	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		// TODO Auto-generated method stub
		logger.debug("Started processor to handle mcts persona case with caseId" + caseEvent.getCaseId());
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		String mctsMatch = fieldValues.get("mcts_match");
		String fullMctsId = fieldValues.get("full_mcts_id");
		MctsPregnantMotherMatchStatus matchStatus = careDataService.findEntityByField(MctsPregnantMotherMatchStatus.class, "name", mctsMatch);
		String mctsId = fieldValues.get("mcts_id");
		MctsPregnantMother pregnantMotherCase = careDataService.findEntityByField(MctsPregnantMother.class, "mctsId", mctsId);
		pregnantMotherCase.setMctsPregnantMotherMatchStatus(matchStatus);
		//pregnantMotherCase.set
		MotherCase motherCase = careDataService.findEntityByField(MotherCase.class,"caseId", caseEvent.getCaseId());
		pregnantMotherCase.setMotherCase(motherCase);
		careDataService.saveOrUpdate(pregnantMotherCase);
	}

}
