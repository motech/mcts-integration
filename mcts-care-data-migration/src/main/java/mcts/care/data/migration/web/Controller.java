package mcts.care.data.migration.web;
import mcts.care.data.migration.CareDataMigrator;

import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Controller {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(Controller.class);

	@Autowired
    private PropertyReader propertyReader;
	@Autowired
	private CareDataMigrator careDataMigrator;
	
	
	@RequestMapping(value = "sync", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String sync() {
		String updateCsvFileLocation = propertyReader.getSyncCsvFileLocation();
		LOGGER.debug("Csv File Location is: " + updateCsvFileLocation);
		careDataMigrator.sync(updateCsvFileLocation);
		return "Csv Upload SUCCESSFUL";
	}
}
