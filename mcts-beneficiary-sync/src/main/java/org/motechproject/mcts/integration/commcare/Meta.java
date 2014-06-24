package org.motechproject.mcts.integration.commcare;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class Meta {
	private String xmlns;
	private String instanceID;
	private String timeStart;
	private String timeEnd;
	private String userID;
	public String getXmlns() {
		return xmlns;
	}
	@XmlAttribute(name = "xmlns")
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getInstanceID() {
		return instanceID;
	}
	@XmlElement(name ="instanceID")
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	public String getTimeStart() {
		return timeStart;
	}
	@XmlElement(name ="timeStart")
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	@XmlElement(name ="timeEnd")
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getUserID() {
		return userID;
	}
	@XmlElement(name ="userID")
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
}
