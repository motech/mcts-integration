package mcts.integration.beneficiary.sync.service;

import org.joda.time.DateTime;

public interface BeneficiarySyncService {

    void syncBeneficiaryData(DateTime startDate, DateTime endDate);

}
