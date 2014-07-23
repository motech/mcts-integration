package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.io.File;
import java.lang.Object;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsLocationErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.model.FLWDataCSV;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class LocationPopulatorTest {
    
	private LocationDataPopulator locationDataPopulator = new LocationDataPopulator();
	LocationDataPopulator locationDataPopulatorSpy = Mockito.spy(locationDataPopulator);

	@Mock
	CareDataRepository careDataRepository;

	@Mock
	private PropertyReader propertyReader;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		  Mockito.doNothing().when(locationDataPopulatorSpy).addLocationToDb((LocationDataCSV)any(),anyBoolean());
		  Mockito.doNothing().when(locationDataPopulatorSpy).saveLocationData((LocationDataCSV)any());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToLocationMaster() throws Exception {
	  
		
		String content = new String(Files.readAllBytes(Paths.get("src/test/resources/location2.csv")));
        MockMultipartFile multipartFile = new MockMultipartFile(
                "location2.csv",                //filename
                content.getBytes());
	
        locationDataPopulatorSpy.populateLocations(multipartFile);
        Mockito.verify(locationDataPopulatorSpy, Mockito.times(1)).addLocationToDb((LocationDataCSV)any(),anyBoolean());
        Mockito.verify(locationDataPopulatorSpy, Mockito.times(1)).saveLocationData((LocationDataCSV)any());
		
	}
	
	

	
}
