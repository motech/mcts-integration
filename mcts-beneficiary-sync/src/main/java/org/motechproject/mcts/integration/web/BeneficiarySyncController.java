package org.motechproject.mcts.integration.web;

import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.service.MCTSBeneficiarySyncService;
import org.motechproject.mcts.integration.service.MotechBeneficiarySyncService;
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
/**
 * Controller class to call the services
 * 1. Sync from Mcts to Motech
 * 2. Sync To Mcts from Motech
 * @author mohit
 *
 */
@Controller
@RequestMapping(value = "/beneficiary")
public class BeneficiarySyncController {
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncController.class);
	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";
	private static final String VALID_DATE_PATTERN = "^\\d{2}-\\d{2}-\\d{4}$";

	@Autowired
    private PropertyReader propertyReader;
	
	@Autowired
	private MCTSBeneficiarySyncService mctsBeneficiarySyncService;
	
	@Autowired
	private MotechBeneficiarySyncService motechBeneficiarySyncService;

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
	
	/**
	 * Method to send request to mcts to send updates
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "syncFrom", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String syncFrom(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) throws Exception {
		LOGGER.debug("Requested startDate is: " + startDate + " & endDate is: " + endDate);
		validateDateFormat(startDate);
		validateDateFormat(endDate);
		LOGGER.info("Arguments are Valid");
		DateTime parsedStartDate = parseDate(startDate);
		DateTime parsedEndDate = parseDate(endDate);
		LOGGER.debug("Parsed startDate is: " + parsedStartDate + " & endDate is: " + parsedEndDate);
		mctsBeneficiarySyncService.syncBeneficiaryData(parsedStartDate, parsedEndDate);
		return "Updates Received Successfully";
	}
	
	/**
	 * Method to post updates to mcts
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "syncTo", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String syncTo(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) throws Exception {
		LOGGER.debug("Requested startDate is: " + startDate + " & endDate is: " + endDate);
		validateDateFormat(startDate);
		validateDateFormat(endDate);
		LOGGER.info("Arguments are Valid");
		DateTime parsedStartDate = parseDate(startDate);
		DateTime parsedEndDate = parseDate(endDate);
		LOGGER.debug("Parsed startDate is: " + parsedStartDate + " & endDate is: " + parsedEndDate);
		motechBeneficiarySyncService.syncBeneficiaryData(parsedStartDate, parsedEndDate);
		return "Updates Sent Successfully";
	}
	
	/**
	 * Method to validate the input arguments
	 * @param date
	 */
	private static void validateDateFormat(String date) {
	if (!Pattern.matches(VALID_DATE_PATTERN, date))
				throw new IllegalArgumentException(String.format(
						"Invalid date format. Date format should be: %s.",
						DATE_TIME_FORMAT));
	}
	
	/**
	 * method to convert from String to dateTime format
	 * @param dateString
	 * @return
	 */
	private static DateTime parseDate(String dateString) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern(DATE_TIME_FORMAT);
		return dateTimeFormatter.parseDateTime(dateString);
	}
}
