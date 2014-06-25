package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Class to handle closed cases sent from commcareHQ
 * @author naveen
 *
 */
@Component
public class ClosedCaseProcessor implements CaseProcesssor {

	@Autowired
	CareDataService careDataService;
	@Autowired
	MCTSFormUpdateService mctsFormUpdateService;
	private static final Logger logger = LoggerFactory.getLogger("ClosedCaseProcessor");
	
	/**
	 * method to process closed case sent by commcareHQ
	 * @param caseEvent: CaseEvent object sent by commcareHQ
	 * @exception BeneficiaryException if mandatory fields are not there or database query fails
	 */
	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		
		String hhNumber=null;
		String familyNumber=null;
		String mctsId = null;
		logger.debug("Started sevice to handle closed case with caseId: "+caseEvent.getCaseId());
		
		//get fields received from commcareHQ
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		
		//Based on caseId get the corresponding objects from database 
		MctsPregnantMother pregnantMotherCase = careDataService.findEntityByField(MctsPregnantMother.class, "mctsId", mctsId);
		
		//set hhNumber and familyNumber in MctsPregnantMother if not present throw an exception as they are mandatory fields
		if(fieldValues.containsKey("hh_number") && fieldValues.containsKey("family_number")) {
			hhNumber = fieldValues.get("hh_number");
			familyNumber = fieldValues.get("family_number");
			pregnantMotherCase.setHhNumber(hhNumber);
			pregnantMotherCase.setFamilyNumber(familyNumber);
		}
		else {
			throw new BeneficiaryException(ApplicationErrors.KEY_FIELD_DOESNOT_EXIST,"Either hh_number or family_number doesn't exist");
		}
		
		//set mcts_id for motherCaseMctsUpdate and throws exception if there is mcts_id mismatch for received case and existing case
		if(fieldValues.containsKey("mcts_id")) {
			mctsId = fieldValues.get("mcts_id");
			if(!mctsId.equals(pregnantMotherCase.getMctsId())) {
				throw new BeneficiaryException(ApplicationErrors.FIELD_MISMATCH,"mcts_id of received persona case and existing persona case doesn't match for given caseId");
			}
		}
		
		//save mctsPregnantMother and motherCaseMctsUpdate in database
		careDataService.saveOrUpdate(pregnantMotherCase);
		
		//Call to method to update caseid(from commcare case) for closed case in mcts pregnant mother 
		mctsFormUpdateService.updateMctsPregnantMotherForm(pregnantMotherCase.getId());
		logger.info("successfully handled the closed case with case Id: "+caseEvent.getCaseId()+" hhNumber"+hhNumber+" and family number"+familyNumber);
	}

}
