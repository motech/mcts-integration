package org.motechproject.mcts.integration.commcare;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class UpdateData {
    private String xmlns;
    private Meta meta;
    private Case caseTask;

    public Case getCaseTask() {
        return caseTask;
    }

    @XmlElement(name = "case")
    public void setCaseTask(Case caseTask) {
        this.caseTask = caseTask;
    }

    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name = "xmlns")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public Meta getMeta() {
        return meta;
    }

    @XmlElement(name = "meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
