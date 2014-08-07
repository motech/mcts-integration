package org.motechproject.mcts.integration.commcare;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "closed")
public class CloseData {
    private String xmlns;
    private List<Case> cases;
    private Meta meta;

    @XmlElement(name = "case")
    public void setCases(List<Case> cases) {
        this.cases = cases;
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

    public String getXmlns() {
        return xmlns;
    }

    public List<Case> getCases() {
        return cases;
    }

}
