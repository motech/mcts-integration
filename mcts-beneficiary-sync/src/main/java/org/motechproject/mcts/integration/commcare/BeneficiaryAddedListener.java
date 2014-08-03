package org.motechproject.mcts.integration.commcare;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.utils.MCTSEventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryAddedListener {

    @Autowired
    private CreateCaseXmlService createCaseXmlService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CreateCaseXmlService.class);

    @MotechListener(subjects = MCTSEventConstants.EVENT_BENEFICIARIES_ADDED)
    public void handleEvent(MotechEvent motechEvent) {
        try {
            createCaseXmlService.createCaseXml();
        } catch (BeneficiaryException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
