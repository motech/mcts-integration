package org.motechproject.mcts.integration.mds.model;

import java.util.Date;

import javax.jdo.annotations.Unique;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.motechproject.mds.annotations.Field;

@MappedSuperclass
public class Form implements java.io.Serializable {
	
	private static final long serialVersionUID = -3677815893832100806L;
	@Unique
	private String instanceId;

	private String appVersion;

	private Date serverDateModified;

	private Integer deliveryOffsetDays;

	public Form() {
	}

	@Field
	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Field
	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Field
	public Date getServerDateModified() {
		return serverDateModified;
	}

	public void setServerDateModified(Date serverDateModified) {
		this.serverDateModified = serverDateModified;
	}

	@Field
	public Integer getDeliveryOffsetDays() {
		return this.deliveryOffsetDays;
	}

	public void setDeliveryOffsetDays(Integer deliveryOffsetDays) {
		this.deliveryOffsetDays = deliveryOffsetDays;
	}

}
