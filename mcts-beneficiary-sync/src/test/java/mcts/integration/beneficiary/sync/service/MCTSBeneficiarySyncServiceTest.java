package mcts.integration.beneficiary.sync.service;

import mcts.integration.beneficiary.sync.settings.BeneficiarySyncSettings;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(FileUtils.class)
@RunWith(PowerMockRunner.class)
public class MCTSBeneficiarySyncServiceTest {
    @Mock
    private MCTSHttpClientService mctsHttpClientService;
    @Mock
    private BeneficiarySyncSettings beneficiarySyncSettings;

    private BeneficiarySyncService beneficiarySyncService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        beneficiarySyncService = new MCTSBeneficiarySyncService(mctsHttpClientService, beneficiarySyncSettings);
    }

    @Test
    public void shouldSyncBeneficiaryDataOfMCTSToMotechAndWriteToFile() throws Exception {
        PowerMockito.mockStatic(FileUtils.class);
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        String outputFileLocation = "fileLocation";
        String timeStampIgnoringSeconds = DateTime.now().toString("yyyy-MM-dd") + "T" + DateTime.now().toString("HH:mm");
        String expectedOutputFileLocation = String.format("%s_%s", outputFileLocation, timeStampIgnoringSeconds);
        MultiValueMap<String, String> defaultQueryParams = new LinkedMultiValueMap<>();
        defaultQueryParams.add("username", "myUser");
        when(beneficiarySyncSettings.getDefaultBeneficiaryListQueryParams()).thenReturn(defaultQueryParams);
        when(beneficiarySyncSettings.getSyncRequestOutputFileLocation()).thenReturn(outputFileLocation);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.putAll(defaultQueryParams);
        requestBody.add("FromDate", startDate.toString("dd-MM-yyyy"));
        requestBody.add("ToDate", endDate.toString("dd-MM-yyyy"));
        when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(response());

        beneficiarySyncService.syncBeneficiaryData(startDate, endDate);

        verify(mctsHttpClientService).syncFrom(requestBody);

        PowerMockito.verifyStatic(times(1));
        ArgumentCaptor<File> outputFileCaptor = ArgumentCaptor.forClass(File.class);
        FileUtils.writeStringToFile(outputFileCaptor.capture(), eq(response()));
        String actualOutputFileLocation = outputFileCaptor.getValue().getPath();
        assertTrue(actualOutputFileLocation.contains(expectedOutputFileLocation));
    }

    private String response() {
        return "<newdataset>\n" +
                "    <records>\n" +
                "            <stateid>31</stateid>\n" +
                "            <state_name>Lakshadweep</state_name>\n" +
                "            <district_id>1</district_id>\n" +
                "            <district_name>Lakshadweep</district_name>\n" +
                "            <block_id>1</block_id>\n" +
                "            <block_name>N'Amini'</block_name>\n" +
                "            <tehsil_id>0001</tehsil_id>\n" +
                "            <tehsil_name>AMINI</tehsil_name>\n" +
                "            <facility_id>2</facility_id>\n" +
                "    </records>\n" +
                "</newdataset>";
    }

}
