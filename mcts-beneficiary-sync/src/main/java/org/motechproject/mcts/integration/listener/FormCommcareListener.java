package org.motechproject.mcts.integration.listener;

import org.motechproject.commcare.builder.CommcareFormBuilder;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.processor.MotherFormProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormCommcareListener {
	@Autowired
	MotherFormProcessor motherFormProcessor;
	 @MotechListener(subjects = EventSubjects.FORMS_EVENT)
	public void handleEvent(MotechEvent motechEvent) throws BeneficiaryException {
		 System.out.println("inside motech");
		 CommcareForm form = new CommcareFormBuilder().buildFrom(motechEvent);
		 
		 motherFormProcessor.process(form);
		 System.out.println("type of form"+form.getForm().getSubElements().entries());
	       
	}
}
