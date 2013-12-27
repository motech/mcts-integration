package motech.care.data.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Beneficiary {

    private Integer mctsPregnantMotherId;
    private String mctsId;
    private Integer serviceType;
    private Date serviceDeliveryDate;

    public Beneficiary(Integer mctsPregnantMotherId, String mctsId, Integer serviceType, Date serviceDeliveryDate) {
        this.mctsPregnantMotherId = mctsPregnantMotherId;
        this.mctsId = mctsId;
        this.serviceType = serviceType;
        this.serviceDeliveryDate = serviceDeliveryDate;
    }

    public Integer getMctsPregnantMotherId() {
        return mctsPregnantMotherId;
    }

    public String getMctsId() {
        return mctsId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public Date getServiceDeliveryDate() {
        return serviceDeliveryDate;
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
