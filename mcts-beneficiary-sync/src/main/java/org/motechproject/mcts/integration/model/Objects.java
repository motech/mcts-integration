package org.motechproject.mcts.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;


@SuppressWarnings("restriction")
@XmlRootElement
public class Objects {
	private Fields fields;
	private String fixtureType;
	String uri;
	String resourceUri;
	
	public Fields getFields() {
		return fields;
	}
	public void setFields(Fields fields) {
		this.fields = fields;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	@JsonProperty("fixture_type")
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public String getUri() {
		return uri;
	}
	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getResourceUri() {
		return resourceUri;
	}
	@JsonProperty("resource_uri")
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}
	
	
	
}
