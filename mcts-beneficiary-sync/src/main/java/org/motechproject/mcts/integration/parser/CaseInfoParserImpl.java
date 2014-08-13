package org.motechproject.mcts.integration.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.mcts.utils.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;

@Component
public class CaseInfoParserImpl implements CaseInfoParser {

    @JsonProperty("caseElementPath")
    private String caseElementPath = "//case";

    @JsonProperty("subcaseElementPath")
    private String subcaseElementPath = "//subcase_0";

    @JsonProperty("keyConversionMap")
    private Map<String, String> keyConversionMap = new HashMap<>();

    @JsonProperty("convertToCamelCase")
    private boolean convertToCamelCase = true;

    @Override
    public Map<String, String> parse(FormValueElement element) {
        return parse(element, false);
    }

    @Override
    public Map<String, String> parse(FormValueElement element,
            boolean isRecursive) {

        Map<String, String> mapper = new HashMap<>();
        Multimap<String, FormValueElement> subElements = element
                .getSubElements();

        if (empty(subElements)) {
            return mapper;
        }

        Map<String, Collection<FormValueElement>> subElementsMap = subElements
                .asMap();
        for (Map.Entry<String, Collection<FormValueElement>> subElement : subElementsMap
                .entrySet()) {

            String key = subElement.getKey();
            if(!key.equals("anm")) {
            if (isRecursive) {
                for (FormValueElement formValueElement : subElement.getValue()) {
                    mapper.putAll(parse(formValueElement, isRecursive));
                }
            }
           }

            key = applyConversions(key);
            mapper.put(key, getFormValueElementValue(subElement));
        }
        return mapper;
    }

    @Override
    public FormValueElement getCaseElement(FormValueElement startElement) {
        return (FormValueElement) startElement.searchFirst(caseElementPath);
    }

    @Override
    public FormValueElement getsubcaseElement(FormValueElement startElement) {
        return (FormValueElement) startElement.searchFirst(subcaseElementPath);
    }

    private boolean empty(Multimap<?, ?> subElements) {
        return null == subElements || subElements.size() == 0;
    }

    private String applyConversions(String key) {

        String param;
        param = applyKeyConversionMap(key);
        param = applyCamelConversion(param);
        return param;
    }

    private String applyKeyConversionMap(String keyValue) {
        return keyConversionMap.containsKey(keyValue) ? keyConversionMap
                .get(keyValue) : keyValue;
    }

    private String applyCamelConversion(String input) {
        return convertToCamelCase ? StringUtils.toCamelCase(input) : input;
    }

    private String getFormValueElementValue(
            Map.Entry<String, Collection<FormValueElement>> subElement) {
        FormValueElement fieldValue = getFormValueElement(subElement);
        return fieldValue.getValue();
    }

    private FormValueElement getFormValueElement(
            Map.Entry<String, Collection<FormValueElement>> subElement) {
        Collection<FormValueElement> subElementValue = subElement.getValue();
        return (FormValueElement) CollectionUtils.get(subElementValue, 0);
    }

}
