package org.motechproject.mcts.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.springframework.stereotype.Component;

@Component
public class BatchServiceUrlGenerator {

    private Properties properties = new Properties();
    private InputStream input;

    public BatchServiceUrlGenerator(Properties properties) {
        this.properties = properties;
    }

    public BatchServiceUrlGenerator() {

    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getUploadXmlUrl() {
        return (properties.getProperty("batch.base.url") + properties
                .getProperty("rest-batch.properties"));
    }

    public String getScheduleBatchUrl() {
        return (properties.getProperty("batch.base.url") + properties
                .getProperty("batch.schedule.job.url"));
    }

    public String getTriggerJobUrl() {
        return (properties.getProperty("batch.base.url") + properties
                .getProperty("batch.trigger.job.url"));
    }

    public String getCaseUploadUrl() throws BeneficiaryException {
        loadInputFile("rest-batch.properties");
        return (properties.getProperty("commcareHQ.base.url") + properties
                .getProperty("commcare.upload.url"));
    }

    private void loadInputFile(String propertiesPath)
            throws BeneficiaryException {

        try {
            input = BatchServiceUrlGenerator.class.getClassLoader()
                    .getResourceAsStream(propertiesPath);
            properties.load(input);
        } catch (IOException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e,
                    e.getMessage());
        }

    }

}
