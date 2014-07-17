package org.motechproject.mcts.integration.parser;

import java.util.Map;

import org.motechproject.commcare.domain.FormValueElement;

public interface CaseInfoParser {
	
	 Map<String, String> parse(FormValueElement element);
	 FormValueElement getCaseElement(FormValueElement startElement);
	 Map<String, String> parse(FormValueElement element, boolean isRecursive);

}
