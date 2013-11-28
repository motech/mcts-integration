package mcts.integration.beneficiary.sync.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Beneficiary {

    private String mctsId;
    private Integer serviceType;

    public Beneficiary(String mctsId, Integer serviceType) {
        this.mctsId = mctsId;
        this.serviceType = serviceType;
    }

    public String getMctsId() {
        return mctsId;
    }

    public Integer getServiceType() {
        return serviceType;
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
