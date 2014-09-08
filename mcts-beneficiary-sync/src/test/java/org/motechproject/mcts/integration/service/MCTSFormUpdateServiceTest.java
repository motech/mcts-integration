package org.motechproject.mcts.integration.service;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.integration.repository.CareDataRepository;

@RunWith(MockitoJUnitRunner.class)
public class MCTSFormUpdateServiceTest {
    @InjectMocks
    private MCTSFormUpdateService mCTSFormUpdateService = new MCTSFormUpdateService();

    @Mock
    CareDataRepository careDataRepository;

    MctsPregnantMother mother1;

    MotherCase motherCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mother1 = new MctsPregnantMother();
        mother1.setName("soniya devi");
        mother1.setFatherHusbandName("Dharmandra Sada");
        mother1.setHindiName("soniya devi");
        mother1.setHindiFatherHusbandName("Dharmandra Sada");
        mother1.setHhNumber("1234");
        mother1.setFamilyNumber("1234");
        mother1.setOwnerId("1234");
        //mother1.setId(50);

        motherCase = new MotherCase();

    }


    @Test
    public void shouldUpdateMctsPregnantMotherForm() {
        when(careDataRepository.getMotherFromPrimaryId(50)).thenReturn(mother1);
        when(careDataRepository
                        .matchMctsPersonawithMotherCase(1234, 1234, "1234")).thenReturn(motherCase);
        mCTSFormUpdateService.updateMctsPregnantMotherForm(50);
        verify(careDataRepository).saveOrUpdate((MctsPregnantMother)anyObject());
        
    }

}
