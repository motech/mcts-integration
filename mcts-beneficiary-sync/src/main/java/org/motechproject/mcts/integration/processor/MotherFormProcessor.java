package org.motechproject.mcts.integration.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.parser.CaseInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotherFormProcessor {

    private static final Logger logger = LoggerFactory
            .getLogger("mcts-form-processor");

    private static final String FORM_NAME_ATTRIBUTE = "name";
    private static final String FORM_XMLNS_ATTRIBUTE = "xmlns";
    @Autowired
    CaseInfoParser infoParser;
    @Autowired
    FormsProcessor formsProcessor;

    public MotherFormProcessor() {

    }

    public void process(CommcareForm commcareForm) throws BeneficiaryException {

        final Map<String, String> formAttributes = commcareForm.getForm()
                .getAttributes();
        String formName = formAttributes.get(FORM_NAME_ATTRIBUTE);
        String xmlns = formAttributes.get(FORM_XMLNS_ATTRIBUTE);
        logger.info(String.format(
                "Received form. id: %s, type: %s; xmlns: %s;",
                commcareForm.getId(), formName, xmlns));

        Map<String, String> motherForm = parseMotherForm(commcareForm);
        formsProcessor.processForm(motherForm);

    }

    private Map<String, String> parseMotherForm(CommcareForm commcareForm)
            throws BeneficiaryException {
        String namespace = this.getNamespace(commcareForm);
        String appVersion = this.getAppVersion(commcareForm);
        Map<String, String> motherInfo = new HashMap<>();
        Map<String, String> formFields = parse(commcareForm.getForm(),
                commcareForm);
        if (formFields == null) {
            return null;
        }
        motherInfo.putAll(formFields);
        motherInfo.put("appVersion", appVersion);
        motherInfo.put("namespace", namespace);
        return motherInfo;
    }

    private Map<String, String> parse(FormValueElement startElement,
            CommcareForm commcareForm) throws BeneficiaryException {
        FormValueElement caseElement = startElement;
        if (!startElement.getElementName().equals("case")) {
            caseElement = infoParser.getCaseElement(startElement);
        }
        FormValueElement attributeElements = startElement;
        if (!startElement.getElementName().equals("subcase_0")) {
            attributeElements = infoParser.getsubcaseElement(startElement);
        }

        if (caseElement == null) {
            return null;
        }
        Map<String, String> infoMap = parseCaseInfo(caseElement, commcareForm);
        if (attributeElements != null) {
            attributeElements = infoParser.getCaseElement(attributeElements);
            infoMap = parseSubcaseInfo(attributeElements, commcareForm, infoMap);
        }
        infoMap.putAll(extractHeaders(commcareForm));
        infoMap.putAll(infoParser.parse(startElement, true));
        return infoMap;
    }

    private Map<String, String> parseSubcaseInfo(
            FormValueElement attributeElements, CommcareForm commcareForm,
            Map<String, String> infoMap) {
        final String caseId = attributeElements.getAttributes().get("case_id");

        if (StringUtils.isEmpty(caseId)) {
            throw new RuntimeException(String.format(
                    "Empty case id found in form(%s)", commcareForm.getId()));
        } else {
            infoMap.put("pregnancyId", caseId);
        }

        return infoMap;
    }

    private Map<String, String> parseCaseInfo(FormValueElement caseElement,
            CommcareForm commcareForm) throws BeneficiaryException {
        Map<String, String> caseInfo = new HashMap<>();
        final String caseId = caseElement.getAttributes().get("case_id");

        if (StringUtils.isEmpty(caseId)) {
            throw new BeneficiaryException(ApplicationErrors.FOUND_EMPTY_STRING);
        } else {
            caseInfo.put("caseId", caseId);
        }

        final String dateModified = caseElement.getAttributes().get(
                "date_modified");
        caseInfo.put("dateModified", dateModified);
        caseInfo.putAll(infoParser.parse(caseElement, true));

        return caseInfo;
    }

    private String getNamespace(CommcareForm commcareForm) {
        return this.getAttribute(commcareForm, "xmlns");
    }

    private String getAttribute(CommcareForm commcareForm, String name) {
        return commcareForm.getForm().getAttributes().get(name);
    }

    private String getAppVersion(CommcareForm commcareForm) {
        return commcareForm.getMetadata().get("appVersion");
    }

    public Map<String, String> extractHeaders(final CommcareForm commcareForm) {
        return new HashMap<String, String>() {
            {
                put("serverDateModified", commcareForm.getReceivedOn());
            }
        };
    }

}
