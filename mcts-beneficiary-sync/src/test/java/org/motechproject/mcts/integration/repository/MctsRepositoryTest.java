package org.motechproject.mcts.integration.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.repository.MdsRepository;
import org.motechproject.mds.query.Property;
import org.motechproject.mds.query.QueryExecution;

@RunWith(MockitoJUnitRunner.class)
public class MctsRepositoryTest {

    @Mock
    private MdsRepository dbRepository;

    @InjectMocks
    private MctsRepository careDataRepository = new MctsRepository();

    List<MctsPregnantMother> motherList;

    MotherCase motherCase = new MotherCase();

    MctsHealthworker mctsHealthworker = new MctsHealthworker();

    List<MctsPhc> phcList = new ArrayList<MctsPhc>();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        careDataRepository.setDbRepository(dbRepository);
        motherList = new ArrayList<MctsPregnantMother>();

        MctsPregnantMother mother1 = new MctsPregnantMother();
        mother1.setName("soniya devi");
        mother1.setFatherHusbandName("Dharmandra Sada");
        mother1.setHindiName("soniya devi");
        mother1.setHindiFatherHusbandName("Dharmandra Sada");

        MctsPregnantMother mother2 = new MctsPregnantMother();
        mother2.setName("Ranju Devi");
        mother2.setFatherHusbandName("Dilkush Kamat");
        mother2.setHindiName("Ranju Devi");
        mother2.setHindiFatherHusbandName("Dilkush Kamat");

        MctsPhc mctsPhc = new MctsPhc();
        mctsPhc.setName("SaurBazar");
        mctsPhc.setPhcId(175);

        phcList.add(mctsPhc);

        mctsHealthworker.setHealthworkerId(69735);
        mctsHealthworker.setMctsPhc(mctsPhc);
        mctsHealthworker.setType("ASHA");
        mctsHealthworker.setName("abc");
        mctsHealthworker.setSex("");
        mctsHealthworker.setCareGroupid("123");

        motherCase.setAge(30);

        motherList.add(mother1);
        motherList.add(mother2);
    }

    @Test
    public void getMctsPregnantMotherForClosedCasesTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(motherList);
        careDataRepository.getMctsPregnantMotherForClosedCases();
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void matchMctsPersonawithMotherCaseTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.matchMctsPersonawithMotherCase(1, 2, "3");
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void getMctsPregnantMotherTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(motherList);
        careDataRepository.getMctsPregnantMother();
        verify(dbRepository).executeJDO(any(Class.class),
                (QueryExecution<List>)anyObject());
    }

    @Test
    public void findUniqueVillageTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.findUniqueVillage(1, 2, 3);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void findUniqueDistrictTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.findUniqueDistrict(1, 2);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void findUniqueTalukTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.findUniqueTaluk(1, 2);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void findUniqueHealthBlockTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.findUniqueHealthBlock(1, 2);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void findUniquePhcTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(phcList);
        MctsPhc phc = careDataRepository.findUniquePhc(1, 2);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
        assertEquals("SaurBazar", phc.getName());

    }

    @Test
    public void findUniqueSubcentreTest() {
        when(
                dbRepository.executeJDO(any(Class.class),
                        (List<Property>) anyObject())).thenReturn(null);
        careDataRepository.findUniqueSubcentre(1, 2);
        verify(dbRepository).executeJDO(any(Class.class),
                (List<Property>) anyObject());
    }

    @Test
    public void getCaseGroupIdfromAshaIdTest() {
        when(dbRepository.get(any(Class.class), anyString(), anyInt()))
                .thenReturn(mctsHealthworker);
        String groupId = careDataRepository.getCaseGroupIdfromAshaId(69735);
        assertEquals("123", groupId);

    }

    @Test
    public void findEntityByFieldWithConstarintTest() {
        when(
                careDataRepository
                        .findEntityByFieldWithConstarint(any(Class.class),
                                anyString(), anyObject(), anyObject()))
                .thenReturn(motherList);
        List<MctsPregnantMother> list = careDataRepository
                .findEntityByFieldWithConstarint(MctsPregnantMother.class,
                        "creationTime", 123, 456);
        verify(dbRepository).findEntitiesByFieldWithConstraint(
                any(Class.class), anyString(), anyObject(), anyObject());
        assertEquals("soniya devi", list.get(0).getName());
    }

}
