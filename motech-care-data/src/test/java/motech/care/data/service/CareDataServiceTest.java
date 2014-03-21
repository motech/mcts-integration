package motech.care.data.service;

import motech.care.data.domain.Beneficiary;
import motech.care.data.domain.MCTSPregnantMother;
import motech.care.data.domain.MCTSPregnantMotherServiceUpdate;
import motech.care.data.repository.CareDataRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.care.reporting.domain.dimension.MotherCase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        List<Beneficiary> expectedBeneficiaries = Arrays.asList(
        		new Beneficiary(1, "mcts_id1", 2, new Date(),"9999900000", 1, null, null,null),
        		new Beneficiary(2, "mcts_id2", 4, new Date(), "9999911111", 1, null, null,null));
        Mockito.when(careDataRepository.getBeneficiariesToSync(startDate, endDate)).thenReturn(expectedBeneficiaries);

        List<Beneficiary> actualBeneficiaries = careDataService.getBeneficiariesToSync(startDate, endDate);

        verify(careDataRepository).getBeneficiariesToSync(startDate, endDate);
        assertEquals(expectedBeneficiaries, actualBeneficiaries);
    }

    @Test
    public void shouldMapMotherCaseToNewMCTSPregnantMotherAndSaveIt() {
        String caseId = "caseId";
        String mctsId = "mctsId";
        MotherCase motherCase = new MotherCase();
        motherCase.setCaseId(caseId);
        when(careDataRepository.findEntityByField(MotherCase.class, "caseId", caseId)).thenReturn(motherCase);
        when(careDataRepository.findEntityByField(MCTSPregnantMother.class, "motherCase", motherCase)).thenReturn(null);

        careDataService.mapMotherCaseToMCTSPregnantMother(caseId, mctsId);

        ArgumentCaptor<MCTSPregnantMother> captor = ArgumentCaptor.forClass(MCTSPregnantMother.class);
        verify(careDataRepository).saveOrUpdate(captor.capture());
        MCTSPregnantMother savedMCTSPregnantMother = captor.getValue();
        assertEquals(motherCase, savedMCTSPregnantMother.getMotherCase());
        assertEquals(mctsId, savedMCTSPregnantMother.getMctsId());
    }

    @Test
    public void shouldUpdateMCTSPregnantMotherWithNewMCTSIdIfItAlreadyExists() {
        String caseId = "caseId";
        String newMctsId = "newMctsId";
        MotherCase motherCase = new MotherCase();
        motherCase.setCaseId(caseId);
        when(careDataRepository.findEntityByField(MotherCase.class, "caseId", caseId)).thenReturn(motherCase);
        MCTSPregnantMother existingMCTSPregnantMother = new MCTSPregnantMother("oldMctsId", motherCase);
        when(careDataRepository.findEntityByField(MCTSPregnantMother.class, "motherCase", motherCase)).thenReturn(existingMCTSPregnantMother);

        careDataService.mapMotherCaseToMCTSPregnantMother(caseId, newMctsId);

        ArgumentCaptor<MCTSPregnantMother> captor = ArgumentCaptor.forClass(MCTSPregnantMother.class);
        verify(careDataRepository).saveOrUpdate(captor.capture());
        MCTSPregnantMother savedMCTSPregnantMother = captor.getValue();
        assertEquals(existingMCTSPregnantMother.getMotherCase(), savedMCTSPregnantMother.getMotherCase());
        assertEquals(newMctsId, savedMCTSPregnantMother.getMctsId());
    }

    @Test
    public void shouldUpdateSyncedBeneficiaries() {

        Beneficiary beneficiary1 = new Beneficiary(1, "mcts_id1", 2, new Date(),"9999900000", 1, null, null,null);
        Beneficiary beneficiary2 = new Beneficiary(2, "mcts_id2", 4, new Date(),"9999911111", 1, null, null,null);
        List<Beneficiary> beneficiaries = Arrays.asList(beneficiary1, beneficiary2);
        MCTSPregnantMother mctsPregnantMother1 = new MCTSPregnantMother();
        MCTSPregnantMother mctsPregnantMother2 = new MCTSPregnantMother();
        when(careDataRepository.load(MCTSPregnantMother.class, beneficiary1.getMctsPregnantMotherId())).thenReturn(mctsPregnantMother1);
        when(careDataRepository.load(MCTSPregnantMother.class, beneficiary2.getMctsPregnantMotherId())).thenReturn(mctsPregnantMother2);

        careDataService.updateSyncedBeneficiaries(beneficiaries);

        ArgumentCaptor<MCTSPregnantMotherServiceUpdate> captor = ArgumentCaptor.forClass(MCTSPregnantMotherServiceUpdate.class);
        verify(careDataRepository, times(2)).saveOrUpdate(captor.capture());
        List<MCTSPregnantMotherServiceUpdate> updatedRecords = captor.getAllValues();
        assertMCTSPregnantMotherServiceUpdate(updatedRecords.get(0), beneficiary1, mctsPregnantMother1);
        assertMCTSPregnantMotherServiceUpdate(updatedRecords.get(1), beneficiary2, mctsPregnantMother2);
    }

    private void assertMCTSPregnantMotherServiceUpdate(MCTSPregnantMotherServiceUpdate updatedRecord, Beneficiary beneficiary, MCTSPregnantMother mctsPregnantMother) {
        assertEquals(mctsPregnantMother, updatedRecord.getMctsPregnantMother());
        assertEquals(beneficiary.getServiceDeliveryDate(), updatedRecord.getServiceDeliveryDate());
        assertEquals(beneficiary.getServiceType(), updatedRecord.getServiceType());
    }
}
