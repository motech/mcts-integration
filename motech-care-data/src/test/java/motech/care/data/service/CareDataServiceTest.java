package motech.care.data.service;

import motech.care.data.domain.Beneficiary;
import motech.care.data.repository.CareDataRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CareDataServiceTest {

    @Mock
    private CareDataRepository careDataRepository;

    private CareDataService careDataService;

    @Before
    public void setUp() throws Exception {
        careDataService = new CareDataService(careDataRepository);
    }

    @Test
    public void shouldGetBeneficiariesToSync() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        List<Beneficiary> expectedBeneficiaries = Arrays.asList(new Beneficiary("mcts_id1", 2), new Beneficiary("mcts_id2", 4));
        Mockito.when(careDataRepository.getBeneficiariesToSync(startDate, endDate)).thenReturn(expectedBeneficiaries);

        List<Beneficiary> actualBeneficiaries = careDataService.getBeneficiariesToSync(startDate, endDate);

        verify(careDataRepository).getBeneficiariesToSync(startDate, endDate);
        assertEquals(expectedBeneficiaries, actualBeneficiaries);
    }

    @Test
    public void shouldUpdateMCTSIdForAGivenCaseId() {
        String caseId = "caseId";
        String mctsId = "mctsId";

        careDataService.updateCase(caseId, mctsId);

        verify(careDataRepository).updateCase(caseId, mctsId);
    }
}
