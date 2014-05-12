package org.motechproject.mcts.integration.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class MCTSBeneficiarySyncService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private MCTSHttpClientService mctsHttpClientService;
    private PropertyReader propertyReader;
    private Publisher publisher = new Publisher();
    private String beneficiaryData;
    private String outputFileLocation;

    @Autowired
    public MCTSBeneficiarySyncService(MCTSHttpClientService mctsHttpClientService, PropertyReader propertyReader) {
        this.mctsHttpClientService = mctsHttpClientService;
        this.propertyReader = propertyReader;
    }

    public void syncBeneficiaryData(DateTime startDate, DateTime endDate) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.putAll(propertyReader.getDefaultBeneficiaryListQueryParams());
        requestBody.add("FromDate", startDate.toString(DATE_FORMAT));
        requestBody.add("ToDate", endDate.toString(DATE_FORMAT));

        String beneficiaryData = mctsHttpClientService.syncFrom(requestBody);
        if (beneficiaryData == null)
        	return;

        outputFileLocation = String.format("%s_%s", propertyReader.getSyncRequestOutputFileLocation(), DateTime.now());
        try {
            FileUtils.writeStringToFile(new File(outputFileLocation), beneficiaryData);
        } catch (IOException e) {
            LOGGER.error(String.format("Cannot write MCTS beneficiary details response to file: %s", outputFileLocation), e);
            return;
        }
        LOGGER.info(String.format("MCTS beneficiary details response is added to file %s", outputFileLocation));
        LOGGER.info("Notifying Hub to Publish the Updates at url" + getHubSyncFromUrl() );
       publisher.publish(getHubSyncFromUrl(), beneficiaryData);
        
    }
    
    public String getHubSyncFromUrl(){
    	return propertyReader.getHubSyncFromUrl() + outputFileLocation;
    }

}
