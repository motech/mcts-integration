package org.motechproject.mcts.integration.web;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.care.common.mds.dimension.Flw;
import org.motechproject.mcts.care.common.mds.dimension.FlwGroup;
import org.motechproject.mcts.care.common.mds.measure.Form;
import org.motechproject.mcts.care.common.mds.measure.MotherEditForm;
import org.motechproject.mcts.integration.commcare.CloseCaseXmlService;
import org.motechproject.mcts.integration.commcare.CreateCaseXmlService;
import org.motechproject.mcts.integration.exception.BeneficiaryError;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.exception.RestException;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.FLWDataPopulator;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.LocationDataPopulator;
import org.motechproject.mcts.integration.service.MCTSBeneficiarySyncService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.motechproject.mcts.integration.service.MotechBeneficiarySyncService;
import org.motechproject.mcts.integration.service.StubDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller class to call the syncFrom and syncTo mcts services
 * 
 * @author mohit
 */

@Controller
@RequestMapping(value = "/beneficiary")
public class BeneficiarySyncController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeneficiarySyncController.class);
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";
    private static final String VALID_DATE_PATTERN = "^\\d{2}-\\d{2}-\\d{4}$";

    @Autowired
    private MCTSBeneficiarySyncService mctsBeneficiarySyncService;

    @Autowired
    private CreateCaseXmlService createCaseXmlService;

    @Autowired
    private MotechBeneficiarySyncService motechBeneficiarySyncService;

    @Autowired
    private LocationDataPopulator locationDataPopulator;

    @Autowired
    private FLWDataPopulator fLWDataPopulator;

    @Autowired
    private MCTSFormUpdateService mCTSFormUpdateService;

    @Autowired
    private FixtureDataService fixtureDataService;

    @Autowired
    private CloseCaseXmlService closeCaseXmlService;

    @Autowired
    private StubDataService stubDataService;

    @Autowired
    private CareDataService service;

    // @Autowired
    // private MctsRepository repository;

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
     * Method to send request to MCTS to send updates
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws BeneficiaryException
     * @throws Exception
     */
    @RequestMapping(value = "syncFrom", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String syncFrom(@RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LOGGER.debug("Requested startDate is: " + startDate + " & endDate is: "
                + endDate);

        validateDateFormat(startDate);
        validateDateFormat(endDate);
        LOGGER.info("Arguments are Valid");
        DateTime parsedStartDate = parseDate(startDate);
        DateTime parsedEndDate = parseDate(endDate);
        LOGGER.debug("Parsed startDate is: " + parsedStartDate
                + " & Parsed endDate is: " + parsedEndDate);
        return mctsBeneficiarySyncService.syncBeneficiaryData(parsedStartDate,
                parsedEndDate);
    }

    /**
     * Method to post updates to MCTS
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws BeneficiaryException
     * @throws FileNotFoundException
     * @throws Exception
     */
    @RequestMapping(value = "syncTo", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String syncTo(@RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LOGGER.debug("Requested startDate is: " + startDate + " & endDate is: "
                + endDate);
        validateDateFormat(startDate);
        validateDateFormat(endDate);
        LOGGER.info("Arguments are Valid");
        DateTime parsedStartDate = parseDate(startDate);
        DateTime parsedEndDate = parseDate(endDate);

        LOGGER.debug("Parsed startDate is: " + parsedStartDate
                + " & Parsed endDate is: " + parsedEndDate);
        motechBeneficiarySyncService.syncBeneficiaryData(parsedStartDate,
                parsedEndDate);

        return "Updates Sent Successfully";
    }

    /**
     * Method to add locations to database
     * 
     * @param file
     * @return
     * @throws
     * @throws
     * @throws
     * @throws Exception
     */
    @RequestMapping(value = "/addLocations", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String addLocationData(@RequestParam("file") MultipartFile file) {

        LOGGER.info("Request to upload xml file of job:");

        try {
            locationDataPopulator.populateLocations(file);
            return "Data Added Successfully";
        } catch (BeneficiaryException e) {
            LOGGER.error("Adding Location Data to the database failed");
            throw new RestException(e, e.getMessage());
        }

    }

    /**
     * Method to add FLWs to database
     * 
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addFLW/{stateId}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String addFLWData(@RequestParam("file") MultipartFile file,
            @PathVariable("stateId") String stateId) {

        LOGGER.info("Request to upload xml file of job:");

        try {
            fLWDataPopulator.populateFLWData(file, stateId);
            return "Data Added Successfully";

        } catch (BeneficiaryException e) {
            LOGGER.error("Adding FLW Data to the database failed", e);
            throw new RestException(e, e.getMessage());
        }

    }

    /**
     * Method to validate the input date arguments
     * 
     * @param date
     */
    private static void validateDateFormat(String date) {
        if (!Pattern.matches(VALID_DATE_PATTERN, date)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid date format. Date format should be: %s.",
                    DATE_TIME_FORMAT));
        }
    }

    /**
     * method to convert from String to dateTime format
     * 
     * @param dateString
     * @return
     */
    private static DateTime parseDate(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat
                .forPattern(DATE_TIME_FORMAT);
        return dateTimeFormatter.parseDateTime(dateString);
    }

    /**
     * Controller to update the case_groupId in the database
     * 
     * @param type
     * @return
     * @throws BeneficiaryException
     */
    @RequestMapping(value = "/getFixData/{type}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getFixData(@PathVariable("type") String type) {
        stubDataService.getFixtureData();
        return "successful";
    }

    @RequestMapping(value = "/getCommcareFixture", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String commcareFixture() {
        fixtureDataService.updateGroupId();
        return "successful";
    }

    @RequestMapping(value = "/getCaseXml", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getCaseXml() {
        createCaseXmlService.createCaseXml();
        return "success";
    }

    @RequestMapping(value = "/getCloseCaseXml", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getCloseCaseXml() {
        closeCaseXmlService.closeCaseXml();
        return "success";
    }

    @RequestMapping(value = "/saveForm", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void saveForm() {
        MotherEditForm form = service.findEntityByField(MotherEditForm.class,
                "id", 3);
        if (form != null) {
            service.saveOrUpdate(form);
        }
    }

    @RequestMapping(value = "/createFlw", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void createFlw() {
        Flw flw = service.findEntityByField(Flw.class, "flwId",
                "8f5a275380f60fa6e54fd8b17ec2ae79");
        if (flw == null) {
            flw = new Flw();
            flw.setFlwId("8f5a275380f60fa6e54fd8b17ec2ae79");
        }
        Set<Flw> flws = new HashSet<Flw>();
        flws.add(flw);
        FlwGroup group1 = service.findEntityByField(FlwGroup.class, "groupId",
                "1");
        if (group1 == null) {
            group1 = new FlwGroup();
            group1.setGroupId("1");
        }
        FlwGroup group2 = service.findEntityByField(FlwGroup.class, "groupId",
                "2")

        ;
        if (group2 == null) {
            group2 = new FlwGroup();
            group2.setGroupId("2");
        }
        FlwGroup group3 = service.findEntityByField(FlwGroup.class, "groupId",
                "3");
        if (group3 == null) {
            group3 = new FlwGroup();
            group3.setGroupId("3");
        }
        Set<FlwGroup> groupset = new HashSet<FlwGroup>();

        groupset.add(group2);
        groupset.add(group1);
        groupset.add(group3);
        flw.setFlwGroups(groupset);
        service.saveOrUpdate(flw);

        groupset.remove(group2);
        flw.setFlwGroups(groupset);
        service.saveOrUpdate(flw);

        FlwGroup group4 = service.findEntityByField(FlwGroup.class, "groupId",
                "4");
        if (group4 == null) {
            group4 = new FlwGroup();
            group4.setGroupId("4");
        }
        groupset.add(group4);
        flw.setFlwGroups(groupset);
        service.saveOrUpdate(flw);

        groupset.add(group2);
        flw.setFlwGroups(groupset);
        service.saveOrUpdate(flw);

    }

    @ExceptionHandler(value = { RestException.class })
    @ResponseBody
    public BeneficiaryError restExceptionHandler(RestException ex,
            HttpServletResponse response) {
        LOGGER.error(ex.getMessage(), ex);
        BeneficiaryError error = new BeneficiaryError();

        try {
            response.setStatus(ex.getHttpStatus().value());
            error.setErrorCode(String.valueOf(ex.getBatchException()
                    .getErrorCode()));
            error.setErrorMessage(ex.getBatchException().getErrorMessage());
            error.setApplication("motech-platform-batch");

        } catch (Exception e) {
            LOGGER.error("in last exception");
        }
        return error;
    }
}
