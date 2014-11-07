package org.motechproject.mcts.integration.processor;

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

import org.motechproject.commcare.builder.CommcareFormBuilder;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.event.MotechEvent;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;


public class BaseTest {

    private FullFormParser parser = null;
    private FormValueElement formValueElement = null;
    private Map<String, Object> parameters = new HashMap<>();
    private MotechEvent motechEvent = null;
    private CommcareForm commcareForm = null;
    
    protected CommcareForm getFormElement(String xmlPath) throws FullFormParserException {
        parser = new FullFormParser(readFile(new File(xmlPath)));
        formValueElement = parser.parse();
        parameters.put(VALUE, formValueElement.getValue());
        parameters.put(ELEMENT_NAME, formValueElement.getElementName());
        parameters.put(ATTRIBUTES, formValueElement.getAttributes());
        parameters.put(SUB_ELEMENTS,
                convertToMap(formValueElement.getSubElements()));

        if (FORM.equals(formValueElement.getElementName())) {
            motechEvent = new MotechEvent(FORMS_EVENT, parameters);
        }
        commcareForm = new CommcareFormBuilder().buildFrom(motechEvent);

        return commcareForm;

    }
    
    protected Multimap<String, Map<String, Object>> convertToMap(
            Multimap<String, FormValueElement> subElements) {
        Multimap<String, Map<String, Object>> elements = LinkedHashMultimap.create();

        for (Map.Entry<String, FormValueElement> entry : subElements.entries()) {
            Map<String, Object> elementAsMap = new HashMap<>(4);
            FormValueElement formValueElement = entry.getValue();

            elementAsMap.put(ELEMENT_NAME, formValueElement.getElementName());
            elementAsMap.put(SUB_ELEMENTS,
                    convertToMap(formValueElement.getSubElements()));
            elementAsMap.put(ATTRIBUTES, formValueElement.getAttributes());
            elementAsMap.put(VALUE, formValueElement.getValue());

            elements.put(entry.getKey(), elementAsMap);
        }

        return elements;
    }
    
    protected String readFile(File xmlfile) {
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
}
