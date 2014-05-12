package mcts.care.data.migration;

import mcts.care.data.migration.service.DataMigrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CareDataMigrator {

    @Autowired
    private DataMigrationService dataMigrationService;

    public void sync(String filePath) {
        dataMigrationService.migrate(filePath);
    }
}