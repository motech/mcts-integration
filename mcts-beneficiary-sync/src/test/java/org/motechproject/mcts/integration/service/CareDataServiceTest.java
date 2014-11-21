package org.motechproject.mcts.integration.service;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMotherServiceUpdate;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.CareDataService;

@RunWith(MockitoJUnitRunner.class)
public class CareDataServiceTest {

    @Mock
    private MctsRepository careDataRepository;

    @InjectMocks
    private CareDataService careDataService = new CareDataService();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetBeneficiariesToSync() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        List<Beneficiary> expectedBeneficiaries = Arrays.asList(
                new Beneficiary(1, "mcts_id1", 2, new Date(), "9999900000", 1,
                        null, null, null), new Beneficiary(2, "mcts_id2", 4,
                        new Date(), "9999911111", 1, null, null, null));
        Mockito.when(
                careDataRepository.getBeneficiariesToSync(startDate, endDate))
                .thenReturn(expectedBeneficiaries);
        List<Beneficiary> actualBeneficiaries = careDataService
                .getBeneficiariesToSync(startDate, endDate);
        verify(careDataRepository).getBeneficiariesToSync(startDate, endDate);
        assertEquals(expectedBeneficiaries, actualBeneficiaries);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldMapMotherCaseToNewMctsPregnantMotherAndSaveIt()
            throws BeneficiaryException {
        String caseId = "caseId";
        String mctsId = "mctsId";
        MotherCase motherCase = new MotherCase();
        motherCase.setCaseId(caseId);
        when(
                careDataRepository.findEntityByField(MotherCase.class,
                        "caseId", caseId)).thenReturn(motherCase);
        when(
                careDataRepository.findEntityByField(MctsPregnantMother.class,
                        "motherCase", motherCase)).thenReturn(null);

        careDataService.mapMotherCaseToMctsPregnantMother(caseId, mctsId);

        ArgumentCaptor<MctsPregnantMother> captor = ArgumentCaptor
                .forClass(MctsPregnantMother.class);
        verify(careDataRepository).saveOrUpdate(captor.capture());
        MctsPregnantMother savedMctsPregnantMother = captor.getValue();
        assertEquals(motherCase, savedMctsPregnantMother.getMotherCase());
        assertEquals(mctsId, savedMctsPregnantMother.getMctsId());
    }

    /**
     * @throws BeneficiaryException
     * 
     */
    @SuppressWarnings("deprecation")
    @Test
    public void shouldUpdateMctsPregnantMotherWithNewMCTSIdIfItAlreadyExists()
            throws BeneficiaryException {
        String caseId = "caseId";
        String newMctsId = "newMctsId";
        MotherCase motherCase = new MotherCase();
        motherCase.setCaseId(caseId);
        when(
                careDataRepository.findEntityByField(MotherCase.class,
                        "caseId", caseId)).thenReturn(motherCase);
        MctsPregnantMother existingMctsPregnantMother = new MctsPregnantMother();
        existingMctsPregnantMother.setMctsId("oldMctsId");
        existingMctsPregnantMother.setMotherCase(motherCase);

        when(
                careDataRepository.findEntityByField(MctsPregnantMother.class,
                        "motherCase", motherCase)).thenReturn(
                existingMctsPregnantMother);

        careDataService.mapMotherCaseToMctsPregnantMother(caseId, newMctsId);

        ArgumentCaptor<MctsPregnantMother> captor = ArgumentCaptor
                .forClass(MctsPregnantMother.class);
        verify(careDataRepository).saveOrUpdate(captor.capture());
        MctsPregnantMother savedMctsPregnantMother = captor.getValue();
        assertEquals(existingMctsPregnantMother, savedMctsPregnantMother);
        assertEquals(newMctsId, savedMctsPregnantMother.getMctsId());
    }

    @Test
    public void shouldUpdateSyncedBeneficiaries() throws BeneficiaryException {

        Beneficiary beneficiary1 = new Beneficiary(1, "mcts_id1", 2,
                new Date(), "9999900000", 1, null, null, null);
        Beneficiary beneficiary2 = new Beneficiary(2, "mcts_id2", 4,
                new Date(), "9999911111", 1, null, null, null);
        List<Beneficiary> beneficiaries = Arrays.asList(beneficiary1,
                beneficiary2);
        MctsPregnantMother MctsPregnantMother1 = new MctsPregnantMother();
        MctsPregnantMother MctsPregnantMother2 = new MctsPregnantMother();
        when(
                careDataRepository.getMotherFromPrimaryId(beneficiary1
                        .getMctsPregnantMotherId())).thenReturn(
                MctsPregnantMother1);
        when(
                careDataRepository.getMotherFromPrimaryId(beneficiary2
                        .getMctsPregnantMotherId())).thenReturn(
                MctsPregnantMother2);

        careDataService.updateSyncedBeneficiaries(beneficiaries);

        ArgumentCaptor<MctsPregnantMotherServiceUpdate> captor = ArgumentCaptor
                .forClass(MctsPregnantMotherServiceUpdate.class);
        verify(careDataRepository, times(2)).saveOrUpdate(captor.capture());
        List<MctsPregnantMotherServiceUpdate> updatedRecords = captor
                .getAllValues();
        assertMctsPregnantMotherServiceUpdate(updatedRecords.get(0),
                beneficiary1, MctsPregnantMother1);
        assertMctsPregnantMotherServiceUpdate(updatedRecords.get(1),
                beneficiary2, MctsPregnantMother2);
    }
    
    @Test
    public void shouldGetMctsPregnantMotherFromCaseId(){
    	MctsPregnantMother MctsPregnantMother1 = new MctsPregnantMother();
    	when(
    			careDataRepository.findEntityByField(MctsPregnantMother.class,
                "motherCase.id", MctsPregnantMother1.getMctsId())).thenReturn(
                		MctsPregnantMother1);
    	MctsPregnantMother MctsPregnantMother2 = careDataService.getMctsPregnantMotherFromCaseId(MctsPregnantMother1.getMctsId());
    	assertEquals(MctsPregnantMother1, MctsPregnantMother2);
    }

    @SuppressWarnings("deprecation")
    private void assertMctsPregnantMotherServiceUpdate(
            MctsPregnantMotherServiceUpdate updatedRecord,
            Beneficiary beneficiary, MctsPregnantMother MctsPregnantMother) {
        assertEquals(MctsPregnantMother, updatedRecord.getMctsPregnantMother());
        assertEquals(beneficiary.getServiceDeliveryDate(), updatedRecord
                .getServiceDeliveryDate());
        assertEquals(Integer.valueOf(beneficiary.getServiceType().toString()),
                updatedRecord.getServiceType());
    }
}
