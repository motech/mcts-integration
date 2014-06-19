package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.integration.model.DataList;
import org.motechproject.mcts.integration.model.Fields;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.web.client.RestTemplate;


@RunWith(MockitoJUnitRunner.class)
public class FixtureDataServiceTest {

	@InjectMocks
	private FixtureDataService fixtureDataService = new FixtureDataService() ;

	@Mock
	CareDataRepository careDataRepository;
	
	@Mock
	PropertyReader propertyReader;
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	StubDataService stubDataService;

	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldsyncupdateGroupId() throws Exception {
		DataList dataList = new DataList();
		List<Data> dummyData = new ArrayList<Data>();
		Fields fields =  new Fields("2345","69735");
		Data data = new Data(fields,"ASHA");
		dummyData.add(data);
		dataList.setDataList(dummyData);
		MctsPhc mctsPhc = new MctsPhc();
		mctsPhc.setId(10);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(175);
		MctsHealthworker mctsHealthworker = new MctsHealthworker();
		mctsHealthworker.setHealthworkerId(69735);
		mctsHealthworker.setMctsPhc(mctsPhc);
		mctsHealthworker.setType("ASHA");
		mctsHealthworker.setName("abc");
		mctsHealthworker.setSex('F');
		when(stubDataService.getFixtureData()).thenReturn(dataList);
		when(careDataRepository.getHealthWorkerfromId("69735")).thenReturn(mctsHealthworker);
		
		fixtureDataService.updateGroupId();
		
		verify(careDataRepository).saveOrUpdate((MctsHealthworker)any());
		//verify(careDataRepository, times(1)).saveOrUpdate((MctsHealthworker)any());
		
		
		
	}
}
