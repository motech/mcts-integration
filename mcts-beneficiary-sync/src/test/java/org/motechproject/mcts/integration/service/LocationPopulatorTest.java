package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.io.File;
import java.lang.Object;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsLocationMaster;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.repository.CareDataRepository;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class LocationPopulatorTest {
	@InjectMocks 
	private LocationDataPopulator locationDataPopulator;
	
	@Mock
	CareDataRepository careDataRepository;
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToLocationMaster() throws Exception {
		File file = new File("/home/aman/Downloads/location2.csv");
		locationDataPopulator.saveLocationData(file);
		ArgumentCaptor<MctsLocationMaster> captor = ArgumentCaptor
				.forClass(MctsLocationMaster.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsLocationMaster mctsLocationMaster = captor.getValue();
		assertEquals("Saur Bazar",mctsLocationMaster.getTalukaname());
		assertEquals("Saor Bazar", mctsLocationMaster.getBlock());
		verify(careDataRepository).saveOrUpdate((MctsLocationMaster)any());
		verify(careDataRepository, times(1)).saveOrUpdate((MctsLocationMaster)any());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToLocation() throws Exception {
		when(careDataRepository.findEntityByField(Object.class, eq((String)any()), (Object)any())).thenReturn(null);
		File file = new File("/home/aman/Downloads/location2.csv");
		//TODO Aman locationDataPopulator.populateLocations(file);
		ArgumentCaptor<Object> captor = ArgumentCaptor
				.forClass(Object.class);
		verify(careDataRepository, times(7)).saveOrUpdate(captor.capture());
		Object object = captor.getValue();
		//assertEquals("Bihar",mctsState.getName());
		//verify(careDataRepository).saveOrUpdate((Object)any());
		verify(careDataRepository, times(7)).saveOrUpdate((Object)any());
		
	}
}
