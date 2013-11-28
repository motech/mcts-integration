package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.request.BeneficiaryRequest;
import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MCTSHttpClientServiceTest {

    @Mock
    private BeneficiarySyncSettings beneficiarySyncSettings;
    @Mock
    private RestTemplate restTemplate;

    private MCTSHttpClientService mctsHttpClientService;

    @Before
    public void setUp() throws Exception {
        mctsHttpClientService = new MCTSHttpClientService(restTemplate, beneficiarySyncSettings);
    }

    @Test
    public void shouldSyncBeneficiaries() {
        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        String requestUrl = "requestUrl";
        when(beneficiarySyncSettings.getSyncUrl()).thenReturn(requestUrl);

        mctsHttpClientService.sync(beneficiaryRequest);

        verify(restTemplate).postForEntity(requestUrl, beneficiaryRequest, String.class);
    }
}
