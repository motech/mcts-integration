package org.motechproject.mcts.integration.processor;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.mcts.integration.exception.BeneficiaryException;

public interface CaseProcesssor {
	
	public void process(CaseEvent caseEvent) throws BeneficiaryException;

}
