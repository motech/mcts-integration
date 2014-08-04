package org.motechproject.mcts.integration.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Beneficiary {

    private Integer mctsPregnantMotherId;
    private String mctsId;
    private Integer serviceType;
    private Date serviceDeliveryDate;
    private String mobileNumber;
    private Integer anc1HBLevel;
    private Integer anc2HBLevel;
    private Integer anc3HBLevel;
    private Integer anc4HBLevel;
    private String hbLevelStr = "";

    public Integer getAnc1HBLevel() {
        return anc1HBLevel;
    }

    public void setAnc1HBLevel(Integer anc1hbLevel) {
        anc1HBLevel = anc1hbLevel;
    }

    public Integer getAnc2HBLevel() {
        return anc2HBLevel;
    }

    public void setAnc2HBLevel(Integer anc2hbLevel) {
        anc2HBLevel = anc2hbLevel;
    }

    public Integer getAnc3HBLevel() {
        return anc3HBLevel;
    }

    public void setAnc3HBLevel(Integer anc3hbLevel) {
        anc3HBLevel = anc3hbLevel;
    }

    public Integer getAnc4HBLevel() {
        return anc4HBLevel;
    }

    public void setAnc4HBLevel(Integer anc4hbLevel) {
        anc4HBLevel = anc4hbLevel;
    }

    public String getHbLevelStr() {
        return hbLevelStr;
    }

    public void setHbLevelStr(String hbLevelStr) {
        this.hbLevelStr = hbLevelStr;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Beneficiary(Integer mctsPregnantMotherId, String mctsId,
            Integer serviceType, Date serviceDeliveryDate, String mobileNumber,
            Integer anc1HBLevel, Integer anc2HBLevel, Integer anc3HBLevel,
            Integer anc4HBLevel) {
        this.mctsPregnantMotherId = mctsPregnantMotherId;
        this.mctsId = mctsId;
        this.serviceType = serviceType;
        this.serviceDeliveryDate = serviceDeliveryDate;
        this.mobileNumber = mobileNumber;
        this.anc1HBLevel = anc1HBLevel;
        Integer hbLevel = null;
        if (serviceType == 2) {
            hbLevel = anc2HBLevel;
        } else if (serviceType == 3) {
            hbLevel = anc3HBLevel;
        } else if (serviceType == 4) {
            hbLevel = anc4HBLevel;
        }

        if (hbLevel != null) {
            if (hbLevel < 7 && hbLevel > 0) {
                hbLevelStr = "3";
            } else if (hbLevel >= 7 && hbLevel < 11) {
                hbLevelStr = "2";
            } else if (hbLevel >= 11) {
                hbLevelStr = "1";
            }
        }
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
