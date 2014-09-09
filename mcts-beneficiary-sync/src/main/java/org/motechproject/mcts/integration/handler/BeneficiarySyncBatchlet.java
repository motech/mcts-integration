package org.motechproject.mcts.integration.handler;

import java.util.ArrayList;
import java.util.List;

import javax.batch.api.Batchlet;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeneficiarySyncBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncBatchlet.class);
    private List<Beneficiary> beneficiariesList = new ArrayList<Beneficiary>();
    private DateTime startDate = new DateTime();
    private DateTime endDate = new DateTime();

    @Autowired
    private MctsRepository careDataRepository;

    @Override
    public String process() {
        beneficiariesList = careDataRepository.getBeneficiariesToSync(
                startDate, endDate);
        LOGGER.info("beneficiaries list size"
                + Integer.toString(beneficiariesList.size()));
        LOGGER.debug("beneficiaries list size"
                + Integer.toString(beneficiariesList.size()));
        return null;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
