package org.motechproject.mcts.integration.service;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.repository.CareDataRepository;

@RunWith(MockitoJUnitRunner.class)
public class MCTSFormUpdateServiceTest {
	@InjectMocks
	private MCTSFormUpdateService mCTSFormUpdateService;
	
	@Mock
	CareDataRepository careDataRepository;
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldupdateMCTSStatusesfromRegForm() {
		//List<Object[]> casemctsId = {{17661,"109283019411234567"}};
		List<Object[]> casemctsIds = new ArrayList<Object[]>();
		
		Object i = (Object)new Integer(17661);
		Object str = (Object)new String("109283019411234567");
		Object[] ob = {i,str};
		casemctsIds.add(ob);
		
		when(careDataRepository.getmctsIdcaseId()).thenReturn(casemctsIds);
		mCTSFormUpdateService.updateMCTSStatusesfromRegForm();
		
		verify(careDataRepository, times(1)).updateQuery((String)any());
	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldUpdateMCTSStatusfromEditform() {
		List<Object[]> caseIds = new ArrayList<Object[]>();
		Object i = (Object)new Integer(17661);
		Object str = (Object)new String("109283019411234567");
		Object[] ob = {i,str};
		caseIds.add(ob);
		
		when(careDataRepository.getmctsIdcaseIdfromEditForm()).thenReturn(caseIds);
		mCTSFormUpdateService.updateMCTSStatusfromEditForm();
		verify(careDataRepository, times(1)).updateQuery((String)any());
	}

}
