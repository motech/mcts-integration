package org.motechproject.mcts.integration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="NewDataSet")
public class NewDataSet {

	
	private List<Record> records;

	public NewDataSet() {
		super();
	}

	@XmlElement(name = "Records")
	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

}
