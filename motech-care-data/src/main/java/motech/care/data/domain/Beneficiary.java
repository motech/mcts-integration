package motech.care.data.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

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
	private String hbLevelStr ="";
	
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
		Integer hbLevel = anc4HBLevel;
		if (hbLevel == null) {
			hbLevel = anc3HBLevel;
			if(hbLevel == null) {
				hbLevel = anc2HBLevel;
				if(hbLevel == null) {
					hbLevel = anc1HBLevel;
				}
			}
		}
		if (hbLevel != null) {
			hbLevelStr = String.valueOf(hbLevel);
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
