package mcts.care.data.migration.service;

import mcts.care.data.migration.exception.DataMigrationException;
import motech.care.data.service.CareDataService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class DataMigrationService {

    private static final String DELIMITER = ",";
    private static final String VALID_FILE_CONTENT_LINE_FORMAT = String.format("^[a-zA-Z0-9_-]+\\s*%s\\s*[a-zA-Z0-9_-]+$", DELIMITER);

    private CareDataService careDataService;

    @Autowired
    public DataMigrationService(CareDataService careDataService) {
        this.careDataService = careDataService;
    }

    public void migrate(String filePath) {
        File migrationFile = FileUtils.getFile(filePath);
        validateFile(migrationFile);

        try {
            List<String> fileContents = FileUtils.readLines(migrationFile);
            validateContents(fileContents);
            for (String row : fileContents) {
                String[] rowContents = StringUtils.split(row, DELIMITER);
                careDataService.updateCase(rowContents[0], rowContents[1]);
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
                throw new DataMigrationException(String.format("Invalid record in file content: %s. Sample record format: <case_id1,mcts_id1>. All such records should be seperated by new line.", fileContent));
        }
    }
}
