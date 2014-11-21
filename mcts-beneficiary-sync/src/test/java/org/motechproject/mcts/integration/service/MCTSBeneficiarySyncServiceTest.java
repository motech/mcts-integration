package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.mcts.care.common.mds.measure.DontKnowForm;
import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.model.MctsState;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.model.NewDataSet;
import org.motechproject.mcts.integration.model.Record;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.transliteration.hindi.service.TransliterationService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@PrepareForTest(FileUtils.class)
@RunWith(PowerMockRunner.class)
public class MCTSBeneficiarySyncServiceTest {

    @Mock
    private MCTSHttpClientService mctsHttpClientService;

    @Mock
    private Publisher publisher;

    @Mock
    private PropertyReader propertyReader;

    @Mock
    private MctsRepository careDataRepository;

    @Mock
    private CareDataService careDataService;

    @Mock
    private LocationDataPopulator locationDataPopulator;

    @Mock
    private TransliterationService transliterationService;

    @Mock
    private FixtureDataService fixtureDataService;

    @Mock
    private EventRelay eventRelay;

    @InjectMocks
    public MCTSBeneficiarySyncService mctsBeneficiarySyncService = new MCTSBeneficiarySyncService();
    MctsState state = new MctsState(10, "Bihar");
    List<MctsDistrict> listDistrict = new ArrayList<MctsDistrict>();
    List<MctsTaluk> listTaluk = new ArrayList<MctsTaluk>();
    List<MctsHealthblock> listBlock = new ArrayList<MctsHealthblock>();
    List<MctsPhc> listPhc = new ArrayList<MctsPhc>();
    List<MctsSubcenter> listSubcenter = new ArrayList<MctsSubcenter>();
    MctsPregnantMother mother = new MctsPregnantMother();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        MctsDistrict dis = new MctsDistrict(state, 12, "Saharsa");
        listDistrict.add(dis);
        MctsTaluk taluk = new MctsTaluk(dis, 0163, "Saur Bazar");
        listTaluk.add(taluk);
        MctsHealthblock block = new MctsHealthblock(taluk, 180, "SaorBazar");
        listBlock.add(block);
        MctsPhc phc = new MctsPhc(block, 175, "Saur Bazar");
        listPhc.add(phc);
        MctsSubcenter subCenter = new MctsSubcenter(phc, 175, "Tiri HSC");
        listSubcenter.add(subCenter);
        mother.setMctsId("101216300411300080");
        mother.setMctsPhc(phc);
        mother.setMctsVillage(null);
        mother.setMctsSubcenter(subCenter);
        when(careDataService.findEntityByField(MctsState.class, "stateId", 10))
                .thenReturn(state);

        when(transliterationService.transliterate(anyString())).thenReturn(
                "hindi");
        when(fixtureDataService.getCaseGroupIdfromAshaId(anyInt(), anyString()))
                .thenReturn("6345");
    }

    @Test
    public void shouldSyncBeneficiaryDataOfMCTSToMotech() throws Exception {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();

        MultiValueMap<String, String> requestBody = getRequestBody();
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(
                getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody))
                .thenReturn(response());

        NewDataSet response = mctsBeneficiarySyncService.syncFrom(startDate,
                endDate);

        verify(mctsHttpClientService).syncFrom(requestBody);
        assertEquals(response(), response);
    }

    @Test
    public void shouldWriteDataToFile() throws Exception {
        when(propertyReader.getSyncRequestOutputFileLocation()).thenReturn(
                "updateRequestXML");

        mctsBeneficiarySyncService.writeToFile(response().toString());
        verify(propertyReader).getSyncRequestOutputFileLocation();
    }

    @Test
    public void shouldNotProceedIfResponseIsNull() throws Exception {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        MultiValueMap<String, String> requestBody = getRequestBody();
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(
                getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(null);
        mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
        verify(careDataRepository, times(0)).findEntityByField((Class) any(),
                (String) any(), (Object) any());
        verify(publisher, times(0)).publish((String) any());
        verify(propertyReader, times(0)).getSyncRequestOutputFileLocation();
    }

    @Test
    public void syncBeneficiaryDataTest() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        MultiValueMap<String, String> requestBody = getRequestBody();
        when(
                careDataService.findListOfEntitiesByMultipleField(
                        any(Class.class), (Map<String, Object>) anyObject()))
                .thenReturn(listDistrict).thenReturn(listTaluk)
                .thenReturn(listBlock).thenReturn(listPhc)
                .thenReturn(listSubcenter);
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(
                getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(resp());
        mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
        ArgumentCaptor<MctsPregnantMother> captor = ArgumentCaptor
                .forClass(MctsPregnantMother.class);
        verify(careDataService).saveOrUpdate(captor.capture());
        MctsPregnantMother mother1 = captor.getValue();
        assertEquals("Ranju Devi", mother1.getName());
        assertEquals("101216300411300080", mother1.getMctsId());
        verify(locationDataPopulator).addLocationToDb((LocationDataCSV) anyObject(), anyBoolean());

    }

    @Test
    public void syncBeneficiaryDataTestForInvalidBeneficiary() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        MultiValueMap<String, String> requestBody = getRequestBody();
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(
                getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody))
                .thenReturn(response());
        String actual = mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
        assertEquals("All " + response().getRecords().size() +" received updates are already present in database or have some error. None added to database.", actual);
        verify(mctsHttpClientService).syncFrom(requestBody);
    }

    @Test
    public void syncBeneficiaryDataTestForAlreadyExistingRecord() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        MultiValueMap<String, String> requestBody = getRequestBody();
        when(
                careDataService.findEntityByField(MctsPregnantMother.class,
                        "mctsId", "101216300411300080")).thenReturn(mother);
        when(
                careDataService.findListOfEntitiesByMultipleField(
                        any(Class.class), (Map<String, Object>) anyObject()))
                .thenReturn(listDistrict).thenReturn(listTaluk)
                .thenReturn(listBlock).thenReturn(listPhc)
                .thenReturn(listSubcenter).thenReturn(listDistrict)
                .thenReturn(listTaluk).thenReturn(listBlock)
                .thenReturn(listPhc).thenReturn(listSubcenter);
        when(propertyReader.getDefaultBeneficiaryListQueryParams()).thenReturn(
                getDefaultQueryParams());
        when(mctsHttpClientService.syncFrom(requestBody)).thenReturn(resp());
        String actual = mctsBeneficiarySyncService.syncBeneficiaryData(startDate, endDate);
        assertEquals("All " + response().getRecords().size() +" received updates are already present in database or have some error. None added to database.", actual);
        verify(mctsHttpClientService).syncFrom(requestBody);
        verify(careDataService).findEntityByField(MctsPregnantMother.class,
                        "mctsId", "101216300411300080");
	}

    public MultiValueMap<String, String> getRequestBody() {
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.putAll(getDefaultQueryParams());
        requestBody.add("FromDate", startDate.toString("dd-MM-yyyy"));
        requestBody.add("ToDate", endDate.toString("dd-MM-yyyy"));
        return requestBody;
    }

    public MultiValueMap<String, String> getDefaultQueryParams() {
        MultiValueMap<String, String> defaultQueryParams = new LinkedMultiValueMap<>();
        defaultQueryParams.add("username", "myUser");
        return defaultQueryParams;
    }

    private NewDataSet response() {
        NewDataSet newDataSet = new NewDataSet();
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.setStateID("31");
        record.setStateName("Lakshadweep");
        record.setDistrictID("1");
        record.setDistrictName("Lakshadweep");
        records.add(record);
        newDataSet.setRecords(records);
        return newDataSet;
    }

    private NewDataSet resp() {
        NewDataSet newDataSet = new NewDataSet();
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.setBeneficiaryName("Ranju Devi");
        record.setBeneficiaryID("101216300411300080");
        record.setStateID("10");
        record.setDistrictID("12");
        record.setTehsilID("0163");
        record.setFacilityID("175");
        record.setSubCentreID("6357");
        record.setBlockID("180");
        records.add(record);
        newDataSet.setRecords(records);
        return newDataSet;
    }

}
