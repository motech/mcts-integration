package org.motechproject.mcts.integration.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.parser.CaseInfoParser;
import org.motechproject.mcts.integration.parser.CaseInfoParserImpl;

@RunWith(MockitoJUnitRunner.class)
public class MotherFormProcessorTest extends BaseTest {

    @InjectMocks
    MotherFormProcessor motherFormProcessor = new MotherFormProcessor();
    @Mock
    CaseInfoParser infoParser;
    @Mock
    FormsProcessor formsProcessor;

    private CommcareForm commcareForm = null;
    private CaseInfoParser caseInfoParser = new CaseInfoParserImpl();
    private FormValueElement caseElement = null;
    private Map<String, String> caseInfo = null;

    @Test
    public void processTest_map_existing() throws BeneficiaryException, FullFormParserException {
        commcareForm = getFormElement("src/test/resources/caseXml/after_asha_approval");
        caseElement = caseInfoParser.getCaseElement(commcareForm.getForm());
        caseInfo = caseInfoParser.parse(commcareForm.getForm(), true);
        when(infoParser.getCaseElement((FormValueElement) any())).thenReturn(
                caseElement);
        when(infoParser.parse((FormValueElement) any(), anyBoolean()))
                .thenReturn(caseInfo);

        motherFormProcessor.process(commcareForm);
        //verify(unapprovedFormProcessor).process((Map) any());
    }
    
    @Test
    public void processTest_mapping_to_approve() throws BeneficiaryException, FullFormParserException {
        commcareForm = getFormElement("src/test/resources/caseXml/approved_case");
        caseElement = caseInfoParser.getCaseElement(commcareForm.getForm());
        caseInfo = caseInfoParser.parse(commcareForm.getForm(), true);
        when(infoParser.getCaseElement((FormValueElement) any())).thenReturn(
                caseElement);
        when(infoParser.parse((FormValueElement) any(), anyBoolean()))
                .thenReturn(caseInfo);

        motherFormProcessor.process(commcareForm);
        verify(formsProcessor).processForm((Map<String, String>) any());
    }
    
    @Test
    public void processTest_already_closed() throws BeneficiaryException, FullFormParserException {
        commcareForm = getFormElement("src/test/resources/caseXml/closed_one");
        caseElement = caseInfoParser.getCaseElement(commcareForm.getForm());
        caseInfo = caseInfoParser.parse(commcareForm.getForm(), true);
        when(infoParser.getCaseElement((FormValueElement) any())).thenReturn(
                caseElement);
        when(infoParser.parse((FormValueElement) any(), anyBoolean()))
                .thenReturn(caseInfo);

        motherFormProcessor.process(commcareForm);
        verify(formsProcessor).processForm((Map<String, String>) any());
    }

}
