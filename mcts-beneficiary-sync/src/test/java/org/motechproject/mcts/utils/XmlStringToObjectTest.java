package org.motechproject.mcts.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class XmlStringToObjectTest {

	@Autowired
	private XmlStringToObject xmlStringToObject = new XmlStringToObject();

	@Test
	public void testToObjectConversion() throws Exception {
		String data = getNewDataSetXmlString();
		NewDataSet newDataSet = xmlStringToObject.stringXmlToObject(
				NewDataSet.class, data);
	}

	private String getNewDataSetXmlString() {
		String data = "<NewDataSet>"
				+ "<Records>"
				+ "<StateID>10</StateID>"
				+ "<State_Name>Bihar</State_Name>"
				+ "<District_ID>12</District_ID>"
				+ "<District_Name>Saharsa</District_Name>"
				+ "<Block_ID>180</Block_ID>"
				+ "<Block_Name>Saor Bazar</Block_Name>"
				+ "<Tehsil_ID>0163</Tehsil_ID>"
				+ "<Tehsil_Name>Saur Bazar</Tehsil_Name>"
				+ "<Facility_ID>175</Facility_ID>"
				+ "<Facility_Name>Saur Bazar</Facility_Name>"
				+ "<SubCentre_ID>6357</SubCentre_ID>"
				+ "<SubCentre_Name>Tiri HSC</SubCentre_Name>"
				+ "<Town></Town>"
				+ "<Village_ID>0</Village_ID>"
				+ "<Village_Name>Direct_Entry </Village_Name"
				+ "><ANM_ID>22364</ANM_ID>"
				+ "<ASHA_ID>22753</ASHA_ID>"
				+ "<Ward></Ward>"
				+ "<Beneficiary_Type>MOTHER</Beneficiary_Type>"
				+ "<Beneficiary_ID>101216300411300078</Beneficiary_ID>"
				+ "<Beneficiary_Name>Rubi Devi</Beneficiary_Name>"
				+ "<UID_Number></UID_Number>"
				+ "<EID_Number></EID_Number>"
				+ "<Birthdate>01-01-1996</Birthdate>"
				+ "<Gender>F</Gender>"
				+ "<FatherHusbandName>Pramchandr Yadav</FatherHusbandName>"
				+ "<BeneficiaryAddress>Tiri</BeneficiaryAddress>"
				+ "<PinCode></PinCode>"
				+ "<Category>Others</Category>"
				+ "<Economic_Status>Not Known</Economic_Status>"
				+ "<Mobile_no>9572701531</Mobile_no>"
				+ "<Email></Email>"
				+ "<LMP_Date>20-04-2013</LMP_Date>"
				+ "</Records>"
				+ "</NewDataSet>";
		return data;
	}

	private NewDataSet getNewDataSetObject() {
		Record record = new Record("10", "Bihar", "12", "Saharsa",
				"180", "Saor Bazar", "0163", "Saur Bazar", "175", "Saur Bazar", "6357",
				"Tiri HSC", null, "0", "Direct_Entry", "22364", "22753", null,
				"MOTHER", "101216300411300078", "Rubi Devi", null, null,
				"01-01-1996", "F", "Pramchandr Yadav", "Tiri", null, "ST",
				"Not Known", "9572701531", null, "20-04-2013");
		NewDataSet newDataSet = new NewDataSet();
		List<Record> records = new ArrayList<Record>();
		records.add(record);
		newDataSet.setRecords(records);
		return newDataSet;

	}
}
