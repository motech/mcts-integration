package org.motechproject.mcts.integration.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Records")
public class Record {

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

	public Record() {
		super();
	}

	@XmlElement(name = "StateID")
	public String getStateID() {
		return stateID;
	}

	@XmlElement(name = "State_Name")
	public String getStateName() {
		return stateName;
	}

	@XmlElement(name = "District_ID")
	public String getDistrictID() {
		return districtID;
	}

	@XmlElement(name = "District_Name")
	public String getDistrictName() {
		return districtName;
	}

	@XmlElement(name = "Block_ID")
	public String getBlockID() {
		return blockID;
	}

	@XmlElement(name = "Block_Name")
	public String getBlockName() {
		return blockName;
	}

	@XmlElement(name = "Tehsil_ID")
	public String getTehsilID() {
		return tehsilID;
	}

	@XmlElement(name = "Tehsil_Name")
	public String getTehsilName() {
		return tehsilName;
	}

	@XmlElement(name = "Facility_ID")
	public String getFacilityID() {
		return facilityID;
	}

	@XmlElement(name = "Facility_Name")
	public String getFacilityName() {
		return facilityName;
	}

	@XmlElement(name = "SubCentre_ID")
	public String getSubCentreID() {
		return subCentreID;
	}

	@XmlElement(name = "SubCentre_Name")
	public String getSubCentreName() {
		return subCentreName;
	}

	@XmlElement(name = "Town")
	public String getTown() {
		return town;
	}

	@XmlElement(name = "Village_ID")
	public String getVillageID() {
		return villageID;
	}

	@XmlElement(name = "Village_Name")
	public String getVillageName() {
		return villageName;
	}

	@XmlElement(name = "ANM_ID")
	public String getANMID() {
		return aNMID;
	}

	@XmlElement(name = "ASHA_ID")
	public String getASHAID() {
		return aSHAID;
	}

	@XmlElement(name = "Ward")
	public String getWard() {
		return ward;
	}

	@XmlElement(name = "Beneficiary_Type")
	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	@XmlElement(name = "Beneficiary_ID")
	public String getBeneficiaryID() {
		return beneficiaryID;
	}

	@XmlElement(name = "Beneficiary_Name")
	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	@XmlElement(name = "UID_Number")
	public String getUIDNumber() {
		return uIDNumber;
	}

	@XmlElement(name = "EID_Number")
	public String getEIDNumber() {
		return eIDNumber;
	}

	@XmlElement(name = "Birthdate")
	public String getBirthdate() {
		return birthdate;
	}

	@XmlElement(name = "Gender")
	public String getGender() {
		return gender;
	}

	@XmlElement(name = "FatherHusbandName")
	public String getFatherHusbandName() {
		return fatherHusbandName;
	}

	@XmlElement(name = "BeneficiaryAddress")
	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	@XmlElement(name = "PinCode")
	public String getPinCode() {
		return pinCode;
	}

	@XmlElement(name = "Category")
	public String getCategory() {
		return category;
	}

	@XmlElement(name = "Economic_Status")
	public String getEconomicStatus() {
		return economicStatus;
	}

	@XmlElement(name = "Mobile_no")
	public String getMobileno() {
		return mobileno;
	}

	@XmlElement(name = "Email")
	public String getEmail() {
		return email;
	}

	@XmlElement(name = "LMP_Date")
	public String getLMPDate() {
		return lMPDate;
	}

	public void setStateID(String stateID) {
		this.stateID = stateID;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public void setBlockID(String blockID) {
		this.blockID = blockID;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public void setTehsilID(String tehsilID) {
		this.tehsilID = tehsilID;
	}

	public void setTehsilName(String tehsilName) {
		this.tehsilName = tehsilName;
	}

	public void setFacilityID(String facilityID) {
		this.facilityID = facilityID;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public void setSubCentreID(String subCentreID) {
		this.subCentreID = subCentreID;
	}

	public void setSubCentreName(String subCentreName) {
		this.subCentreName = subCentreName;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setVillageID(String villageID) {
		this.villageID = villageID;
	}

	// public void setRecords(List<Record> records) {
	// this.records = records;
	// }
	//

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public void setANMID(String aNMID) {
		this.aNMID = aNMID;
	}

	public void setASHAID(String aSHAID) {
		this.aSHAID = aSHAID;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public void setBeneficiaryID(String beneficiaryID) {
		this.beneficiaryID = beneficiaryID;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public void setUIDNumber(String uIDNumber) {
		this.uIDNumber = uIDNumber;
	}

	public void setEIDNumber(String eIDNumber) {
		this.eIDNumber = eIDNumber;
	}

	public void setBirthdate(String birthdate){
		Date date;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(birthdate);
			this.birthdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			//TODO
		}

	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setEconomicStatus(String economicStatus) {
		this.economicStatus = economicStatus;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLMPDate(String lMPDate){
		Date date;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(lMPDate);
			this.lMPDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		} catch (ParseException e) {
			//TODO
		}

	}

	public Record(String stateID, String stateName, String districtID,
			String districtName, String blockID, String blockName,
			String tehsilID, String tehsilName, String facilityID,
			String facilityName, String subCentreID, String subCentreName,
			String town, String villageID, String villageName, String aNMID,
			String aSHAID, String ward, String beneficiaryType,
			String beneficiaryID, String beneficiaryName, String uIDNumber,
			String eIDNumber, String birthdate, String gender,
			String fatherHusbandName, String beneficiaryAddress,
			String pinCode, String category, String economicStatus,
			String mobileno, String email, String lMPDate) {
		super();
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

	@Override
	public String toString() {
		return String
				.format("stateID: %s, stateName: %s, districtID: %s, districtName: %s, blockID: %s, blockName: %s, tehsilID: %s, tehsilName: %s, facilityID: %s, facilityName: %s, subCentreID: %s, subCentreName: %s, town: %s, villageID: %s, villageName: %s, aNMID: %s, aSHAID: %s, ward: %s, beneficiaryType: %s, beneficiaryID: %s, beneficiaryName: %s, uIDNumber: %s, eIDNumber: %s, birthdate: %s, gender: %s, fatherHusbandName: %s, beneficiaryAddress: %s, pinCode: %s, category: %s, economicStatus: %s, mobileno: %s, email: %s, lMPDate: %s",
						stateID, stateName, districtID, districtName, blockID,
						blockName, tehsilID, tehsilName, facilityID,
						facilityName, subCentreID, subCentreName, town,
						villageID, villageName, aNMID, aSHAID, ward,
						beneficiaryType, beneficiaryID, beneficiaryName,
						uIDNumber, eIDNumber, birthdate, gender,
						fatherHusbandName, beneficiaryAddress, pinCode,
						category, economicStatus, mobileno, email, lMPDate);
	}

	@Override
	public boolean equals(Object object) {
		Record record = (Record) object;
		if (record.getANMID() == this.getANMID()
				&& record.getASHAID() == this.getASHAID()
				&& record.getBeneficiaryAddress() == this
						.getBeneficiaryAddress()
				&& record.getBeneficiaryID() == this.getBeneficiaryID()
				&& record.getBeneficiaryName() == this.getBeneficiaryName()
				&& record.getBeneficiaryType() == this.getBeneficiaryType()
				&& record.getBirthdate() == this.getBirthdate()
				&& record.getBlockID() == this.getBlockID()
				&& record.getBlockName() == this.getBlockName()
				&& record.getCategory() == this.getCategory()
				&& record.getDistrictID() == this.getDistrictID()
				&& record.getDistrictName() == this.getDistrictName()
				&& record.getEconomicStatus() == this.getEconomicStatus()
				&& record.getEIDNumber() == this.getEIDNumber()
				&& record.getEmail() == this.getEmail()
				&& record.getFacilityID() == this.getFacilityID()
				&& record.getFacilityName() == this.getFacilityName()
				&& record.getFatherHusbandName() == this.getFatherHusbandName()
				&& record.getGender() == this.getGender()
				&& record.getLMPDate() == this.getLMPDate()
				&& record.getMobileno() == this.getMobileno()
				&& record.getPinCode() == this.getPinCode()
				&& record.getStateID() == this.getStateID()
				&& record.getStateName() == this.getStateName()
				&& record.getSubCentreID() == this.getSubCentreID()
				&& record.getSubCentreName() == this.getSubCentreName()
				&& record.getTehsilID() == this.getTehsilID()
				&& record.getTehsilName() == this.getTehsilName()
				&& record.getTown() == this.getTown()
				&& record.getUIDNumber() == this.getUIDNumber()
				&& record.getVillageID() == this.getVillageID()
				&& record.getVillageName() == this.getVillageName()
				&& record.getWard() == this.getWard()) {
			return true;
		}
		return true;
	}
}
