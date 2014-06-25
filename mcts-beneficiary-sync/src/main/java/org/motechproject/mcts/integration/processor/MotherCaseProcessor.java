package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.hibernate.model.MotherCaseMctsAuthorizedStatus;
import org.motechproject.mcts.integration.hibernate.model.MotherCaseMctsUpdate;
import org.motechproject.mcts.integration.service.CareDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Class to handle mother cases sent from commcareHQ  
 * @author naveen
 *
 */
@Component
public class MotherCaseProcessor implements CaseProcesssor{

	@Autowired
	CareDataService careDataService;
	private static final Logger logger = LoggerFactory.getLogger("MotherCaseProcessor");
	
	/**
	 * method to process mother case sent by commcareHQ
	 * @param caseEvent: CaseEvent object sent by commcareHQ
	 * @exception throws BeneficiaryException if mandatory fields are not there or database query fails
	 */
	@Override
	public void process(CaseEvent caseEvent) throws BeneficiaryException {
		
		logger.debug("Started processor to handle mother case with caseId" + caseEvent.getCaseId());
		
		String authorized = null;
		String mctsId = null;
		MotherCaseMctsAuthorizedStatus authorizeStatus = null;
		
		//get fields received from commcareHQ
		Map<String , String> fieldValues = caseEvent.getFieldValues();
		
		//Based on caseId get the corresponding objects from database  
		MctsPregnantMother mctsPregnantMother = careDataService.findEntityByField(MctsPregnantMother.class, "caseId", caseEvent.getCaseId());
		MotherCaseMctsUpdate motherCaseMctsUpdate = careDataService.findEntityByField(MotherCaseMctsUpdate.class, "caseId", caseEvent.getCaseId());
		MotherCase motherCase = careDataService.findEntityByField(MotherCase.class,"caseId", caseEvent.getCaseId());
		
		if(motherCaseMctsUpdate == null) {
			motherCaseMctsUpdate = new MotherCaseMctsUpdate();
		}
		
		//authorize is mandatory field so if not present throw an exception
		if(fieldValues.containsKey("authorized")) {
		    authorized = fieldValues.get("authorized");
		    authorizeStatus = careDataService.findEntityByField(MotherCaseMctsAuthorizedStatus.class, "name", authorized);
		}
		else {
			logger.error("Stopping MotherCase Processor: couldn't get field value of authorize");
			throw new BeneficiaryException(ApplicationErrors.KEY_FIELD_DOESNOT_EXIST,"authorized field doesn't exist");
		}
		
		//set mcts_id for motherCaseMctsUpdate and throws exception if there is mcts_id mismatch for received case and existing case
		if(fieldValues.containsKey("mcts_id")) {	
		    mctsId = fieldValues.get("mcts_id");
		    motherCaseMctsUpdate.setMctsId(mctsId);
		    if(!mctsId.equals(mctsPregnantMother.getMctsId())) {
				throw new BeneficiaryException(ApplicationErrors.FIELD_MISMATCH,"mcts_id of received mother case and existing mother case doesn't match for given caseId");
			}
		}
		
		//set fields of MotherCaseMctsUpdate
		motherCaseMctsUpdate.setMctsPregnantMother(mctsPregnantMother);
		motherCaseMctsUpdate.setMotherCase(motherCase);
		motherCaseMctsUpdate.setMotherCaseMctsAuthorizedStatus(authorizeStatus);
		
		//set authorized status and MotherCase of MctsPregnantMother
		mctsPregnantMother.setMotherCaseMctsAuthorizedStatus(authorizeStatus);
		mctsPregnantMother.setMotherCase(motherCase);
		
		//save mctsPregnantMother and motherCaseMctsUpdate in database
		careDataService.saveOrUpdate(mctsPregnantMother);
		careDataService.saveOrUpdate(motherCaseMctsUpdate);
		
		logger.info("successfully handled the case with case Id: "+caseEvent.getCaseId());
	
	}

}
