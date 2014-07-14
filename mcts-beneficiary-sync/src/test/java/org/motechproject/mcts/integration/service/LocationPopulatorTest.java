package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

import java.io.File;
import java.lang.Object;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsLocationErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class LocationPopulatorTest {
	@InjectMocks 
	private LocationDataPopulator locationDataPopulator = new LocationDataPopulator();
	
	@Mock
	CareDataRepository careDataRepository;
	
	@Mock
	private PropertyReader propertyReader;
	
	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	@Ignore
	public void shouldSyncCsvDataToLocationMaster() throws Exception {
		File file = new File(propertyReader.getFLWCsvFileLocation()); //TODO Aman get from test res
		//locationDataPopulator.saveLocationData(file);
		ArgumentCaptor<MctsLocationErrorLog> captor = ArgumentCaptor
				.forClass(MctsLocationErrorLog.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsLocationErrorLog mctsLocationMaster = captor.getValue();
		assertEquals("Saur Bazar",mctsLocationMaster.getTalukaname());
		assertEquals("Saor Bazar", mctsLocationMaster.getBlock());
		verify(careDataRepository).saveOrUpdate((MctsLocationErrorLog)any());
		verify(careDataRepository, times(1)).saveOrUpdate((MctsLocationErrorLog)any());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	@Ignore
	public void shouldSyncCsvDataToLocation() throws Exception {
		when(careDataRepository.findEntityByField(Object.class, eq((String)any()), (Object)any())).thenReturn(null);
		File file = new File(propertyReader.getFLWCsvFileLocation());//TODO Aman - change proeprty
		//locationDataPopulator.populateLocations(file);
		ArgumentCaptor<Object> captor = ArgumentCaptor
				.forClass(Object.class);
		verify(careDataRepository, times(7)).saveOrUpdate(captor.capture());
		Object object = captor.getValue();
		//assertEquals("Bihar",mctsState.getName());
		//verify(careDataRepository).saveOrUpdate((Object)any());
		verify(careDataRepository, times(7)).saveOrUpdate((Object)any());
		
	}
}
