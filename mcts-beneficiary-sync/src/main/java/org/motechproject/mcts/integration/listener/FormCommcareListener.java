package org.motechproject.mcts.integration.listener;

import org.motechproject.commcare.builder.CommcareFormBuilder;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.processor.MotherFormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormCommcareListener {
	@Autowired
	MotherFormProcessor motherFormProcessor;
	
	 private static final Logger logger = LoggerFactory
	         .getLogger("mcts-forms-processor");
	 
	 @MotechListener(subjects = EventSubjects.FORMS_EVENT)
	public void handleEvent(MotechEvent motechEvent) throws BeneficiaryException {
		 CommcareForm form = new CommcareFormBuilder().buildFrom(motechEvent);
		 logger.info("Request to process commcareForm started");
		 motherFormProcessor.process(form);
	}
}
