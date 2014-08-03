package org.motechproject.mcts.integration.commcare;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "case")
public class Case {

    private int mctsPregnantMotherId;

    public int getMctsPregnantMotherId() {
        return mctsPregnantMotherId;
    }

    @XmlTransient
    public void setMctsPregnantMotherId(int mctsPregnantMotherId) {
        this.mctsPregnantMotherId = mctsPregnantMotherId;
    }

    private CreateTask createTask;

    private UpdateTask updateTask;

    private String caseId;

    private String userId;

    private String dateModified;

    private String xmlns;

    public CreateTask getCreateTask() {
        return createTask;
    }

    @XmlElement(name = "create")
    public void setCreateTask(CreateTask createTask) {
        this.createTask = createTask;
    }

    public UpdateTask getUpdateTask() {
        return updateTask;
    }

    @XmlElement(name = "update")
    public void setUpdateTask(UpdateTask updateTask) {
        this.updateTask = updateTask;
    }

    public String getCaseId() {
        return caseId;
    }

    @XmlAttribute(name = "case_id")
    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserId() {
        return userId;
    }

    @XmlAttribute(name = "user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateModified() {
        return dateModified;
    }

    @XmlAttribute(name = "date_modified")
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name = "xmlns")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

}
