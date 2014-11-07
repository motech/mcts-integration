package org.motechproject.mcts.integration.parser;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;
import static org.motechproject.commcare.parser.FullFormParser.FORM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.builder.CommcareFormBuilder;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.event.MotechEvent;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class CaseInfoParserImplTest {

    @InjectMocks
    private CaseInfoParser caseInfoParser = new CaseInfoParserImpl();
    FullFormParser parser = new FullFormParser(readFile(new File(
            "src/test/resources/after_asha_approval")));
    FormValueElement formValueElement = null;
    Map<String, Object> parameters = new HashMap<>();
    MotechEvent motechEvent = null;

    @Before
    public void setUp() throws FullFormParserException {

        formValueElement = parser.parse();
        parameters.put(VALUE, formValueElement.getValue());
        parameters.put(ELEMENT_NAME, formValueElement.getElementName());
        parameters.put(ATTRIBUTES, formValueElement.getAttributes());
        parameters.put(SUB_ELEMENTS, convertToMap(formValueElement
                .getSubElements()));

        if (FORM.equals(formValueElement.getElementName())) {
            motechEvent = new MotechEvent(FORMS_EVENT, parameters);
        }
        CommcareForm commcareForm = new CommcareFormBuilder()
                .buildFrom(motechEvent);
        formValueElement = commcareForm.getForm();

    }

    @Test
    public void parseTestwithRecursiveTrue() {
        Map<String, String> infoMap = caseInfoParser.parse(formValueElement,
                true);
        assertEquals(infoMap.get("authorized"), "pending");
        assertEquals(infoMap.get("instanceID"),
                "87bdd92f-ab18-4acb-806e-3904ea1257d7");
        assertEquals(infoMap.get("mctsMatch"), "yes");
        assertEquals(infoMap.get("mctsId"), "1000114");
    }

    @Test
    public void parseTestwithRecursiveFalse() {
        Map<String, String> infoMap = caseInfoParser.parse(formValueElement);
        assertEquals(infoMap.get("fullMctsId"), "101216300111000114");
        assertEquals(infoMap.get("confirmMapping"), "yes");
    }

    @Test
    public void getCaseElementTest() {
        FormValueElement formCaseElement = caseInfoParser
                .getCaseElement(formValueElement);
        assertEquals(formCaseElement.getElementName(), "case");
        assertEquals(formCaseElement.getAttributes().get("case_id"),
                "1a4cc82e-e0ce-451b-be9e-8cd26ca7e921");
        assertEquals(formCaseElement.getAttributes().get("user_id"),
                "a6e563e92d955b5bf904c47af614b9a1");
    }

    @Test
    public void getSubCaseElementTest() {
        FormValueElement formCaseElement = caseInfoParser
                .getsubcaseElement(formValueElement);
        assertEquals(formCaseElement.getElementName(), "subcase_0");
        assertEquals(formCaseElement.getAttributes().size(), 0);
    }

    private String readFile(File xmlfile) {
        FileReader file = null;
        String returnValue = "";
        try {
            file = new FileReader(xmlfile);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                returnValue += line + "\n";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
        return returnValue;

    }

    private Multimap<String, Map<String, Object>> convertToMap(
            Multimap<String, FormValueElement> subElements) {
        Multimap<String, Map<String, Object>> elements = LinkedHashMultimap
                .create();

        for (Map.Entry<String, FormValueElement> entry : subElements.entries()) {
            Map<String, Object> elementAsMap = new HashMap<>(4);
            FormValueElement formValueElement = entry.getValue();

            elementAsMap.put(ELEMENT_NAME, formValueElement.getElementName());
            elementAsMap.put(SUB_ELEMENTS, convertToMap(formValueElement
                    .getSubElements()));
            elementAsMap.put(ATTRIBUTES, formValueElement.getAttributes());
            elementAsMap.put(VALUE, formValueElement.getValue());

            elements.put(entry.getKey(), elementAsMap);
        }

        return elements;
    }
}
