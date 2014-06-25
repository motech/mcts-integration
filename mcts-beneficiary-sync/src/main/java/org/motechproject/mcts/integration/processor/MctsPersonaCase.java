package org.motechproject.mcts.integration.processor;

import java.util.Map;
import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherMatchStatus;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to handle mcts persona case received via commcareHQ
 * @author naveen
 *
 */
@Component
public class MctsPersonaCase implements CaseProcesssor {
	
	@Autowired
	CareDataService careDataService;
	private static final Logger logger = LoggerFactory.getLogger("MctsPersonaCase");

	/**
	 * method to process mcts persona case sent by commcareHQ
	 * @param caseEvent: CaseEvent object sent by commcareHQ
	 * @exception throws BeneficiaryException if mandatory fields are not there or database query fails
	 */
	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		
		logger.debug("Started processor to handle mcts persona case with caseId" + caseEvent.getCaseId());
		
		String mctsId = null;
		String mctsMatch = null;
		MctsPregnantMotherMatchStatus matchStatus = null;
		//get fields received from commcareHQ
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		
		//Based on caseId get the corresponding objects from database  
		MctsPregnantMother pregnantMotherCase = careDataService.findEntityByField(MctsPregnantMother.class, "caseId", caseEvent.getCaseId());
		MotherCase motherCase = careDataService.findEntityByField(MotherCase.class,"caseId", caseEvent.getCaseId());
		
		//mcts_match is mandatory field so if not present throw an exception and if present then get the corresponding object for MctsPregnantMotherMatchStatus
		if(fieldValues.containsKey("mcts_match")) {
			mctsMatch = fieldValues.get("mcts_match");
		    matchStatus = careDataService.findEntityByField(MctsPregnantMotherMatchStatus.class, "name", mctsMatch);
		}
		else {
			logger.error("Stopping MctsPersonaCase Processor: couldn't get field value of mcts_match");
			throw new BeneficiaryException(ApplicationErrors.KEY_FIELD_DOESNOT_EXIST,"mcts-match field doesn't exist");
		}
		
		//Throws exception if there is mcts_id mismatch for received case and existing case
		if(fieldValues.containsKey("mcts_id")) {
			mctsId = fieldValues.get("mcts_id");
			if(!mctsId.equals(pregnantMotherCase.getMctsId())) {
				throw new BeneficiaryException(ApplicationErrors.FIELD_MISMATCH,"mcts_id of received persona case and existing persona case doesn't match for given caseId");
			}
		}
		else {
			logger.info("mcts_id field is not present in received persona case");
		}
		
		//set mctsMatch status and MotherCase of MctsPregnantMother
		pregnantMotherCase.setMctsPregnantMotherMatchStatus(matchStatus);
		pregnantMotherCase.setMotherCase(motherCase);
		
		//save mctsPregnantMother in database
		careDataService.saveOrUpdate(pregnantMotherCase);
		
		logger.debug("successfully handled the persona case with case Id: "+caseEvent.getCaseId());
	}

}
