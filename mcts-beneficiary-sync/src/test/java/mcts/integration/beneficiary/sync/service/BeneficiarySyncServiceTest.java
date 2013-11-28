package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.domain.Beneficiary;
import mcts.integration.beneficiary.sync.request.BeneficiaryDetails;
import mcts.integration.beneficiary.sync.request.BeneficiaryRequest;
import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiarySyncServiceTest {
    @Mock
    private CareDataService careDataService;
    @Mock
    private MCTSHttpClientService mctsHttpClientService;
    @Mock
    private BeneficiarySyncSettings beneficiarySyncSettings;

    private BeneficiarySyncService beneficiarySyncService;

    @Before
    public void setUp() throws Exception {
        beneficiarySyncService = new BeneficiarySyncService(careDataService, mctsHttpClientService, beneficiarySyncSettings);
    }

    @Test
    public void shouldGetBeneficiaryDataAndSync() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        List<Beneficiary> beneficiaries = Arrays.asList(new Beneficiary("mcts_id1", 2), new Beneficiary("mcts_id2", 4));
        when(careDataService.getBeneficiariesToSync(startDate, endDate)).thenReturn(beneficiaries);
        when(beneficiarySyncSettings.getStateId()).thenReturn(31);

        beneficiarySyncService.syncBeneficiaryData(startDate, endDate);

        verify(careDataService).getBeneficiariesToSync(startDate, endDate);

        ArgumentCaptor<BeneficiaryRequest> beneficiaryRequestCaptor = ArgumentCaptor.forClass(BeneficiaryRequest.class);
        verify(mctsHttpClientService).sync(beneficiaryRequestCaptor.capture());
        BeneficiaryRequest actualRequest = beneficiaryRequestCaptor.getValue();
        List<BeneficiaryDetails> beneficiaryDetails = actualRequest.getAllBeneficiaryDetails();
        assertEquals(2, beneficiaryDetails.size());
        assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31, "mcts_id1", 2)));
        assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31, "mcts_id2", 4)));
    }
}
