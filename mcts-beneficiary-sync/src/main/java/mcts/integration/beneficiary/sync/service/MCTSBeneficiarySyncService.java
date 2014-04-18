package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

@Component
public class MCTSBeneficiarySyncService implements BeneficiarySyncService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private MCTSHttpClientService mctsHttpClientService;
    private BeneficiarySyncSettings beneficiarySyncSettings;

    @Autowired
    public MCTSBeneficiarySyncService(MCTSHttpClientService mctsHttpClientService, BeneficiarySyncSettings beneficiarySyncSettings) {
        this.mctsHttpClientService = mctsHttpClientService;
        this.beneficiarySyncSettings = beneficiarySyncSettings;
    }

    public void syncBeneficiaryData(DateTime startDate, DateTime endDate) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.putAll(beneficiarySyncSettings.getDefaultBeneficiaryListQueryParams());
        requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
        requestBody.add("ToDate", endDate.toString(DATE_FORMAT));

        String beneficiaryData = mctsHttpClientService.syncFrom(requestBody);

        String outputFileLocation = String.format("%s_%s", beneficiarySyncSettings.getSyncRequestOutputFileLocation(), DateTime.now());
        try {
            FileUtils.writeStringToFile(new File(outputFileLocation), beneficiaryData);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot write MCTS beneficiary details response to file: %s", outputFileLocation), e);
            return;
        }
        LOGGER.info(String.format("MCTS beneficiary details response is added to file %s", outputFileLocation));
    }

}
