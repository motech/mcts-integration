package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.hibernate.model.MotherCaseMctsAuthorizedStatus;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotherCaseProcessor implements CaseProcesssor{

	@Autowired
	CareDataService careDataService;
	private static final Logger logger = LoggerFactory.getLogger("MotherCaseProcessor");
	
	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		
		logger.debug("Started processor to handle mother case with caseId" + caseEvent.getCaseId());
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		String authorized = fieldValues.get("authorized");
		String fullMctsId = fieldValues.get("full_mcts_id");
		MotherCaseMctsAuthorizedStatus authorizeStatus = careDataService.findEntityByField(MotherCaseMctsAuthorizedStatus.class, "name", authorized);
		String mctsId = fieldValues.get("mcts_id");
		MctsPregnantMother pregnantMotherCase = careDataService.findEntityByField(MctsPregnantMother.class, "mctsId", mctsId);
		pregnantMotherCase.setMotherCaseMctsAuthorizedStatus(authorizeStatus);
		MotherCase motherCase = careDataService.findEntityByField(MotherCase.class,"caseId", caseEvent.getCaseId());
		pregnantMotherCase.setMotherCase(motherCase);
		careDataService.saveOrUpdate(pregnantMotherCase);
	
	}

}
