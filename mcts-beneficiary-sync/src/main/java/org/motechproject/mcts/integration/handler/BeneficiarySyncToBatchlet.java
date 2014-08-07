package org.motechproject.mcts.integration.handler;

import java.util.ArrayList;
import java.util.List;

import javax.batch.api.Batchlet;

import org.joda.time.DateTime;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.service.MCTSBeneficiarySyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BeneficiarySyncToBatchlet implements Batchlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncToBatchlet.class);
    private List<Beneficiary> beneficiariesList = new ArrayList<Beneficiary>();
    private DateTime startDate = new DateTime(2014, 01, 03, 12, 30, 30);
    private DateTime endDate = new DateTime(2014, 02, 03, 12, 30, 30);

    @Autowired
    private MCTSBeneficiarySyncService mctsBeneficiarySyncService;

    @Override
    public String process() throws Exception {
        mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
        LOGGER.info("beneficiaries list size"
                + Integer.toString(beneficiariesList.size()));
        return null;
    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub

    }

}
