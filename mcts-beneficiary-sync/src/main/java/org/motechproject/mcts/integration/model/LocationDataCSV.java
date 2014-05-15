package org.motechproject.mcts.integration.model;

public class LocationDataCSV 
{
	private String StateID;
	private String State;
	private String DCode;
	private String District;
	private String TCode;
	private String Taluka_Name;
	private String BID;
	private String Block;
	private String PID;
	private String PHC;
	private String SID;
	private String SUBCenter;
	private String VCode;
	private String Village;
	
	public Integer getStateID() {
		return Integer.parseInt(StateID);
	}
	public void setStateID(String stateID) {
		StateID = stateID;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public Integer getDCode() {
		return Integer.parseInt(DCode);
	}
	public void setDCode(String dCode) {
		DCode = dCode;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public Integer getTCode() {
		return Integer.parseInt(TCode);
	}
	public void setTCode(String tCode) {
		TCode = tCode;
	}
	public String getTaluka_Name() {
		return Taluka_Name;
	}
	public void setTaluka_Name(String taluka_Name) {
		Taluka_Name = taluka_Name;
	}
	public Integer getBID() {
		return Integer.parseInt(BID);
	}
	public void setBID(String bID) {
		BID = bID;
	}
	public String getBlock() {
		return Block;
	}
	public void setBlock(String block) {
		Block = block;
	}
	public Integer getPID() {
		return Integer.parseInt(PID);
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public String getPHC() {
		return PHC;
	}
	public void setPHC(String pHC) {
		PHC = pHC;
	}
	public Integer getSID() {
		return Integer.parseInt(SID);
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getSUBCenter() {
		return SUBCenter;
	}
	public void setSUBCenter(String sUBCenter) {
		SUBCenter = sUBCenter;
	}
	public Integer getVCode() {
		return Integer.parseInt(VCode);
	}
	public void setVCode(String vCode) {
		VCode = vCode;
	}
	public String getVillage() {
		return Village;
	}
	public void setVillage(String village) {
		Village = village;
	}
	
	
	
}
