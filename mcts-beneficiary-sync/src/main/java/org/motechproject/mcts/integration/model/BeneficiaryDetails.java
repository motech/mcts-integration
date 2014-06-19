package org.motechproject.mcts.integration.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlRootElement
public class BeneficiaryDetails {
    private static final Integer MCTS_REQUEST_MODE = 4;
    private static final Integer MCTS_REQUEST_DAY_OFFSET = 0;
    private static final String DEFAULT_DATE_PATTERN = "dd-MM-yyyy";

    @XmlElement(name = "StateId")
    private Integer stateId;

    @XmlElement(name = "ID")
    private String mctsId;

    @XmlElement(name = "ServiceType")
    private Integer serviceType;

    @XmlElement(name = "Day")
    private Integer dayOffset;

    @XmlElement(name = "AsOn_date")
    private String asOnDate;

    @XmlElement(name = "Remarks")
    private String remarks;

    @XmlElement(name = "Mode")
    private Integer mode;
    
    @XmlElement(name="HBLevel",required=true)
    private String hbLevel;
    
   	@XmlElement(name="ContactNo")
    private String contactNo;
    
    public BeneficiaryDetails() {
    }

    public BeneficiaryDetails(Integer stateId, String mctsId, Integer serviceType, Date asOnDate, String contactNo, String hbLevelStr) {
        this.stateId = stateId;
        this.mctsId = mctsId;
        this.serviceType = serviceType;
        this.asOnDate = asOnDate != null ? new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(asOnDate) : null;
        this.dayOffset = MCTS_REQUEST_DAY_OFFSET;
        this.contactNo = contactNo;
        this.hbLevel = hbLevelStr;
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
