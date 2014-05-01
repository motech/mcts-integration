package mcts.integration.stub.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Update {
	private static final Integer MCTS_REQUEST_MODE = 4;
	private static final Integer MCTS_REQUEST_DAY_OFFSET = 0;
	private static final String DEFAULT_DATE_PATTERN = "dd-MM-yyyy";

	@XmlElement(name = "StateId")
	private Integer stateId;

	@XmlElement(name = "ID")
	private String mctsId;

	@XmlElement(name = "ServiceType")
	private Integer serviceType;

	@XmlElement(name = "Day")
	private Integer dayOffset;

	@XmlElement(name = "AsOn_date")
	private String asOnDate;

	@XmlElement(name = "Remarks")
	private String remarks;

	@XmlElement(name = "Mode")
	private Integer mode;

	@XmlElement(name = "HBLevel", defaultValue = "")
	private String hbLevel;

	@XmlElement(name = "ContactNo")
	private String contactNo;

	private boolean isError = false;

	public Update() {
	}

	public Update(Integer stateId, String mctsId, Integer serviceType,
			Date asOnDate, String contactNo, String hbLevel) {
		this.stateId = stateId;
		this.mctsId = mctsId;
		this.serviceType = serviceType;
		this.asOnDate = asOnDate != null ? new SimpleDateFormat(
				DEFAULT_DATE_PATTERN).format(asOnDate) : null;
		this.dayOffset = MCTS_REQUEST_DAY_OFFSET;
		this.contactNo = contactNo;
		this.hbLevel = hbLevel;
		this.mode = MCTS_REQUEST_MODE;
	}

	public String[] verifyUpdate() {
		String[] failure = new String[2];
		failure[0] = this.mctsId;
		String error = String.format("%s%s%s%s%s%s%s%s",
				this.verifyAsOnDate(this.asOnDate),
				this.verifyContactNo(this.contactNo),
				this.verifyDayOffSet(this.dayOffset),
				this.verifyHbLevel(this.hbLevel),
				this.verifyMctsId(this.mctsId), this.verifyMode(this.mode),
				this.verifyServiceType(this.serviceType),
				this.verifyStateId(this.stateId));
		failure[1] = error;
		return isError ? failure : null;
	}

	// Verifies that StateId is 31
	public String verifyStateId(Integer stateId) {
		if (!stateId.equals(10)) {
			isError = true;
			return "invalid StateId: " + stateId;
		}
		return null;
	}

	// NO VERIFICATION
	public String verifyMctsId(String mctsId) {
		if (mctsId != null)
			return null;
		else {
			isError = true;
			return "Invalid Mcts-Id: " + mctsId;
		}
	}

	// Can be from 2 to 9 only
	public String verifyServiceType(Integer serviceType) {
		if (serviceType > 1 && serviceType < 10)
			return null;
		isError = true;
		return ("invalid Service Type: " + serviceType);
	}

	// verifies that date is a valid and in format DD-MM-YYYY
	public String verifyAsOnDate(String asOnDate) {
		final String DATE_FORMAT = "dd-MM-yyyy";
		try {
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			dateFormat.setLenient(false);
			dateFormat.parse(asOnDate);
			return null;
		} catch (ParseException e) {
			isError = true;
			return ("invalid date: " + asOnDate);
		}
	}

	// verifies that dayOffSet is 0
	public String verifyDayOffSet(Integer dayOffset) {
		if (!dayOffset.equals(0)) {
			isError = true;
			return ("invalid Day OffSet: " + dayOffset);
		}
		return null;
	}

	// NO VERIFICATION
	public String verifyContactNo(String contactNo2) {
		return null;
	}

	// Verifies it to be either 1 or 2 or 3 or null
	public String verifyHbLevel(String hbLevel) {
		if (this.serviceType == 2 || this.serviceType == 3
				|| this.serviceType == 4) {
			if (hbLevel.equals("1") || hbLevel.equals("2")
					|| hbLevel.equals("3") || hbLevel.equals("")
					|| hbLevel == null)
				return null;
		} else if (hbLevel == "" || hbLevel == null)
			return null;
		isError = true;
		return ("invalid hb Level: " + hbLevel);
	}

	// Verifies Mode ==4
	public String verifyMode(Integer mode) {
		if (!mode.equals(4)) {
			isError = true;
			return ("invalid mode: " + mode);
		}
		return null;
	}
	
	public void setServiceType(int serviceType){
		this.serviceType = serviceType;
	}
}
