package org.motechproject.mcts.integration.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.springframework.stereotype.Component;

@Component
public class CaseCommcareListener {
	
	 private static final Logger logger = LoggerFactory.getLogger(CaseCommcareListener.class);
	
	    //@Autowired
	    public CaseCommcareListener() {
	    }
	    
	    @MotechListener(subjects = EventSubjects.CASE_EVENT)
	    public void handleEvent(MotechEvent event) {
	    	
	    }
}
