package org.motechproject.mcts.integration.model;

import org.motechproject.mcts.integration.service.IntegerValidator;

/**
 * Class to read LocationData from CSV file and transfer data to hibernate generated model class
 * @author aman
 *
 */
public class LocationDataCSV 
{
	private String StateID;
	private String State;
	private String DCode;
	private String District;
	private String TCode;
	private String Taluka_Name;
	public String getStateID() {
		return StateID;
	}
	public String getDCode() {
		return DCode;
	}
	public String getTCode() {
		return TCode;
	}
	public String getBID() {
		return BID;
	}
	public String getPID() {
		return PID;
	}
	public String getSID() {
		return SID;
	}
	public String getVCode() {
		return VCode;
	}
	private String BID;
	private String Block;
	private String PID;
	private String PHC;
	private String SID;
	private String SUBCenter;
	private String VCode;
	private String Village;
	
	public Integer getStateIDasInteger() {
		return IntegerValidator.validateAndReturnAsInt("stateId", StateID);
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
	public Integer getDCodeasInteger() {
		return IntegerValidator.validateAndReturnAsInt("dCode", DCode);
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
	public Integer getTCodeasInteger() {
		return IntegerValidator.validateAndReturnAsInt("tCode", TCode);
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
	public Integer getBIDasInteger() {
		return IntegerValidator.validateAndReturnAsInt("BID", BID);
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
	public Integer getPIDasInteger() {
		return IntegerValidator.validateAndReturnAsInt("PID", PID);
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
	public Integer getSIDasInteger() {
		return IntegerValidator.validateAndReturnAsInt("SID", SID);	
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
	public Integer getVCodeasInteger() {
		return IntegerValidator.validateAndReturnAsInt("VCode", VCode);
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
