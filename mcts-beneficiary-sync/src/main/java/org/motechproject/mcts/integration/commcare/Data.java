package org.motechproject.mcts.integration.commcare;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class Data {
    private String xmlns;

    private List<Case> cases;

    public List<Case> getCases() {
        return cases;
    }

    @XmlElement(name = "case")
    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    private Meta meta;

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
