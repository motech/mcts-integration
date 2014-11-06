package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworkerErrorLog;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsState;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class FLWDataPopulatorTest {

    @InjectMocks
    private FLWDataPopulator fLWDataPopulator = new FLWDataPopulator();

    @Mock
    MctsRepository careDataRepository;
    @Mock
    private PropertyReader propertyReader;
    @Mock
    LocationDataPopulator locationDataPopulator;

    @Mock
    private CareDataService careDataService;
    

    private MctsState mctsState;
    private MctsDistrict mctsDistrict;
    private MctsTaluk mctsTaluk;
    private MctsHealthblock mctsHealthblock;
    private MctsPhc mctsPhc;
    private MctsSubcenter mctsSubcenter;
    private MctsVillage mctsVillage;
    List<MctsDistrict> listDistrict = new ArrayList<MctsDistrict>();
    List<MctsTaluk> listTaluk = new ArrayList<MctsTaluk>();
    List<MctsVillage> listVillage = new ArrayList<MctsVillage>();
    List<MctsHealthblock> listBlock = new ArrayList<MctsHealthblock>();
    List<MctsPhc> listPhc = new ArrayList<MctsPhc>();
    List<MctsSubcenter> listSubcentre = new ArrayList<MctsSubcenter>();

    @Before
    public void setUp() throws Exception {
        mctsState = new MctsState(10, "Bihar");
        mctsDistrict = new MctsDistrict(mctsState, 11, "Saharsa");
        mctsTaluk = new MctsTaluk(mctsDistrict, 12, "Saur Bazar");
        mctsHealthblock = new MctsHealthblock(mctsTaluk, 13, "Saor Bazar");
        mctsPhc = new MctsPhc(mctsHealthblock, 14, "saur bazar");
        mctsSubcenter = new MctsSubcenter(mctsPhc, 15, "saor bazar");
        mctsVillage = new MctsVillage(mctsTaluk, mctsSubcenter, 16, "random");
        listDistrict.add(mctsDistrict);
        listTaluk.add(mctsTaluk);
        listVillage.add(mctsVillage);
        listBlock.add(mctsHealthblock);
        listPhc.add(mctsPhc);
        listSubcentre.add(mctsSubcenter);
        
        when(
                careDataRepository.findEntityByField(MctsHealthworker.class,
                        "healthworkerId", 69735)).thenReturn(null);

    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void shouldSyncCsvDataToDatabase() throws Exception {
        when(careDataService.findEntityByField(MctsState.class, "stateId", 10))
                .thenReturn(mctsState);
        when(
                careDataService.findListOfEntitiesByMultipleField(
                        any(Class.class), anyMap()))
                .thenReturn(listDistrict).thenReturn(listTaluk)
                .thenReturn(listVillage).thenReturn(listBlock)
                .thenReturn(listPhc).thenReturn(listSubcentre);
        @SuppressWarnings("resource")
        String content = new String(Files.readAllBytes(Paths
                .get("src/test/resources/FLW2.csv")));
        MockMultipartFile multipartFile = new MockMultipartFile("FLW2.csv", // filename
                content.getBytes());
        fLWDataPopulator.populateFLWData(multipartFile, "10");
        verify(careDataRepository, times(2)).saveOrUpdate(anyObject());

    }

}
