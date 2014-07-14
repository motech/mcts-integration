package org.motechproject.mcts.integration.hibernate.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mcts_pregnant_mother_error_log", schema = "report")
public class MctsPregnantMotherErrorLog implements java.io.Serializable {

	private Integer id;
	private String stateID;
	private String stateName;
	private String districtID;
	private String districtName;
	private String blockID;
	private String blockName;
	private String tehsilID;
	private String tehsilName;
	private String facilityID;
	private String facilityName;
	private String subCentreID;
	private String subCentreName;
	private String town;
	private String villageID;
	private String villageName;
	private String aNMID;
	private String aSHAID;
	private String ward;
	private String beneficiaryType;
	private String beneficiaryID;
	private String beneficiaryName;
	private String uIDNumber;
	private String eIDNumber;
	private String birthdate;
	private String gender;
	private String fatherHusbandName;
	private String beneficiaryAddress;
	private String pinCode;
	private String category;
	private String economicStatus;
	private String mobileno;
	private String email;
	private String lMPDate;
	private Date creationTime;
	
	public MctsPregnantMotherErrorLog() {
	}

	public MctsPregnantMotherErrorLog(Integer id, String stateID,
			String stateName, String districtID, String districtName,
			String blockID, String blockName, String tehsilID,
			String tehsilName, String facilityID, String facilityName,
			String subCentreID, String subCentreName, String town,
			String villageID, String villageName, String aNMID, String aSHAID,
			String ward, String beneficiaryType, String beneficiaryID,
			String beneficiaryName, String uIDNumber, String eIDNumber,
			String birthdate, String gender, String fatherHusbandName,
			String beneficiaryAddress, String pinCode, String category,
			String economicStatus, String mobileno, String email, String lMPDate) {
		super();
		this.id = id;
		this.stateID = stateID;
		this.stateName = stateName;
		this.districtID = districtID;
		this.districtName = districtName;
		this.blockID = blockID;
		this.blockName = blockName;
		this.tehsilID = tehsilID;
		this.tehsilName = tehsilName;
		this.facilityID = facilityID;
		this.facilityName = facilityName;
		this.subCentreID = subCentreID;
		this.subCentreName = subCentreName;
		this.town = town;
		this.villageID = villageID;
		this.villageName = villageName;
		this.aNMID = aNMID;
		this.aSHAID = aSHAID;
		this.ward = ward;
		this.beneficiaryType = beneficiaryType;
		this.beneficiaryID = beneficiaryID;
		this.beneficiaryName = beneficiaryName;
		this.uIDNumber = uIDNumber;
		this.eIDNumber = eIDNumber;
		this.birthdate = birthdate;
		this.gender = gender;
		this.fatherHusbandName = fatherHusbandName;
		this.beneficiaryAddress = beneficiaryAddress;
		this.pinCode = pinCode;
		this.category = category;
		this.economicStatus = economicStatus;
		this.mobileno = mobileno;
		this.email = email;
		this.lMPDate = lMPDate;
	}



	@SequenceGenerator(name = "generator", sequenceName = "mcts_pregnant_mother_id_seq")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "state_id")
	public String getStateID() {
		return stateID;
	}

	public void setStateID(String stateID) {
		this.stateID = stateID;
	}

	@Column(name = "state_name")
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Column(name = "district_id")
	public String getDistrictID() {
		return districtID;
	}

	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}

	@Column(name = "district_name")
	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	@Column(name = "block_id")
	public String getBlockID() {
		return blockID;
	}

	public void setBlockID(String blockID) {
		this.blockID = blockID;
	}

	@Column(name = "block_name")
	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	@Column(name = "tehsil_id")
	public String getTehsilID() {
		return tehsilID;
	}

	public void setTehsilID(String tehsilID) {
		this.tehsilID = tehsilID;
	}

	@Column(name = "tehsil_name")
	public String getTehsilName() {
		return tehsilName;
	}

	public void setTehsilName(String tehsilName) {
		this.tehsilName = tehsilName;
	}

	@Column(name = "facility_id")
	public String getFacilityID() {
		return facilityID;
	}

	public void setFacilityID(String facilityID) {
		this.facilityID = facilityID;
	}

	@Column(name = "facility_name")
	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	@Column(name = "sub_center_id")
	public String getSubCentreID() {
		return subCentreID;
	}

	public void setSubCentreID(String subCentreID) {
		this.subCentreID = subCentreID;
	}

	@Column(name = "sub_center_name")
	public String getSubCentreName() {
		return subCentreName;
	}

	public void setSubCentreName(String subCentreName) {
		this.subCentreName = subCentreName;
	}

	@Column(name = "town")
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Column(name = "village_id")
	public String getVillageID() {
		return villageID;
	}

	public void setVillageID(String villageID) {
		this.villageID = villageID;
	}

	@Column(name = "village_name")
	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	@Column(name = "anm_id")
	public String getaNMID() {
		return aNMID;
	}

	public void setaNMID(String aNMID) {
		this.aNMID = aNMID;
	}

	@Column(name = "asha_id")
	public String getaSHAID() {
		return aSHAID;
	}

	public void setaSHAID(String aSHAID) {
		this.aSHAID = aSHAID;
	}

	@Column(name = "ward")
	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	@Column(name = "beneficiary_type")
	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	@Column(name = "beneficiary_id")
	public String getBeneficiaryID() {
		return beneficiaryID;
	}

	public void setBeneficiaryID(String beneficiaryID) {
		this.beneficiaryID = beneficiaryID;
	}

	@Column(name = "beneficiary_name")
	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	@Column(name = "uuid_number")
	public String getuIDNumber() {
		return uIDNumber;
	}

	public void setuIDNumber(String uIDNumber) {
		this.uIDNumber = uIDNumber;
	}

	@Column(name = "eid_number")
	public String geteIDNumber() {
		return eIDNumber;
	}

	public void seteIDNumber(String eIDNumber) {
		this.eIDNumber = eIDNumber;
	}

	@Column(name = "birth_date")
	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "father_husband_name")
	public String getFatherHusbandName() {
		return fatherHusbandName;
	}

	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	@Column(name = "beneficiary_address")
	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	@Column(name = "pin_code")
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	@Column(name = "category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "economic_status")
	public String getEconomicStatus() {
		return economicStatus;
	}

	public void setEconomicStatus(String economicStatus) {
		this.economicStatus = economicStatus;
	}

	@Column(name = "mobile_no")
	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "lmp_date")
	public String getlMPDate() {
		return lMPDate;
	}

	public void setlMPDate(String lMPDate) {
		this.lMPDate = lMPDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_time", length = 29)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
