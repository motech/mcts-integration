package org.motechproject.mcts.integration.service;

import java.util.regex.Pattern;

import org.motechproject.mcts.integration.beneficiary.sync.factory.BeneficiarySyncServiceFactory;
import org.motechproject.mcts.integration.constants.SyncType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
@Component
public class BeneficiarySyncLauncher {

	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";
	private static final String VALID_DATE_PATTERN = "^\\d{2}-\\d{2}-\\d{4}$";
	private static final String APPLICATION_CONTEXT_XML = "applicationBeneficiarySyncContext.xml";
	
	static BeneficiarySyncServiceFactory beneficiarySyncServiceFactory;
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncLauncher.class);

/*	@Autowired
	public BeneficiarySyncLauncher(){}
*/
	public static void syncLauncher(String[] syncArgs) {
		validateArguments(syncArgs);
		LOGGER.info("Arguments are Valid");
		SyncType syncType = SyncType.from(syncArgs[0]);
		DateTime startDate = parseDate(syncArgs[1]);
		DateTime endDate = parseDate(syncArgs[2]);

		beneficiarySyncServiceFactory = getBeneficiarySyncServiceFactory();
		BeneficiarySyncService beneficiarySyncService = beneficiarySyncServiceFactory.getBeneficiarySyncService(syncType);
		beneficiarySyncService.syncBeneficiaryData(startDate, endDate);
	}

	private static BeneficiarySyncServiceFactory getBeneficiarySyncServiceFactory() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setConfigLocation(APPLICATION_CONTEXT_XML);
		context.refresh();
		return (BeneficiarySyncServiceFactory) context
				.getBean("beneficiarySyncServiceFactory");
	}

	private static DateTime parseDate(String dateString) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern(DATE_TIME_FORMAT);
		return dateTimeFormatter.parseDateTime(dateString);
	}

	private static void validateArguments(String[] syncArgs) {
		if (syncArgs.length != 3)
			throw new IllegalArgumentException(
					"Invalid arguments. Expected 3 arguments(in order): SyncType, StartTime and EndTime");

		validateSyncType(syncArgs[0]);
		validateDateFormat(syncArgs);
	}

	private static void validateSyncType(String syncType) {
		if (!SyncType.isValid(syncType))
			throw new IllegalArgumentException(String.format(
					"Invalid sync type. Sync type should be %s or %s",
					SyncType.GET.getDescription(),
					SyncType.UPDATE.getDescription()));
	}

	private static void validateDateFormat(String[] syncArgs) {
		for (int argCounter = 1; argCounter < syncArgs.length; argCounter++) {
			if (!Pattern.matches(VALID_DATE_PATTERN, syncArgs[argCounter]))
				throw new IllegalArgumentException(String.format(
						"Invalid date format. Date format should be: %s.",
						DATE_TIME_FORMAT));
		}
	}
}
