package org.motechproject.mcts.integration.mds.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MctsPregnantMotherErrorLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3154643651482435499L;
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

    @Field
    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    @Field
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Field 
    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    @Field
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Field 
    public String getBlockID() {
        return blockID;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    @Field
    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    @Field 
    public String getTehsilID() {
        return tehsilID;
    }

    public void setTehsilID(String tehsilID) {
        this.tehsilID = tehsilID;
    }

    @Field
    public String getTehsilName() {
        return tehsilName;
    }

    public void setTehsilName(String tehsilName) {
        this.tehsilName = tehsilName;
    }

    @Field
    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    @Field 
    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    @Field 
    public String getSubCentreID() {
        return subCentreID;
    }

    public void setSubCentreID(String subCentreID) {
        this.subCentreID = subCentreID;
    }

    @Field 
    public String getSubCentreName() {
        return subCentreName;
    }

    public void setSubCentreName(String subCentreName) {
        this.subCentreName = subCentreName;
    }

    @Field 
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Field 
    public String getVillageID() {
        return villageID;
    }

    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }

    @Field 
    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    @Field 
    public String getaNMID() {
        return aNMID;
    }

    public void setaNMID(String aNMID) {
        this.aNMID = aNMID;
    }

    @Field 
    public String getaSHAID() {
        return aSHAID;
    }

    public void setaSHAID(String aSHAID) {
        this.aSHAID = aSHAID;
    }

    @Field 
    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    @Field
    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    @Field 
    public String getBeneficiaryID() {
        return beneficiaryID;
    }

    public void setBeneficiaryID(String beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    @Field 
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    @Field 
    public String getuIDNumber() {
        return uIDNumber;
    }

    public void setuIDNumber(String uIDNumber) {
        this.uIDNumber = uIDNumber;
    }

    @Field 
    public String geteIDNumber() {
        return eIDNumber;
    }

    public void seteIDNumber(String eIDNumber) {
        this.eIDNumber = eIDNumber;
    }

    @Field 
    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Field 
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Field 
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    @Field 
    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    @Field 
    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Field 
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Field
    public String getEconomicStatus() {
        return economicStatus;
    }

    public void setEconomicStatus(String economicStatus) {
        this.economicStatus = economicStatus;
    }

    @Field 
    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    @Field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Field 
    public String getlMPDate() {
        return lMPDate;
    }

    public void setlMPDate(String lMPDate) {
        this.lMPDate = lMPDate;
    }

    @Field 
    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
