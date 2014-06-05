/**
 * Contains the method to be called by Subscribers. The method sends back the updates received from MCTS
 **/
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

	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

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
	public String ping(@RequestParam("query") String query) {
		return String.format("Ping Received Succefully with query param: %s",
				query);
	}

	/**
	 * Call Back Method to send Updates to Subscriber by fetching updates from Db based on parameters in call back url by calling
	 * <code>findEntityByFieldWithConstarint</code> from <code>CareDatService</code> class
	 * and Sends the Mapped <code>BeneficiaryUpdateDTO</code> to Subscribers
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updatesreceived", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	HttpEntity sendUpdatesReceived(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime)
			throws Exception {
		LOGGER.info("Publishing Data to Hub.");
		Date startdate = new Date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS",
				Locale.ENGLISH).parse(startTime).getTime());
		Date enddate = new Date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS",
				Locale.ENGLISH).parse(endTime).getTime());
	/*	long t = date.getTime();
		int interval = propertyReader.getIntervalToFetchUpdatesFromDbInMin();
		Date higherDate = new Date(t + (interval / 2 * ONE_MINUTE_IN_MILLIS));
		Date lowerDate = new Date(t - (interval / 2 * ONE_MINUTE_IN_MILLIS));*/
		LOGGER.debug("Params Passed are LowerDateTime: " + startdate
				+ " & HigherDateTime: " + enddate);
		List<MctsPregnantMother> mctsPregnantMothers = careDataService
				.findEntityByFieldWithConstarint(MctsPregnantMother.class,
						"creationTime", startdate, enddate);
		LOGGER.debug("Total Number of Updates received are: "
				+ mctsPregnantMothers.size());
		ListOfBeneficiariesUpdatesDTO listOfBeneficiariesUpdatesDTO = new ListOfBeneficiariesUpdatesDTO();
		for (MctsPregnantMother mctsPregnantMother : mctsPregnantMothers) {
			BeneficiaryUpdateDTO beneficiaryUpdateDTO = mapMctsPregnantMotherToBeneficiaryUpdateDTO(mctsPregnantMother);
			listOfBeneficiariesUpdatesDTO
					.addBeneficiaryDetails(beneficiaryUpdateDTO);
			beneficiaryUpdateDTO = null;
		}
		String updateString = objectToXMLConverter.converObjectToXml(
				listOfBeneficiariesUpdatesDTO,
				ListOfBeneficiariesUpdatesDTO.class);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		LOGGER.info("Updates Sent are:\n" + updateString);
		HttpEntity httpEntity = new HttpEntity(updateString, httpHeaders);
		LOGGER.debug(mctsPregnantMothers.toString());
		return httpEntity;
	}
/**
 * maps the <code>mctsPregnantMother</code> to <code>BeneficiaryUpdateDTO</code>
 * @param mctsPregnantMother
 * @return BeneficiaryUpdateDTO
 */
	public BeneficiaryUpdateDTO mapMctsPregnantMotherToBeneficiaryUpdateDTO(
			MctsPregnantMother mctsPregnantMother) {
		BeneficiaryUpdateDTO beneficiaryUpdateDTO = new BeneficiaryUpdateDTO();
		beneficiaryUpdateDTO.setAnmWorkerId(mctsPregnantMother
				.getMctsHealthworkerByAnmId().getId());
		beneficiaryUpdateDTO.setAshaWorkerId(mctsPregnantMother
				.getMctsHealthworkerByAshaId().getId());
		beneficiaryUpdateDTO.setBeneficiaryAddress(mctsPregnantMother
				.getBeneficiaryAddress());
		beneficiaryUpdateDTO.setBirthDate(mctsPregnantMother.getBirthDate());
		beneficiaryUpdateDTO.setCategory(mctsPregnantMother.getCategory());
		beneficiaryUpdateDTO.setEconomicStatus(mctsPregnantMother
				.getEconomicStatus());
		beneficiaryUpdateDTO.setEidNumber(mctsPregnantMother.getEidNumber());
		beneficiaryUpdateDTO.setEmail(mctsPregnantMother.getEmail());
		beneficiaryUpdateDTO.setFatherHusbandName(mctsPregnantMother
				.getFatherHusbandName());
		beneficiaryUpdateDTO.setGender(mctsPregnantMother.getGender());
		beneficiaryUpdateDTO.setLmpDate(mctsPregnantMother.getLmpDate());
		beneficiaryUpdateDTO.setMctsId(mctsPregnantMother.getMctsId());
		beneficiaryUpdateDTO.setMctsSubcenter(mctsPregnantMother
				.getMctsSubcenter().getId());
		beneficiaryUpdateDTO.setMctsVillage(mctsPregnantMother.getMctsVillage()
				.getId());
		beneficiaryUpdateDTO.setMobileNo(mctsPregnantMother.getMobileNo());
		beneficiaryUpdateDTO.setName(mctsPregnantMother.getName());
		beneficiaryUpdateDTO.setPincode(mctsPregnantMother.getPincode());
		beneficiaryUpdateDTO.setTown(mctsPregnantMother.getTown());
		beneficiaryUpdateDTO.setType(mctsPregnantMother.getType());
		beneficiaryUpdateDTO.setUidNumber(mctsPregnantMother.getUidNumber());
		beneficiaryUpdateDTO.setWard(mctsPregnantMother.getWard());
		return beneficiaryUpdateDTO;
	}

}
