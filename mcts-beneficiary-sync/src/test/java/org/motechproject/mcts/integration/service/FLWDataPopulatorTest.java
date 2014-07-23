package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.model.Location;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class FLWDataPopulatorTest {
	
	private FLWDataPopulator fLWDataPopulator = new FLWDataPopulator();
	FLWDataPopulator fLWDataPopulatorSpy = Mockito.spy(fLWDataPopulator);
	@Mock
	CareDataRepository careDataRepository;
	@Mock
	private PropertyReader propertyReader;
	@Mock 
	LocationDataPopulator locationDataPopulator;
	Location location = new Location();
	private MctsPhc mctsPhc;

	@Before
	public void setUp() throws Exception {
		mctsPhc = new MctsPhc();
		mctsPhc.setId(10);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(175);
		when(careDataRepository.getMctsPhc(175)).thenReturn(mctsPhc);
		when(
				careDataRepository.findEntityByField(MctsHealthworker.class,
						"healthworkerId", 69735)).thenReturn(null);
		Mockito.doNothing().when(locationDataPopulator).addLocationToDb((LocationDataCSV)any(), anyBoolean());
		 Mockito.doNothing().when(fLWDataPopulatorSpy).flwDataPopulator((FLWDataCSV)any());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToDatabase() throws Exception {
	    Mockito.doReturn(location).when(fLWDataPopulatorSpy).getUniqueLocation((LocationDataCSV)any());
	    Mockito.doNothing().when(fLWDataPopulatorSpy).addFLWToDb((FLWDataCSV)any(),anyString());
		@SuppressWarnings("resource")
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/FLW2.csv")));
		MockMultipartFile multipartFile = new MockMultipartFile(
		        "FLW2.csv",                //filename
		        content.getBytes());
		fLWDataPopulatorSpy.populateFLWData(multipartFile, "10");
		Mockito.verify(fLWDataPopulatorSpy, Mockito.times(1)).addFLWToDb((FLWDataCSV)any(),anyString());
		Mockito.verify(fLWDataPopulatorSpy, Mockito.times(1)).flwDataPopulator((FLWDataCSV)any());

	}

	
}
