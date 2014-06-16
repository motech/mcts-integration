package org.motechproject.mcts.integration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NewDataSet")
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

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Record record : records)
			s.append(record);
		return s.toString();
	}

	@Override
	public boolean equals(Object object) {
		NewDataSet newDataSet = (NewDataSet) object;
		if (newDataSet.getRecords().equals(this.getRecords())) {
			return true;
		}
		return false;
	}

}
