package org.motechproject.mcts.integration.model;

public class Fields {
	private String groupId;
	private String id;
	
	
	public Fields(String groupId, String id) {
		this.groupId = groupId;
		this.id = id;
	}
	
	public Fields() {
		
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
