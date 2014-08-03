package org.motechproject.mcts.integration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("restriction")
@XmlRootElement(name = "group_id")
public class GroupId {
    private List<FieldList> fieldList;

    public List<FieldList> getFieldList() {
        return fieldList;
    }

    @JsonProperty("field_list")
    public void setFieldList(List<FieldList> fieldList) {
        this.fieldList = fieldList;
    }

}
