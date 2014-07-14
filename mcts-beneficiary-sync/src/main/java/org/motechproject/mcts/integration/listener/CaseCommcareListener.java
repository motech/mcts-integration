package org.motechproject.mcts.integration.listener;

import static java.lang.String.format;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.processor.ClosedCaseProcessor;
import org.motechproject.mcts.integration.processor.MctsPersonaCase;
import org.motechproject.mcts.integration.processor.MotherCaseProcessor;
import org.motechproject.mcts.utils.CaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseCommcareListener {
	private MotherCaseProcessor motherCaseProcessor;
	private MctsPersonaCase mctsPersonaCase;
	private ClosedCaseProcessor closedCaseProcessor;
	 private static final Logger logger = LoggerFactory.getLogger(CaseCommcareListener.class);
	 public static final String CLOSE_ACTION_IDENTIFIER = "CLOSE";
	
	    @Autowired
	    public CaseCommcareListener(MotherCaseProcessor motherCaseProcessor, MctsPersonaCase mctsPersonaCase, ClosedCaseProcessor closedCaseProcessor) {
	    
	    	this.motherCaseProcessor = motherCaseProcessor;
	    	this.mctsPersonaCase = mctsPersonaCase;
	    	this.closedCaseProcessor = closedCaseProcessor;
	    }
	    
	    @MotechListener(subjects = EventSubjects.CASE_EVENT)
	    public void handleEvent(MotechEvent event) throws BeneficiaryException {
	    	CaseEvent caseEvent = new CaseEvent(event);
	    	String caseId = caseEvent.getCaseId();
	        String action = caseEvent.getAction();
	        String caseName = caseEvent.getCaseName();
	        System.out.println("caseEvent.getFieldValues().toString()"+caseEvent.getFieldValues().toString());
	        System.out.println("case event received :"+caseEvent.getCaseType()+"----"+caseEvent.getFieldValues().get("mcts_id")+"----"+caseEvent.getFieldValues());
	        logger.info(format("Received case. id: %s, case name: %s; action: %s;", caseId, caseName, action));
	        if (CLOSE_ACTION_IDENTIFIER.equals(action)) {
	            processClose(caseEvent);
	            return;
	        }
	        createUpdate(caseEvent,caseId);
	        
	        
	    }

		private void processClose(CaseEvent caseEvent) throws BeneficiaryException {
			
			
			closedCaseProcessor.process(caseEvent);
		}

		private void createUpdate(CaseEvent caseEvent, String caseId) throws BeneficiaryException {
			CaseType caseType = CaseType.getType(caseEvent.getCaseType());
			
			if (caseType.equals(CaseType.MOTHER)) {
	            motherCaseProcessor.process(caseEvent);
	            return;
	        }

	        if (caseType.equals(CaseType.PERSONA)) {
	        	mctsPersonaCase.process(caseEvent);
	            return;
	        }
			
		}
}
