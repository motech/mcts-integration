/*Object to post data updates received from MCTS to Subscribers*/

package org.motechproject.mcts.integration.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BeneficiaryUpdate")
public class BeneficiaryUpdateDTO {

	private int mctsSubcenter;
	private int ashaWorkerId;
	private String ashaWorkerName;
	private int anmWorkerId;
	private String anmWorkerName;
	private int motherCaseId;
	private int mctsVillage;
	private String mctsId;
	private String name;
	private String type;
	private Date birthDate;
	private Character gender;
	private String fatherHusbandName;
	private String email;
	private String mobileNo;
	private String economicStatus;
	private String category;
	private String beneficiaryAddress;
	private String uidNumber;
	private String pincode;
	private Date lmpDate;
	private String eidNumber;
	private String ward;
	private String town;

	public BeneficiaryUpdateDTO() {
	}

	@XmlElement(name = "subCenter")
	public void setMctsSubcenter(int mctsSubcenter) {
		this.mctsSubcenter = mctsSubcenter;
	}

	@XmlElement(name = "ashaWorkerId")
	public void setAshaWorkerId(int ashaWorkerId) {
		this.ashaWorkerId = ashaWorkerId;
	}

	@XmlElement(name = "ashaWorkerName")
	public void setAshaWorkerName(String ashaWorkerName) {
		this.ashaWorkerName = ashaWorkerName;
	}

	@XmlElement(name = "anmWorkerId")
	public void setAnmWorkerId(int anmWorkerId) {
		this.anmWorkerId = anmWorkerId;
	}

	@XmlElement(name = "anmWorkerName")
	public void setAnmWorkerName(String anmWorkerName) {
		this.anmWorkerName = anmWorkerName;
	}

	@XmlElement(name = "motherCaseId")
	public void setMotherCaseId(int motherCaseId) {
		this.motherCaseId = motherCaseId;
	}

	@XmlElement(name = "village")
	public void setMctsVillage(int mctsVillage) {
		this.mctsVillage = mctsVillage;
	}

	@XmlElement(name = "mctsId")
	public void setMctsId(String mctsId) {
		this.mctsId = mctsId;
	}

	@XmlElement(name = "beneficiaryName")
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "type")
	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "birthDate")
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@XmlElement(name = "gender")
	public void setGender(Character gender) {
		this.gender = gender;
	}

	@XmlElement(name = "fatherHusbandName")
	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	@XmlElement(name = "email")
	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "mobileNo")
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@XmlElement(name = "economicStatus")
	public void setEconomicStatus(String economicStatus) {
		this.economicStatus = economicStatus;
	}

	@XmlElement(name = "category")
	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement(name = "beneficiaryAddress")
	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	@XmlElement(name = "uidNumber")
	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	@XmlElement(name = "pincode")
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@XmlElement(name = "lmpDate")
	public void setLmpDate(Date lmpDate) {
		this.lmpDate = lmpDate;
	}

	@XmlElement(name = "eidNumber")
	public void setEidNumber(String eidNumber) {
		this.eidNumber = eidNumber;
	}

	@XmlElement(name = "ward")
	public void setWard(String ward) {
		this.ward = ward;
	}

	@XmlElement(name = "town")
	public void setTown(String town) {
		this.town = town;
	}

	public int getMctsSubcenter() {
		return mctsSubcenter;
	}

	public int getAshaWorkerId() {
		return ashaWorkerId;
	}

	public String getAshaWorkerName() {
		return ashaWorkerName;
	}

	public int getAnmWorkerId() {
		return anmWorkerId;
	}

	public String getAnmWorkerName() {
		return anmWorkerName;
	}

	public int getMotherCaseId() {
		return motherCaseId;
	}

	public int getMctsVillage() {
		return mctsVillage;
	}

	public String getMctsId() {
		return mctsId;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public Character getGender() {
		return gender;
	}

	public String getFatherHusbandName() {
		return fatherHusbandName;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public String getEconomicStatus() {
		return economicStatus;
	}

	public String getCategory() {
		return category;
	}

	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public String getPincode() {
		return pincode;
	}

	public Date getLmpDate() {
		return lmpDate;
	}

	public String getEidNumber() {
		return eidNumber;
	}

	public String getWard() {
		return ward;
	}

	public String getTown() {
		return town;
	}

}
