package org.motechproject.mcts.integration.service;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsFlwData;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FLWDataPopulator;
import org.motechproject.mcts.utils.PropertyReader;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class FLWDataPopulatorTest 
{
	@InjectMocks
	private FLWDataPopulator fLWDataPopulator = new FLWDataPopulator();
	@Mock 
	CareDataRepository careDataRepository;
	@Mock
	private PropertyReader propertyReader;
	private MctsPhc mctsPhc;
	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	@Ignore
	public void shouldSyncCsvDataToDatabase() throws Exception {
		MctsPhc mctsPhc = new MctsPhc();
		mctsPhc.setId(10);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(175);
		when(careDataRepository.getMctsPhc(175)).thenReturn(mctsPhc);
		when(careDataRepository.findEntityByField(MctsHealthworker.class, "healthworkerId", 69735)).thenReturn(null);
		File file = new File(propertyReader.getFLWCsvFileLocation()); //TODO Aman - get from test resources
		//fLWDataPopulator.populateFLWData(file);
		ArgumentCaptor<MctsHealthworker> captor = ArgumentCaptor
				.forClass(MctsHealthworker.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsHealthworker mctsHealthworker = captor.getValue();
		assertEquals("SaurBazar", mctsHealthworker.getMctsPhc().getName());
		assertEquals("ASHA",mctsHealthworker.getType());
		assertEquals(175, mctsHealthworker.getMctsPhc().getPhcId());
		assertEquals((Integer)10, mctsHealthworker.getMctsPhc().getId());
		verify(careDataRepository).saveOrUpdate((MctsHealthworker)any());
		verify(careDataRepository, times(1)).saveOrUpdate((MctsHealthworker)any());
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	@Ignore
	public void shouldSyncCsvDataToflw() throws Exception {
		MctsPhc mctsPhc = new MctsPhc();
		mctsPhc.setId(10);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(175);
		when(careDataRepository.getMctsPhc(175)).thenReturn(mctsPhc);
		File file = new File(propertyReader.getFLWCsvFileLocation());//TODOAman chang to test res
		fLWDataPopulator.flwDataPopulator(file);
		ArgumentCaptor<MctsFlwData> captor = ArgumentCaptor
				.forClass(MctsFlwData.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsFlwData mctsFlwData = captor.getValue();
		assertEquals("Anita Kumari",mctsFlwData.getName());
		assertEquals("ASHA",mctsFlwData.getType());
		verify(careDataRepository).saveOrUpdate((MctsHealthworker)any());
		verify(careDataRepository, times(1)).saveOrUpdate((MctsHealthworker)any());
		
	}
}
