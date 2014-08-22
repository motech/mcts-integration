package org.motechproject.mcts.integration.mds.model;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class DontKnowForm {
	private String dontKnow;
	private String known;
	private String mctsHusbandName;
	private String mctsFullName;
	private String nameSpace;
	private String dateModified;
	private String instanceId;
	private String deviceId;
	private String ownerId;
	private String appVersion;
	private String userId;
	private MotherCase motherCase;
	private MctsPregnantMother mctsPregnantMother;

	@Field
	public String getDontKnow() {
		return dontKnow;
	}

	public void setDontKnow(String dontKnow) {
		this.dontKnow = dontKnow;
	}

	@Field
	public String getKnown() {
		return known;
	}

	public void setKnown(String known) {
		this.known = known;
	}

	@Field
	public String getMctsHusbandName() {
		return mctsHusbandName;
	}

	public void setMctsHusbandName(String mctsHusbandName) {
		this.mctsHusbandName = mctsHusbandName;
	}

	@Field
	public String getMctsFullName() {
		return mctsFullName;
	}

	public void setMctsFullName(String mctsFullName) {
		this.mctsFullName = mctsFullName;
	}

	@Field
	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	@Field
	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	@Field
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Field
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Field
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Field
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Field
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Field
	@Cascade(persist = true, update = true, delete = false)
	public MotherCase getMotherCase() {
		return this.motherCase;
	}

	public void setMotherCase(MotherCase motherCase) {
		this.motherCase = motherCase;
	}

	@Field
	@Cascade(persist = true, update = true, delete = false)
	public MctsPregnantMother getMctsPregnantMother() {
		return mctsPregnantMother;
	}

	public void setMctsPregnantMother(MctsPregnantMother mctsPregnantMother) {
		this.mctsPregnantMother = mctsPregnantMother;
	}
}
