package org.motechproject.mcts.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("restriction")
@XmlRootElement
public class Fields {
	
	private GroupId groupId;
	private Id id;
	private Name name;
	
	public GroupId getGroupId() {
		return groupId;
	}
	@JsonProperty("group_id")
	public void setGroupId(GroupId groupId) {
		this.groupId = groupId;
	}
	public Id getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(Id id) {
		this.id = id;
	}
	public Name getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(Name name) {
		this.name = name;
	}
	
	
}
