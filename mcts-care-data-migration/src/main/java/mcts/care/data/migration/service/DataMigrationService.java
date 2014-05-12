package mcts.care.data.migration.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import mcts.care.data.migration.exception.DataMigrationException;
import org.motechproject.mcts.integration.service.CareDataService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DataMigrationService {

	private static Properties properties;
	
    private static String DELIMITER;
	
    private static String VALID_FILE_CONTENT_LINE_FORMAT;

    private CareDataService careDataService;

    @Autowired
    public DataMigrationService(@Qualifier("careDataMigrationProperties") Properties properties,CareDataService careDataService) {
        DataMigrationService.properties=properties;
        this.careDataService = careDataService;
        DELIMITER = properties.getProperty("csv.field.delimiter");
        VALID_FILE_CONTENT_LINE_FORMAT = String.format(properties.getProperty("valid.file.content.line.fomat"), DELIMITER);
    }

    public void migrate(String filePath) {
        File migrationFile = FileUtils.getFile(filePath);
        validateFile(migrationFile);

        try {
            List<String> fileContents = FileUtils.readLines(migrationFile);
            validateContents(fileContents);
            for (String row : fileContents) {
                String[] rowContents = StringUtils.split(row, DELIMITER);
                String caseId = rowContents[0];
                String mctsId = rowContents[1];
                careDataService.mapMotherCaseToMCTSPregnantMother(caseId, mctsId);
            }
        } catch (IOException e) {
            throw new DataMigrationException(String.format("Error while processing file %s", filePath), e);
        }
    }

    private void validateFile(File file) {
        if (file.isDirectory())
            throw new DataMigrationException(String.format("Invalid file. Given path %s is a directory.", file.getPath()));

        if (!file.canRead())
            throw new DataMigrationException(String.format("Cannot read file: %s", file.getPath()));
    }

    private void validateContents(List<String> fileContents) {
        for (String fileContent : fileContents) {
            if(!Pattern.matches(VALID_FILE_CONTENT_LINE_FORMAT, fileContent))
                throw new DataMigrationException(String.format("Invalid record in file content: %s. Sample record format: <case_id1,mcts_id1>. All such records should be seperated by new line. The String is %s", fileContent, VALID_FILE_CONTENT_LINE_FORMAT));
        }
    }
}
