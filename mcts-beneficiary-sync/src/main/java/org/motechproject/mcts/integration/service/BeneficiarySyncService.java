package org.motechproject.mcts.integration.service;

import org.joda.time.DateTime;

public interface BeneficiarySyncService {

    void syncBeneficiaryData(DateTime startDate, DateTime endDate);

}
