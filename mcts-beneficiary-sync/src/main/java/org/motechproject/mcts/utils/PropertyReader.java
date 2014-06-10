/**
 * Reads properties from the properties file <code>beneficiary_sync.properties</code> and return them
 */
package org.motechproject.mcts.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class PropertyReader {

    private Properties properties;

    @Autowired
    public PropertyReader(@Qualifier("beneficiarySyncProperties") Properties properties) {
        this.properties = properties;
    }
    
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getUpdateRequestUrl() {
        return String.format("%s/%s?%s", properties.getProperty("mcts.base.url"), properties.getProperty("beneficiary.sync.update.request.url"), getUpdateRequestParams());
    }

    private String getUpdateRequestParams() {
        return String.format("%s=%s&%s=%s&%s=%s",
                properties.getProperty("beneficiary.sync.update.request.authentication.username.key"), properties.getProperty("mcts.authentication.username"),
                properties.getProperty("beneficiary.sync.update.request.authentication.password.key"), getPassword(),
                properties.getProperty("beneficiary.sync.update.request.operation.key"), properties.getProperty("beneficiary.sync.update.request.operation"));
    }
    
    public String getPassword(){
    	return Encryption.encryptWithTimeInSeconds(properties.getProperty("mcts.authentication.password"));
    }

    public Integer getStateId() {
        return Integer.parseInt(properties.getProperty("beneficiary.state.id"));
    }

    public String getBeneficiaryListRequestUrl() {
        return String.format("%s/%s", properties.getProperty("mcts.base.url"), properties.getProperty("beneficiary.sync.get.request.url"));
    }

    public MultiValueMap<String, String> getDefaultBeneficiaryListQueryParams() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("State_id", getStateId().toString());
        queryParams.add(properties.getProperty("beneficiary.sync.get.request.authentication.username.key"), properties.getProperty("mcts.authentication.username"));
        queryParams.add(properties.getProperty("beneficiary.sync.get.request.authentication.password.key"), getPassword());

        return queryParams;
    }

    public String getSyncRequestOutputFileLocation() {
        return properties.getProperty("beneficiary.sync.get.request.output.file.absolute.location");
    }
    
    public String getUpdateXmlOutputFileLocation() {
        return properties.getProperty("beneficiary.sync.update.xml.output.file.absolute.location");
    }
    
    public String getUpdateUrlOutputFileLocation() {
        return properties.getProperty("beneficiary.sync.update.url.output.file.absolute.location");
    }
    
    public String getSyncCsvFileLocation() {
        return properties.getProperty("beneficiary.sync.csv.input.file.absolute.location");
    }
    
    public String getFLWCsvFileLocation() {
    	return properties.getProperty("beneficiary.sync.csv2.input.file.absolute.location");
    }
    
    public String getHubBaseUrl() {
        return String.format("%s/%s", properties.getProperty("motech.platform.base.url"), properties.getProperty("motech.platform.hub.url"));
    }
    
    public String getHubSyncFromUrl(String startTime, String endTime) throws UnsupportedEncodingException {
        return String.format("%s/%s?startTime=%s&endTime=%s", properties.getProperty("motech.base.url"), properties.getProperty("hub.sync.from.url"), startTime, endTime);
    }
    
    public int getMaxNumberOfPublishRetryCount(){
    	return Integer.parseInt(properties.getProperty("max.publish.retry.count"));
    }
    
    public int getHubRetryInterval()
    {
    	return Integer.parseInt(properties.getProperty("hub.retry.interval"));
    }
    
    public String getBenificiaryUpdateTopicUrlForHub()
    {
    	return String.format("%s/%s", properties.getProperty("motech.base.url"), properties.getProperty("benificiary.update.topic.url.for.hub"));
    }

	public String getMotechLoginRedirectUrl() {
		return String.format("%s/%s", properties.getProperty("motech.platform.base.url"), properties.getProperty("motech.platform.login.redirect.url"));
	}

	public String getMotechPlatformLoginUrl() {
		return String.format("%s/%s", properties.getProperty("motech.platform.base.url"), properties.getProperty("motech.platform.login.url"));
		}

	public MultiValueMap<String, String> getMotechPlatformLoginForm() {
		  MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	        form.add("j_username", properties.getProperty("motech.platform.username"));
	        form.add("j_password", properties.getProperty("motech.platform.password"));
		return form;
	}
}
