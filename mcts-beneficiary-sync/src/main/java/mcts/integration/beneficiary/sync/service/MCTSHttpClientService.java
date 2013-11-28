package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.request.BeneficiaryRequest;
import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MCTSHttpClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MCTSHttpClientService.class);

    private RestTemplate restTemplate;
    private BeneficiarySyncSettings beneficiarySyncSettings;

    @Autowired
    public MCTSHttpClientService(@Qualifier("mctsRestTemplate") RestTemplate restTemplate, BeneficiarySyncSettings beneficiarySyncSettings) {
        this.restTemplate = restTemplate;
        this.beneficiarySyncSettings = beneficiarySyncSettings;
    }

    public void sync(BeneficiaryRequest beneficiaryRequest) {
        ResponseEntity<String> reponse = restTemplate.postForEntity(beneficiarySyncSettings.getSyncUrl(), beneficiaryRequest, String.class);

        if (reponse != null)
            LOGGER.info(String.format("Response [%s] : %s", reponse.getStatusCode(), reponse.getBody()));
    }
}
