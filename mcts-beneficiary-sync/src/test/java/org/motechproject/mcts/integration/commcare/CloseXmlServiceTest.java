package org.motechproject.mcts.integration.commcare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.DateValidator;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class CloseXmlServiceTest {

    @InjectMocks
    private CloseCaseXmlService closeCaseXmlService = new CloseCaseXmlService();

    @Mock
    MctsRepository careDataRepository;

    @Mock
    PropertyReader propertyReader;

    @Mock
    MCTSHttpClientService mCTSHttpClientService;

    @Mock
    FixtureDataService fixtureDataService;

    List<MctsPregnantMother> motherList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        motherList = new ArrayList<MctsPregnantMother>();

        MctsPregnantMother mother1 = new MctsPregnantMother();
        mother1.setName("soniya devi");
        mother1.setFatherHusbandName("Dharmandra Sada");
        mother1.setHindiName("soniya devi");
        mother1.setHindiFatherHusbandName("Dharmandra Sada");
        MctsHealthworker healthWorker = new MctsHealthworker();
        healthWorker.setHealthworkerId(21);
        mother1.setMctsHealthworkerByAshaId(healthWorker);
        mother1.setBirthDate(DateValidator.checkDateInFormat("12-11-1987",
                "dd-MM-yyyy"));
        mother1.setLmpDate(DateValidator.checkDateInFormat("10-03-2011",
                "dd-MM-yyyy"));

        MctsPregnantMother mother2 = new MctsPregnantMother();
        mother2.setName("Ranju Devi");
        mother2.setFatherHusbandName("Dilkush Kamat");
        mother2.setHindiName("Ranju Devi");
        mother2.setHindiFatherHusbandName("Dilkush Kamat");

        motherList.add(mother1);
        motherList.add(mother2);

        HttpStatus status = HttpStatus.ACCEPTED;

        Mockito.when(careDataRepository.getMctsPregnantMotherForClosedCases())
                .thenReturn(motherList);
        Mockito.when(careDataRepository.getMotherFromPrimaryId(anyInt()))
        .thenReturn(mother1);
        Mockito.when(careDataRepository.getDetachedFieldId(anyObject()))
                .thenReturn(1);
        Mockito.when(propertyReader.sizeOfXml()).thenReturn(50);
        Mockito.when(
                mCTSHttpClientService.syncToCloseCommcare((CloseData) any()))
                .thenReturn(status);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                MctsPregnantMother mother = (MctsPregnantMother) args[0];
                assertNotNull(mother);
                // verify
                return null;
            }
        }).when(careDataRepository).saveOrUpdate(
                (MctsPregnantMother) anyObject());
    }

    @Test
    public void shouldCloseCaseXml() throws BeneficiaryException {
        closeCaseXmlService.closeCaseXml();
        verify(careDataRepository).getMctsPregnantMotherForClosedCases();
        verify(careDataRepository, times(2)).getMotherFromPrimaryId(anyInt());
        verify(careDataRepository, times(2)).getDetachedFieldId(anyObject());
        verify(mCTSHttpClientService).syncToCloseCommcare((CloseData) any());
        verify(careDataRepository, times(2)).saveOrUpdate((MctsPregnantMother) any());
    }
}
