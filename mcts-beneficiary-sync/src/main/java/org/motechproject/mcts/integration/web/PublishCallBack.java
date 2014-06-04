package org.motechproject.mcts.integration.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.model.BeneficiaryUpdateDTO;
import org.motechproject.mcts.integration.model.ListOfBeneficiariesUpdatesDTO;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/publish")
public class PublishCallBack {

	@Autowired
	private CareDataService careDataService;
	
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncController.class);

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private ObjectToXMLConverter objectToXMLConverter;

	/**
	 * Method to validate connection
	 * @param query
	 * @return string
	 */
	@RequestMapping(value = "ping", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String ping(@RequestParam("query") String query){
		return String.format("Ping Received Succefully with query param: %s", query);
	}
	
	@RequestMapping(value = "updatesreceived", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	HttpEntity sendUpdatesReceived(@RequestParam("time") String dateTime)
			throws Exception {
		LOGGER.info("Publishing Data to Hub.");
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS", Locale.ENGLISH).parse(dateTime);
		long t=date.getTime();
		int interval = propertyReader.getIntervalToFetchUpdatesFromDbInMin();
		Date higherDate=new Date(t + (interval/2 * ONE_MINUTE_IN_MILLIS));
		Date lowerDate=new Date(t - (interval/2 * ONE_MINUTE_IN_MILLIS));
		LOGGER.debug("Params Passed are LowerDateTime: " + lowerDate + " & HigherDateTime: " + higherDate);
		List<MctsPregnantMother> mctsPregnantMothers = careDataService.findEntityByFieldWithConstarint(MctsPregnantMother.class, "creationTime",lowerDate, higherDate);
		LOGGER.debug("Total Number of Updates received are: " + mctsPregnantMothers.size());
		LOGGER.debug("\n\nMcts Id of the update is: " + mctsPregnantMothers.get(0).getMctsId());
		LOGGER.debug("\n\nANM Id of the update is: " + mctsPregnantMothers.get(0).getMctsHealthworkerByAnmId().getId());
		ListOfBeneficiariesUpdatesDTO listOfBeneficiariesUpdatesDTO = new ListOfBeneficiariesUpdatesDTO();
		for(MctsPregnantMother mctsPregnantMother: mctsPregnantMothers){
			BeneficiaryUpdateDTO beneficiaryUpdateDTO = mapMctsPregnantMotherToBeneficiaryUpdateDTO(mctsPregnantMother);
			listOfBeneficiariesUpdatesDTO.addBeneficiaryDetails(beneficiaryUpdateDTO);
			beneficiaryUpdateDTO = null;
		}
		String updateString = objectToXMLConverter.writeToXML(listOfBeneficiariesUpdatesDTO, ListOfBeneficiariesUpdatesDTO.class);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		LOGGER.info("Updates Sent are:\n" + updateString);
		HttpEntity httpEntity = new HttpEntity(updateString, httpHeaders);
		LOGGER.debug(mctsPregnantMothers.toString());
		return httpEntity;
	}
	
	public BeneficiaryUpdateDTO mapMctsPregnantMotherToBeneficiaryUpdateDTO(MctsPregnantMother mctsPregnantMother){
		BeneficiaryUpdateDTO beneficiaryUpdateDTO = new BeneficiaryUpdateDTO();
		beneficiaryUpdateDTO.setAnmWorkerId(mctsPregnantMother.getMctsHealthworkerByAnmId().getId());
//		beneficiaryUpdateDTO.setAnmWorkerName(mctsPregnantMother.getMctsHealthworkerByAnmId().getName());
		beneficiaryUpdateDTO.setAshaWorkerId(mctsPregnantMother.getMctsHealthworkerByAshaId().getId());
//		beneficiaryUpdateDTO.setAshaWorkerName(mctsPregnantMother.getMctsHealthworkerByAshaId().getName());
		beneficiaryUpdateDTO.setBeneficiaryAddress(mctsPregnantMother.getBeneficiaryAddress());
		beneficiaryUpdateDTO.setBirthDate(mctsPregnantMother.getBirthDate());
		beneficiaryUpdateDTO.setCategory(mctsPregnantMother.getCategory());
		beneficiaryUpdateDTO.setEconomicStatus(mctsPregnantMother.getEconomicStatus());
		beneficiaryUpdateDTO.setEidNumber(mctsPregnantMother.getEidNumber());
		beneficiaryUpdateDTO.setEmail(mctsPregnantMother.getEmail());
		beneficiaryUpdateDTO.setFatherHusbandName(mctsPregnantMother.getFatherHusbandName());
		beneficiaryUpdateDTO.setGender(mctsPregnantMother.getGender());
		beneficiaryUpdateDTO.setLmpDate(mctsPregnantMother.getLmpDate());
		beneficiaryUpdateDTO.setMctsId(mctsPregnantMother.getMctsId());
		beneficiaryUpdateDTO.setMctsSubcenter(mctsPregnantMother.getMctsSubcenter().getId());
		beneficiaryUpdateDTO.setMctsVillage(mctsPregnantMother.getMctsVillage().getId());
//		/beneficiaryUpdateDTO.setMotherCaseId(mctsPregnantMother.getMotherCase().getId());
		beneficiaryUpdateDTO.setMobileNo(mctsPregnantMother.getMobileNo());
		beneficiaryUpdateDTO.setName(mctsPregnantMother.getName());
		beneficiaryUpdateDTO.setPincode(mctsPregnantMother.getPincode());
		beneficiaryUpdateDTO.setTown(mctsPregnantMother.getTown());
		beneficiaryUpdateDTO.setType(mctsPregnantMother.getType());
		beneficiaryUpdateDTO.setUidNumber(mctsPregnantMother.getUidNumber());
		beneficiaryUpdateDTO.setWard(mctsPregnantMother.getWard());
		return beneficiaryUpdateDTO;
	}
	
	/*@RequestMapping(value = "updatessent", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	HttpEntity sendUpdatesSent(@RequestParam("time") String time)
			throws Exception {
		LOGGER.info("Publishing Data to Hub.");
		String data = readFileData(time);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
		return httpEntity;
	}
	*/
/*	public String returnJson(Object obj){
		Gson gson = new Gson();
		 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(obj);
	 
		return json;
	}*/

	/*public String readFileData(String filePath) throws Exception {
		BufferedReader br = null;
		String data = new String();		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				data += sCurrentLine;
			}
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				throw new Exception(ex);
			}
			return data;
		}
	}
*/
}
