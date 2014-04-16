package mcts.integration.beneficiary.sync.launcher;

import mcts.integration.beneficiary.sync.factory.BeneficiarySyncServiceFactory;

import org.springframework.beans.factory.annotation.Autowired;
import mcts.integration.beneficiary.sync.service.BeneficiarySyncService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.regex.Pattern;

public class BeneficiarySyncLauncher {

	private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";
	private static final String VALID_DATE_PATTERN = "^\\d{2}-\\d{2}-\\d{4}$";
	private static final String APPLICATION_CONTEXT_XML = "beneficiarySyncContext.xml";
	
	//@Autowired
	public BeneficiarySyncLauncher(){}

	public static void main(String[] syncArgs) {
		validateArguments(syncArgs);

		SyncType syncType = SyncType.from(syncArgs[0]);
		DateTime startDate = parseDate(syncArgs[1]);
		DateTime endDate = parseDate(syncArgs[2]);

		BeneficiarySyncServiceFactory beneficiarySyncServiceFactory = getBeneficiarySyncServiceFactory();
		BeneficiarySyncService beneficiarySyncService = beneficiarySyncServiceFactory
				.getBeneficiarySyncService(syncType);
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
