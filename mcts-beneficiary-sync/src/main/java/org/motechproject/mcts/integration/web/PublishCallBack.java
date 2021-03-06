/**
 * Contains the method to be called by Subscribers. The method sends back the updates received from MCTS
 **/
package org.motechproject.mcts.integration.web;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.motechproject.mcts.care.common.mds.model.HubTransaction;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;
import org.motechproject.mcts.care.common.mds.repository.MdsRepository;
import org.motechproject.mcts.care.common.mds.service.MctsHealthworkerMDSService;
import org.motechproject.mcts.care.common.mds.service.MctsSubcenterMDSService;
import org.motechproject.mcts.care.common.mds.service.MctsVillageMDSService;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.BeneficiarySubscriberUpdateUrlDTO;
import org.motechproject.mcts.integration.model.BeneficiaryUpdateDTO;
import org.motechproject.mcts.integration.model.ListOfBeneficiariesUpdatesDTO;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	@Autowired
	private MctsRepository careDataRepository;

	static final long ONE_MINUTE_IN_MILLIS = 60000; // millisecs

	private final String dateFormat = "dd/MM/yyyy HH:mm:ss.SS";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncController.class);

	@Autowired
	private PropertyReader propertyReader;


	/**
	 * Method to validate connection
	 * 
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
	 * Call Back Method to send Updates Url to <code>Hub</code> to be sent to
	 * <code>Subscriber</code> by fetching <code>startDate</code> and
	 * <code>endDate</code> from <code>hub_transaction</code> table by calling
	 * <code>findListOfEntitiesByField</code> from <code>CareDatService</code>
	 * class and Sends the Mapped <code>BeneficiaryUpdateDTO</code> to
	 * Subscribers
	 * 
	 * @return String in xml format containing all the update Urls that have not
	 *         been sent to Hub
	 * @throws BeneficiaryException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@RequestMapping(value = "beneficiaries", method = RequestMethod.POST, produces = "text/xml")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	String sendBeneficiaryResourceUrlForSubscriber() {
		LOGGER.info("Sending Resource Url to Hub.");
		List<HubTransaction> hubTransactions = careDataService
				.findListOfEntitiesByField(HubTransaction.class, "isNotified",
						false);
		BeneficiarySubscriberUpdateUrlDTO beneficiarySubscriberUpdateUrlDTO = new BeneficiarySubscriberUpdateUrlDTO();
		for (HubTransaction hubTransaction : hubTransactions) {
			String updateUrl = propertyReader.getHubSyncFromUrl(
					new SimpleDateFormat(dateFormat).format(hubTransaction
							.getStartDate()), new SimpleDateFormat(dateFormat)
							.format(hubTransaction.getEndDate()));
			hubTransaction.setIsNotified(true);
			careDataService.saveOrUpdate(hubTransaction);
			beneficiarySubscriberUpdateUrlDTO.addBeneficiaryDetails(updateUrl);
		}
		String updateString = ObjectToXMLConverter.converObjectToXml(
				beneficiarySubscriberUpdateUrlDTO,
				BeneficiarySubscriberUpdateUrlDTO.class);
		LOGGER.debug("Content Sent is:\n" + updateString + "\n");
		return updateString;
	}

	/**
	 * Call Back Method to send Updates to Subscriber by fetching updates from
	 * Db based on parameters in call back url by calling
	 * <code>findEntityByFieldWithConstarint</code> from
	 * <code>CareDatService</code> class and Sends the Mapped
	 * <code>BeneficiaryUpdateDTO</code> to Subscribers
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 * @throws BeneficiaryException
	 * @throws Exception
	 */
	@RequestMapping(value = "updatesreceived", method = RequestMethod.POST, produces = "text/xml")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	String sendUpdatesReceived(@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {
		LOGGER.info("Publishing Data to Subscribers.");
		Date startdate;
		Date enddate;
		try {
			startdate = new Date(new SimpleDateFormat(dateFormat,
					Locale.ENGLISH).parse(startTime).getTime());
			enddate = new Date(new SimpleDateFormat(dateFormat, Locale.ENGLISH)
					.parse(endTime).getTime());
		} catch (ParseException e) {
			throw new BeneficiaryException(ApplicationErrors.PARSE_EXCEPTION,
					e, e.getMessage());
		}

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
		String updateString = ObjectToXMLConverter.converObjectToXml(
				listOfBeneficiariesUpdatesDTO,
				ListOfBeneficiariesUpdatesDTO.class);
		LOGGER.info("Updates Sent are:\n" + updateString);
		LOGGER.debug(mctsPregnantMothers.toString());
		return updateString;
	}

	/**
	 * maps the <code>mctsPregnantMother</code> to
	 * <code>BeneficiaryUpdateDTO</code>
	 * 
	 * @param mctsPregnantMother
	 * @return BeneficiaryUpdateDTO
	 */
	public BeneficiaryUpdateDTO mapMctsPregnantMotherToBeneficiaryUpdateDTO(
			MctsPregnantMother mctsPregnantMother) {
		BeneficiaryUpdateDTO beneficiaryUpdateDTO = new BeneficiaryUpdateDTO();
		if (mctsPregnantMother.getMctsHealthworkerByAnmId() != null) {
			MctsHealthworker anmWorker = mctsPregnantMother
					.getMctsHealthworkerByAnmId();
			beneficiaryUpdateDTO
					.setAnmWorkerId(careDataRepository.getDetachedFieldId(anmWorker));
		}

		if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
			MctsHealthworker ashaworker = mctsPregnantMother
					.getMctsHealthworkerByAshaId();
			beneficiaryUpdateDTO
					.setAshaWorkerId(careDataRepository.getDetachedFieldId(ashaworker));
		}

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
		if (mctsPregnantMother.getMctsSubcenter() != null) {
			MctsSubcenter subCentre = mctsPregnantMother.getMctsSubcenter();
			beneficiaryUpdateDTO
					.setMctsSubcenter(careDataRepository.getDetachedFieldId(subCentre));
		}
		if (mctsPregnantMother.getMctsVillage() != null) {
			MctsVillage village = mctsPregnantMother.getMctsVillage();
			beneficiaryUpdateDTO
					.setMctsVillage(careDataRepository.getDetachedFieldId(village));
		}
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
