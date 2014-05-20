package org.motechproject.mcts.integration.service;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class FLWDataPopulatorTest 
{
	@InjectMocks
	private FLWDataPopulator fLWDataPopulator;
	@Mock CareDataRepository careDataRepository;
	 @Mock
	 private PropertyReader propertyReader;
	private MctsPhc mctsPhc;
	@Before
	public void setUp() {
		MctsPhc mctsPhc = new MctsPhc();
		mctsPhc.setId(175);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(10);
		
		
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToDatabase() throws Exception {
		when(careDataRepository.getMctsPhc((Integer) any())).thenReturn(mctsPhc);
		fLWDataPopulator.populateFLWData();
		ArgumentCaptor<MctsHealthworker> captor = ArgumentCaptor
				.forClass(MctsHealthworker.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsHealthworker mctsHealthworker = captor.getValue();
		assertEquals("SaurBazar", mctsHealthworker.getMctsPhc().getName());
		assertEquals(10, mctsHealthworker.getMctsPhc().getPhcId());
		assertEquals((Integer)175, mctsHealthworker.getMctsPhc().getId());
		verify(careDataRepository).saveOrUpdate((MctsHealthworker)any());
		verify(careDataRepository, times(1)).saveOrUpdate((MctsHealthworker)any());
		
	}
}
