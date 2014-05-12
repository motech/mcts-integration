package mcts.care.data.migration.service;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import mcts.care.data.migration.exception.DataMigrationException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.mcts.integration.service.CareDataService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest(FileUtils.class)
@RunWith(PowerMockRunner.class)
public class DataMigrationServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private CareDataService careDataService;

    private DataMigrationService dataMigrationService;

  //  @Before
//    public void setUp() throws Exception {
//        initMocks(this);
//        dataMigrationService = new DataMigrationService(careDataService);
//    }

    @Test
    public void shouldValidateIfGivenFilePathIsADirectory() {
        File file = FileUtils.getTempDirectory();
        String path = file.getPath();

        expectedException.expect(DataMigrationException.class);
        expectedException.expectMessage(String.format("Invalid file. Given path %s is a directory.", path));

        dataMigrationService.migrate(path);
    }

    @Test
    public void shouldValidateIfFileIsReadable() {
        String filePath = "some_path";
        PowerMockito.mockStatic(FileUtils.class);
        File file = mock(File.class);
        PowerMockito.when(FileUtils.getFile(filePath)).thenReturn(file);
        when(file.getPath()).thenReturn(filePath);
        when(file.isDirectory()).thenReturn(false);
        when(file.canRead()).thenReturn(false);

        expectedException.expect(DataMigrationException.class);
        expectedException.expectMessage(String.format("Cannot read file: %s", filePath));

        dataMigrationService.migrate(filePath);
    }

    @Test
    public void shouldValidateFileContentsIfCaseIdAndMctsIdIsSplitByCommaSeperator() throws IOException {
        testFileContents("case_id-mcts_id");
    }

    @Test
    public void shouldValidateFileContentsIfThereAreMultipleCaseIdAndMctsIdInSameLine() throws IOException {
        testFileContents("case_id1,mcts_id1,case_id2");
    }

    @Test
    public void shouldValidateFileContentsIfCaseIdAndMctsIdIsNotBlank() throws IOException {
        testFileContents("case_id,");

        testFileContents(",mcts_id");

        testFileContents(",");

        testFileContents("");
    }

    @Test
    public void shouldUpdateCaseForValidContents() throws IOException {
        File migrationFile = Files.createTempFile("data_migration", "txt").toFile();
        migrationFile.deleteOnExit();
        String content = "case_id1,mcts_id1\ncase-id2,mcts_id2";
        FileUtils.write(migrationFile, content);

        dataMigrationService.migrate(migrationFile.getPath());

        ArgumentCaptor<String> caseIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mctsIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(careDataService, times(2)).mapMotherCaseToMCTSPregnantMother(caseIdCaptor.capture(), mctsIdCaptor.capture());
        List<String> actualCaseIds = caseIdCaptor.getAllValues();
        List<String> actualMctsIds = mctsIdCaptor.getAllValues();
        assertEquals("case_id1", actualCaseIds.get(0));
        assertEquals("mcts_id1", actualMctsIds.get(0));
        assertEquals("case-id2", actualCaseIds.get(1));
        assertEquals("mcts_id2", actualMctsIds.get(1));
    }

    private void testFileContents(String content) throws IOException {
        File migrationFile = Files.createTempFile("data_migration", "txt").toFile();
        migrationFile.deleteOnExit();
        FileUtils.write(migrationFile, content);

        expectedException.expect(DataMigrationException.class);
        expectedException.expectMessage(String.format("Invalid record in file content: %s. Sample record format: <case_id1,mcts_id1>. All such records should be seperated by new line.", content));

        dataMigrationService.migrate(migrationFile.getPath());
    }
}
