package mcts.integration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcts.integration.beneficiary.sync.launcher.BeneficiarySyncLauncher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/beneficiary")
public class BeneficiarySyncController {
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncController.class);

	/*private BeneficiarySyncLauncher beneficairySyncLauncher;

	@Autowired
	public BeneficiarySyncController(BeneficiarySyncLauncher beneficairySyncLauncher) {
		this.beneficairySyncLauncher = beneficairySyncLauncher;
	}
*/
	@RequestMapping(value = "sync", method = RequestMethod.GET)
	public void update(@RequestParam("service") String service,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) {
		String[] stringArgs = new String[3];
		stringArgs[0] = service; LOGGER.debug("Requested Service is:" + service);
		stringArgs[1] = startDate; LOGGER.debug("Requested StartDate is:" + startDate);
		stringArgs[2] = endDate; LOGGER.debug("Requested endDate is:" + endDate);
		BeneficiarySyncLauncher.syncLauncher(stringArgs);
	}
}
