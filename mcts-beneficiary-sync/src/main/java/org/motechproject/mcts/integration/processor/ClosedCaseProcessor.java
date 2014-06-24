package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClosedCaseProcessor implements CaseProcesssor {

	@Autowired
	CareDataService careDataService;
	private static final Logger logger = LoggerFactory.getLogger("ClosedCaseProcessor");
	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		
		logger.debug("Started sevice to handle closed case with caseId: "+caseEvent.getCaseId());
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		String hhNumber = fieldValues.get("hhNumber");
		String familyNumber = fieldValues.get("familyNumber");
		String mctsId = fieldValues.get("mctsId");
		MctsPregnantMother pregnantMotherCase = careDataService.findEntityByField(MctsPregnantMother.class, "mctsId", mctsId);
		pregnantMotherCase.setHhNumber(hhNumber);
		pregnantMotherCase.setFamilyNumber(familyNumber);
		careDataService.saveOrUpdate(pregnantMotherCase);
		//TODO call Aman function
	}

}
