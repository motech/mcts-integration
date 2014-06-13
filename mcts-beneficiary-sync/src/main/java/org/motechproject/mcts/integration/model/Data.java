package org.motechproject.mcts.integration.model;

public class Data {
	private Fields fields;
	private String fixtureType;
	
	public Data() {
		
	}

	public Data(Fields fields, String fixtureType) {
		this.fields = fields;
		this.fixtureType = fixtureType;
	}
	
	public Fields getFields() {
		return fields;
	}
	public void setFields(Fields fields) {
		this.fields = fields;
	}
	
	
	public String getFixtureType() {
		return fixtureType;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	
}
