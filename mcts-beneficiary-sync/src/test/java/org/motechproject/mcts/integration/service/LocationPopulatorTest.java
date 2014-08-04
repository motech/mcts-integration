package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class LocationPopulatorTest {
    
    @InjectMocks
	private LocationDataPopulator locationDataPopulator = new LocationDataPopulator();

	@Mock
	CareDataRepository careDataRepository;

	@Mock
	private PropertyReader propertyReader;
	
	private MctsState mctsState;
	private MctsDistrict mctsDistrict;
	private MctsTaluk mctsTaluk;
	private MctsHealthblock mctsHealthblock;
	private MctsPhc mctsPhc;
	private MctsSubcenter mctsSubcenter;
	private MctsVillage mctsVillage;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		  
		  
		  mctsState = new MctsState(10, "Bihar");
		  mctsDistrict = new MctsDistrict(mctsState, 11, "Saharsa");
		  mctsTaluk = new MctsTaluk(mctsDistrict, 12, "Saur Bazar");
		  mctsHealthblock = new MctsHealthblock(mctsTaluk, 13, "Saor Bazar");
		  mctsPhc = new MctsPhc(mctsHealthblock, 14, "saur bazar");
		  mctsSubcenter = new MctsSubcenter(mctsPhc, 15, "saor bazar");
		  mctsVillage = new MctsVillage(mctsTaluk, mctsSubcenter, 16, "random");
		  
		  
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToLocationMaster() throws Exception {
	  
		
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/location2.csv")));
        MockMultipartFile multipartFile = new MockMultipartFile(
                "location2.csv",                //filename
                content.getBytes());
	
        locationDataPopulator.populateLocations(multipartFile);
        verify(careDataRepository,times(8)).saveOrUpdate(anyObject());
		
	}
	
	
	
	

	
}
