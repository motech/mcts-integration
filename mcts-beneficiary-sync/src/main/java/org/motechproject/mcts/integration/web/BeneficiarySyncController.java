package org.motechproject.mcts.integration.web;

import org.motechproject.mcts.integration.service.BeneficiarySyncLauncher;
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

	@Autowired
    private PropertyReader propertyReader;
/*	
	@Autowired
	private CareDataMigrator careDataMigrator;
*/
	@RequestMapping(value = "getXml", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String getXml(@RequestParam("service") String service,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) throws Exception {
		String[] stringArgs = new String[3];
		stringArgs[0] = service; LOGGER.debug("Requested Service is:" + service);
		stringArgs[1] = startDate; LOGGER.debug("Requested StartDate is:" + startDate);
		stringArgs[2] = endDate; LOGGER.debug("Requested endDate is:" + endDate);
		BeneficiarySyncLauncher.syncLauncher(stringArgs);
		return "XML Generation SUCCESSFUL";
	}
/*	
	@RequestMapping(value = "sync", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String sync() {
		String updateCsvFileLocation = propertyReader.getSyncCsvFileLocation();
		LOGGER.debug("Csv File Location is: " + updateCsvFileLocation);
		careDataMigrator.sync(updateCsvFileLocation);
		return "Csv Upload SUCCESSFUL";
	}*/
}
