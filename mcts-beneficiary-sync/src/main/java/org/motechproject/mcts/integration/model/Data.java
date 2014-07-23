package org.motechproject.mcts.integration.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class Data {
	private List<Objects> objects;
	private Meta meta;

	public Meta getMeta() {
		return meta;
	}
	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<Objects> getObjects() {
		return objects;
	}
	@JsonProperty("objects")
	public void setObjects(List<Objects> objects) {
		this.objects = objects;
	}

	
}
