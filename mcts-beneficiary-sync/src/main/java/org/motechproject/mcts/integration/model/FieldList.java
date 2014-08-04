package org.motechproject.mcts.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("restriction")
@XmlRootElement(name = "field_list")
public class FieldList {
    private String fieldValue;
    private java.util.Properties properties;

    public java.util.Properties getProperties() {
        return properties;
    }

    public void setProperties(java.util.Properties properties) {
        this.properties = properties;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    @JsonProperty("field_value")
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
