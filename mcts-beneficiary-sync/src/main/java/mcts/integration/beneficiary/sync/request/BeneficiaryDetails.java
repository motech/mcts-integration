package mcts.integration.beneficiary.sync.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeneficiaryDetails {
    public static final int MCTS_REQUEST_MODE = 4;
    public static final int MCTS_REQUEST_DAY_OFFSET = 0;

    @XmlElement(name = "StateId")
    private Integer stateId;

    @XmlElement(name = "ID")
    private String mctsId;

    @XmlElement(name = "ServiceType")
    private Integer serviceType;

    @XmlElement(name = "Day")
    private Integer dayOffset;

    @XmlElement(name = "Mode")
    private Integer mode;

    public BeneficiaryDetails() {
    }

    public BeneficiaryDetails(Integer stateId, String mctsId, Integer serviceType) {
        this.stateId = stateId;
        this.mctsId = mctsId;
        this.serviceType = serviceType;
        this.dayOffset = MCTS_REQUEST_DAY_OFFSET;
        this.mode = MCTS_REQUEST_MODE;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
