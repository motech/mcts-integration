package org.motechproject.mcts.integration.commcare;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.utils.MCTSEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiariesUpdatedListener {

    @Autowired
    private UpdateCaseXmlService updateCaseXmlService;

    @MotechListener(subjects = MCTSEventConstants.EVENT_BENEFICIARY_UPDATED)
    public void handleEvent(MotechEvent motechEvent) {
        Integer id = (Integer) motechEvent.getParameters().get(
                MCTSEventConstants.PARAM_BENEFICIARY_KEY);
        updateCaseXmlService.updateXml(id);

    }

}
