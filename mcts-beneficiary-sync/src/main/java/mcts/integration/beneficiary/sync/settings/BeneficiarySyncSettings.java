package mcts.integration.beneficiary.sync.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Properties;

@Component
public class BeneficiarySyncSettings {

    private Properties properties;

    @Autowired
    public BeneficiarySyncSettings(@Qualifier("beneficiarySyncProperties") Properties properties) {
        this.properties = properties;
    }

    public String getUpdateRequestUrl() {
        return String.format("%s/%s?%s", properties.getProperty("mcts.base.url"), properties.getProperty("beneficiary.sync.update.request.url"), getUpdateRequestParams());
    }

    private String getUpdateRequestParams() {
        return String.format("%s=%s&%s=%s&%s=%s",
                properties.getProperty("beneficiary.sync.update.request.authentication.username.key"), properties.getProperty("mcts.authentication.username"),
                properties.getProperty("beneficiary.sync.update.request.authentication.password.key"), properties.getProperty("mcts.authentication.password"),
                properties.getProperty("beneficiary.sync.update.request.operation.key"), properties.getProperty("beneficiary.sync.update.request.operation"));
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
        queryParams.add(properties.getProperty("beneficiary.sync.get.request.authentication.password.key"), properties.getProperty("mcts.authentication.password"));

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
}
