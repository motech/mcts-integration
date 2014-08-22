package org.motechproject.mcts.integration.mds.model;

import java.util.Date;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;


@Entity
public class FlwGroup {
	@Field
	private String groupId;
	@Field
	private Boolean caseSharing;
	@Field
	private String domain;
	@Field
	private String awcCode;
	@Field
	private String name;
	@Field
	private Boolean reporting;
	@Field
	private Date creationTime;
	@Field
	private Date lastModifiedTime;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Boolean getCaseSharing() {
		return caseSharing;
	}
	public void setCaseSharing(Boolean caseSharing) {
		this.caseSharing = caseSharing;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAwcCode() {
		return awcCode;
	}
	public void setAwcCode(String awcCode) {
		this.awcCode = awcCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getReporting() {
		return reporting;
	}
	public void setReporting(Boolean reporting) {
		this.reporting = reporting;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
	
}
