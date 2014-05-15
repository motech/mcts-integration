package org.motechproject.mcts.integration.model;

public class FLWDataCSV {
	private String id;
	private String District_ID;
	private String Taluka_ID;
	private String HealthBlock_ID;
	public String PHC_ID;
	
	private String SubCentre_ID;
	private String Village_ID;
	private String Name;
	private String Contact_No;
	private String Sex;
	private String Type;
	private String Aadhar_No;
	private String Husband_Name;
	private String GF_Address;
	
	public Integer getId() {
		return Integer.parseInt(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getDistrict_ID() {
		return Integer.parseInt(District_ID);
	}
	public void setDistrict_ID(String district_ID) {
		District_ID = district_ID;
	}
	public Integer getTaluka_ID() {
		return Integer.parseInt(Taluka_ID);
	}
	public void setTaluka_ID(String taluka_ID) {
		Taluka_ID = taluka_ID;
	}
	public Integer getHealthBlock_ID() {
		return Integer.parseInt(HealthBlock_ID);
	}
	public void setHealthBlock_ID(String healthBlock_ID) {
		HealthBlock_ID = healthBlock_ID;
	}
	
	public Integer getSubCentre_ID() {
		return Integer.parseInt(SubCentre_ID);
	}
	public void setSubCentre_ID(String subCentre_ID) {
		SubCentre_ID = subCentre_ID;
	}
	public Integer getPHC_ID() {
		return Integer.parseInt(PHC_ID);
	}
	public void setPHC_ID(String pHC_ID) {
		PHC_ID = pHC_ID;
	}
	public Integer getVillage_ID() {
		return Integer.parseInt(Village_ID);
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
