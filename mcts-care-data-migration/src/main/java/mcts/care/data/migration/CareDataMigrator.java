package mcts.care.data.migration;

import mcts.care.data.migration.service.DataMigrationService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CareDataMigrator {

    private static final String APPLICATION_CONTEXT_XML = "careDataMigrationContext.xml";

    public static void main(String[] args) {
        validateArguments(args);
        String filePath = args[0];

        DataMigrationService dataMigrationService = getDataMigrationService();
        dataMigrationService.migrate(filePath);
    }

    private static DataMigrationService getDataMigrationService() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation(APPLICATION_CONTEXT_XML);
        context.refresh();
        return (DataMigrationService) context.getBean("dataMigrationService");
    }

    private static void validateArguments(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Invalid arguments. Expected only one argument: absolute filepath");
    }
}
