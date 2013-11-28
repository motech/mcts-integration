package mcts.integration.beneficiary.sync;

import mcts.integration.beneficiary.sync.service.BeneficiarySyncService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.regex.Pattern;

public class BeneficiarySyncLauncher {

    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy";
    private static final String APPLICATION_CONTEXT_XML = "beneficiarySyncContext.xml";


    public static void main(String[] dateArgs) {
        validateArguments(dateArgs);

        DateTime startDate = parseDate(dateArgs[0]);
        DateTime endDate = parseDate(dateArgs[1]);

        BeneficiarySyncService beneficiarySyncService = getBeneficiarySyncService();
        beneficiarySyncService.syncBeneficiaryData(startDate, endDate);
    }

    private static BeneficiarySyncService getBeneficiarySyncService() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation(APPLICATION_CONTEXT_XML);
        context.refresh();
        return (BeneficiarySyncService) context.getBean("beneficiarySyncService");
    }

    private static DateTime parseDate(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
        return dateTimeFormatter.parseDateTime(dateString);
    }

    private static void validateArguments(String[] dateArgs) {
        if (dateArgs.length != 2)
            throw new IllegalArgumentException("Invalid arguments. Only 2 arguments: StartTime and EndTime are allowed.");
        for (String date : dateArgs) {
            if (!Pattern.matches("^\\d{2}-\\d{2}-\\d{4}$", date))
                throw new IllegalArgumentException(String.format("Invalid date format. Date format should be: %s.", DATE_TIME_FORMAT));
        }
    }
}
