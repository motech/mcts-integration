package org.motechproject.mcts.integration.model;

import org.motechproject.mcts.integration.service.IntegerValidator;

/**
 * Class to read FLWData from CSV file and transfer data to hibernate generated
 * model class
 * 
 * @author aman
 * 
 */
public class FLWDataCSV {
    private String id;
    private String District_ID;
    private String Taluka_ID;
    private String HealthBlock_ID;
    public String PHC_ID;

    public String getId() {
        return id;
    }

    public String getTaluka_ID() {
        return Taluka_ID;
    }

    public String getHealthBlock_ID() {
        return HealthBlock_ID;
    }

    public String getPHC_ID() {
        return PHC_ID;
    }

    public String getSubCentre_ID() {
        return SubCentre_ID;
    }

    public String getVillage_ID() {
        return Village_ID;
    }

    private String SubCentre_ID;
    private String Village_ID;
    private String Name;
    private String Contact_No;
    private String Sex;
    private String Type;
    private String Aadhar_No;
    private String Husband_Name;
    private String GF_Address;

    public Integer getIdasInteger() {
        return IntegerValidator.validateAndReturnAsInt("healthworkerId", id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDistrict_IDasInteger() {
        return IntegerValidator.validateAndReturnAsInt("districtId",
                District_ID);
    }

    public String getDistrict_ID() {
        return District_ID;
    }

    public void setDistrict_ID(String district_ID) {
        District_ID = district_ID;
    }

    public Integer getTaluka_IDasInteger() {
        return IntegerValidator.validateAndReturnAsInt("talukaId", Taluka_ID);
    }

    public void setTaluka_ID(String taluka_ID) {
        Taluka_ID = taluka_ID;
    }

    public Integer getHealthBlock_IDasInteger() {
        return IntegerValidator.validateAndReturnAsInt("HealthBlock_ID",
                HealthBlock_ID);
    }

    public void setHealthBlock_ID(String healthBlock_ID) {
        HealthBlock_ID = healthBlock_ID;
    }

    public Integer getSubCentre_IDasInteger() {
        return IntegerValidator.validateAndReturnAsInt("SubCentre_ID",
                SubCentre_ID);
    }

    public void setSubCentre_ID(String subCentre_ID) {
        SubCentre_ID = subCentre_ID;
    }

    public Integer getPHC_IDasInteger() {
        return IntegerValidator.validateAndReturnAsInt("PHC_ID", PHC_ID);
    }

    public void setPHC_ID(String pHC_ID) {
        PHC_ID = pHC_ID;
    }

    public Integer getVillage_IDasInteger() {
        return IntegerValidator
                .validateAndReturnAsInt("Village_ID", Village_ID);
    }

    public void setVillage_ID(String Village_ID) {
        this.Village_ID = Village_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getContact_No() {
        return Contact_No;
    }

    public void setContact_No(String Contact_No) {
        this.Contact_No = Contact_No;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getAadhar_No() {
        return Aadhar_No;
    }

    public void setAadhar_No(String Aadhar_No) {
        this.Aadhar_No = Aadhar_No;
    }

    public String getHusband_Name() {
        return Husband_Name;
    }

    public void setHusband_Name(String Husband_Name) {
        this.Husband_Name = Husband_Name;
    }

    public String getGF_Address() {
        return GF_Address;
    }

    public void setGF_Address(String GF_Address) {
        this.GF_Address = GF_Address;
    }

}
