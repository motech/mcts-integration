package org.motechproject.mcts.integration.commcare;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "create")
public class CreateTask {
	
	private String caseType;
    private String ownerId;
    private String caseName;
	public String getCaseType() {
		return caseType;
	}
	@XmlElement(name ="case_type")
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getOwnerId() {
		return ownerId;
	}
	@XmlElement(name = "owner_id")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getCaseName() {
		return caseName;
	}
	 @XmlElement(name = "case_name")
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

}
