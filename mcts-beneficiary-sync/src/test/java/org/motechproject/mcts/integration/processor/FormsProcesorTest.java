package org.motechproject.mcts.integration.processor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.care.common.mds.measure.CaseAlreadyClosedForm;
import org.motechproject.mcts.care.common.mds.measure.DontKnowForm;
import org.motechproject.mcts.care.common.mds.measure.MapExistingForm;
import org.motechproject.mcts.care.common.mds.measure.MappingToApproveForm;
import org.motechproject.mcts.care.common.mds.measure.UnapprovedToDiscussForm;
import org.motechproject.mcts.care.common.mds.measure.UnmappedToReviewForm;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;

@RunWith(MockitoJUnitRunner.class)
public class FormsProcesorTest {
    @InjectMocks
    FormsProcessor formsProcessor = new FormsProcessor();

    @Mock
    private CareDataService careDataService;
    @Mock
    private MCTSFormUpdateService mctsFormUpdateService;
    @Mock
    MctsRepository careDataRepository;

    MotherCase motherCase = new MotherCase();
    MctsPregnantMother mother = new MctsPregnantMother();

    private static final Map<String, String> motherForm = new HashMap<String, String>() {
        {
            put("appVersion", "appVersion1");
            put("namespace",
                    "http://bihar.commcarehq.org/anm/mapping_to_approve");
            put("caseId", "9dbce942-0163-438b-b3e5-67b473d2f719");
            put("mctsMatch", "yes");
            put("authorized", "approved");
            put("hhNumber", "1234");
            put("mctsFullName", "Soniya Devi");
            put("reasonDisapproved", "random");
            put("mctsId", "2345");

        }
    };

    @Before
    public void setUp() {
        motherCase.setCaseId("9dbce942-0163-438b-b3e5-67b473d2f719");
        when(
                careDataService.findEntityByField(MotherCase.class, "caseId",
                        motherForm.get("caseId"))).thenReturn(motherCase);
        when(careDataRepository.getDetachedFieldId(anyObject())).thenReturn(1);
        when(careDataService.getMctsPregnantMotherFromCaseId(anyString()))
                .thenReturn(mother);
    }

    @Test
    public void processFormTestMappingToapprove() {
        motherForm.put("namespace",
                "http://bihar.commcarehq.org/anm/mapping_to_approve");
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<MappingToApproveForm> captor = ArgumentCaptor
                .forClass(MappingToApproveForm.class);
        verify(careDataService, times(2)).saveOrUpdate(captor.capture());
        MappingToApproveForm form = captor.getValue();
        assertEquals("appVersion1", form.getAppVersion());
    }

    @Test
    public void processFormDontKnowForm() {
        motherForm.put("namespace",
                "http://bihar.commcarehq.org/mcts/dont_know");
        when(
                careDataService.findEntityByField(MctsPregnantMother.class,
                        "mctsPersonaCaseUId", motherForm.get("caseId")))
                .thenReturn(mother);
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<DontKnowForm> captor = ArgumentCaptor
                .forClass(DontKnowForm.class);
        verify(careDataService, times(2)).saveOrUpdate(captor.capture());
        DontKnowForm form = captor.getValue();
        assertEquals("Soniya Devi", form.getMctsFullName());

    }

    @Test
    public void processMapExisting() {
        MotherCase mcase = new MotherCase();
        mcase.setCaseId("f8b34e0c744f8f45dfa12e28617e5717");
        motherForm.put("namespace",
                "http://bihar.commcarehq.org/mcts/map_existing");
        motherForm.put("pregnancyId", "f8b34e0c744f8f45dfa12e28617e5717");
        when(
                careDataService.findEntityByField(MotherCase.class, "caseId",
                        motherForm.get("pregnancyId"))).thenReturn(mcase);
        when(
                careDataService.findEntityByField(MctsPregnantMother.class,
                        "mctsPersonaCaseUId", motherForm.get("caseId")))
                .thenReturn(mother);
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<MapExistingForm> captor = ArgumentCaptor
                .forClass(MapExistingForm.class);
        verify(careDataService, times(2)).saveOrUpdate(captor.capture());
        MapExistingForm form = captor.getValue();
        assertEquals("2345", form.getMctsId());
    }

    @Test
    public void processUnapproveToDiscuss() {
        motherForm.put("namespace",
                "http://bihar.commcarehq.org/anm/unapproved_to_discuss");
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<UnapprovedToDiscussForm> captor = ArgumentCaptor
                .forClass(UnapprovedToDiscussForm.class);
        verify(careDataService).saveOrUpdate(captor.capture());
        UnapprovedToDiscussForm form = captor.getValue();
        assertEquals("random", form.getReasonDisapproved());
    }

    @Test
    public void processUnmappedToReview() {
        motherForm.put("namespace",
                "http://bihar.commcarehq.org/anm/unmapped_to_review");
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<UnmappedToReviewForm> captor = ArgumentCaptor
                .forClass(UnmappedToReviewForm.class);
        verify(careDataService).saveOrUpdate(captor.capture());
        UnmappedToReviewForm form = captor.getValue();
        assertEquals("Soniya Devi", form.getMctsFullName());
    }
    
    @Test
    public void processCaseAlredyClosed() {
        motherForm.put("namespace", "http://bihar.commcarehq.org/mcts/already_closed");
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<CaseAlreadyClosedForm> captor = ArgumentCaptor
                .forClass(CaseAlreadyClosedForm.class);
        verify(careDataService).saveOrUpdate(captor.capture());
        CaseAlreadyClosedForm form = captor.getValue();
        assertEquals("1234",form.getHhNumber());
        assertEquals("Soniya Devi", form.getMctsFullName());
    }
    
    @Test
    public void processCreateNew() {
        motherForm.put("namespace", "http://bihar.commcarehq.org/mcts/create_new");
        motherForm.put("pregnancyId", "9dbce942-0163-438b-b3e5-67b473d2f719");
        motherForm.put("mctsMatch", "archive");
        when(careDataService
                    .findEntityByField(MctsPregnantMother.class,
                            "mctsPersonaCaseUId", motherForm.get("caseId"))).thenReturn(mother);
        formsProcessor.processForm(motherForm);
        ArgumentCaptor<MctsPregnantMother> captor = ArgumentCaptor
                .forClass(MctsPregnantMother.class);
        verify(careDataService).saveOrUpdate(captor.capture());
        MctsPregnantMother savedMctsPregnantMother = captor.getValue();
        assertEquals(motherCase, savedMctsPregnantMother.getMotherCase());
    }
}
