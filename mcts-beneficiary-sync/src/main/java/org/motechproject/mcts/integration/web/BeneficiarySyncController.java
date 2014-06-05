/**
 * Controller class to call the services
 * 1. Sync from Mcts to Motech
 * 2. Sync To Mcts from Motech
 * @author mohit
 *
 */
package org.motechproject.mcts.integration.web;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.service.FLWDataPopulator;
import org.motechproject.mcts.integration.service.LocationDataPopulator;
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
	
	@Autowired
	private LocationDataPopulator locationDataPopulator;
	
	@Autowired
	private FLWDataPopulator fLWDataPopulator;

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
	 * Method to send request to MCTS to send updates
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
	 * Method to post updates to MCTS
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
	

	
	@RequestMapping(value = "/addLocations", method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	 public String addLocationData(  
	     @RequestParam("file") MultipartFile file) throws Exception {
	  
	  LOGGER.info("Request to upload xml file of job:");
	 
	  byte[] bytes = file.getBytes();
	  LOGGER.info("thispath"+System.getProperty("java.io.tmpdir"));
	  String path = System.getProperty("java.io.tmpdir");
	  File newFile = new File(path+"/beneficiary.xml");
	  FileOutputStream out = new FileOutputStream(newFile);
	  out.write(bytes);
	  LOGGER.info("size"+newFile.getTotalSpace());
	  LOGGER.info("temp path"+newFile.getAbsolutePath());
	  locationDataPopulator.populateLocations(newFile);
		  
		  return "Data Added Successfully";
	 
	  
	  
	  
	 }
	
	@RequestMapping(value = "/addFLW", method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	 public String addFLWData(  
	     @RequestParam("file") MultipartFile file) throws Exception {
	  
	  LOGGER.info("Request to upload xml file of job:");
	 
	  byte[] bytes = file.getBytes();
	  File newFile = new File("java.io.tmpdir");
	  FileOutputStream out = new FileOutputStream(newFile);
	  out.write(bytes);
	  System.out.println("size"+newFile.getTotalSpace());
	  fLWDataPopulator.populateFLWData(newFile);;
		  
		  return "Data Added Successfully";
	 
	  
	  
	  
	 }
	
	/**
	 * Method to validate the input date arguments
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


