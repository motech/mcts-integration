package org.motechproject.mcts.integration.commcare;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "update")
public class UpdateTask {
	
	private String caseType;
	
    private String caseName;
	
    private String dateOpened;
	
    private String ownerId;
	
    private String mctsFullname;
	
    private String mctsHusbandName;
	
    private String mctsAge;
	
    private String mctsDob;
	
    private String mctsEdd;
	
    private String mctsId;
	
    private String mctsPhoneNumber;
	
    private String ashaId;
	public String getCaseType() {
		return caseType;
	}
	@XmlElement(name = "case_type")
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseName() {
		return caseName;
	}
	@XmlElement(name = "case_name")
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getDateOpened() {
		return dateOpened;
	}
	@XmlElement(name = "date_opened")
	public void setDateOpened(String dateOpened) {
		this.dateOpened = dateOpened;
	}
	public String getOwnerId() {
		return ownerId;
	}
	@XmlElement(name = "owner_id")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getMctsFullname() {
		return mctsFullname;
	}
	@XmlElement (name = "mcts_full_name")
	public void setMctsFullname(String mctsFullname) {
		this.mctsFullname = mctsFullname;
	}
	public String getMctsHusbandName() {
		return mctsHusbandName;
	}
	@XmlElement (name = "mcts_husband_name")
	public void setMctsHusbandName(String mctsHusbandName) {
		this.mctsHusbandName = mctsHusbandName;
	}
	public String getMctsAge() {
		return mctsAge;
	}
	@XmlElement (name = "mcts_age")
	public void setMctsAge(String mctsAge) {
		this.mctsAge = mctsAge;
	}
	public String getMctsDob() {
		return mctsDob;
	}
	@XmlElement (name = "mcts_dob")
	public void setMctsDob(String mctsDob) {
		this.mctsDob = mctsDob;
	}
	public String getMctsEdd() {
		return mctsEdd;
	}
	@XmlElement (name = "mcts_edd")
	public void setMctsEdd(String mctsEdd) {
		this.mctsEdd = mctsEdd;
	}
	public String getMctsId() {
		return mctsId;
	}
	@XmlElement (name = "mcts_id")
	public void setMctsId(String mctsId) {
		this.mctsId = mctsId;
	}
	public String getMctsPhoneNumber() {
		return mctsPhoneNumber;
	}
	@XmlElement (name = "mcts_phone_number")
	public void setMctsPhoneNumber(String mctsPhoneNumber) {
		this.mctsPhoneNumber = mctsPhoneNumber;
	}
	public String getAshaId() {
		return ashaId;
	}
	@XmlElement (name = "asha_id")
	public void setAshaId(String ashaId) {
		this.ashaId = ashaId;
	}
    
}
