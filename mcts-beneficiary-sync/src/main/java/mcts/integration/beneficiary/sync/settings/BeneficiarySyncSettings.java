package mcts.integration.beneficiary.sync.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class BeneficiarySyncSettings {

    private Properties properties;

    @Autowired
    public BeneficiarySyncSettings(@Qualifier("beneficiarySyncProperties") Properties properties) {
        this.properties = properties;
    }

    public String getSyncUrl() {
        return String.format("%s/%s?%s", properties.getProperty("mcts.base.url"), properties.getProperty("beneficiary.sync.request.url"), getSyncRequestParams());
    }

    private String getSyncRequestParams() {
        return String.format("%s=%s&%s=%s&%s=%s",
                properties.getProperty("mcts.authentication.username.key"), properties.getProperty("mcts.authentication.username"),
                properties.getProperty("mcts.authentication.password.key"), properties.getProperty("mcts.authentication.password"),
                properties.getProperty("beneficiary.sync.request.operation.key"), properties.getProperty("beneficiary.sync.request.operation"));

    }

    public Integer getStateId() {
        return Integer.parseInt(properties.getProperty("beneficiary.state.id"));
    }
}
