package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.integration.exception.BeneficiaryException;

public interface FormProcessor {

	public void process(Map<String, String> motherInfo) throws BeneficiaryException;

}
