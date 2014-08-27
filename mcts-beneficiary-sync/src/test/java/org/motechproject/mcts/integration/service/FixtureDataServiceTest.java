package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.motechproject.mcts.utils.XmlStringToObjectConverter;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class FixtureDataServiceTest {

	@InjectMocks
	private FixtureDataService fixtureDataService = new FixtureDataService();

	@Mock
	CareDataRepository careDataRepository;

	@Mock
	PropertyReader propertyReader;

	@Mock
	RestTemplate restTemplate;

	@Mock
	StubDataService stubDataService;
	Data res;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		String resp = "{\"meta\": {\"limit\": 1000, \"next\": null, \"offset\": 0, \"previous\": null, \"total_count\": 2}, \"objects\": [{\"fields\": {\"group_id\": {\"field_list\": [{\"field_value\": \"636162a69e05723c145247411a309507\", \"properties\": {}}]}, \"id\": {\"field_list\": [{\"field_value\": \"2345\", \"properties\": {}}]}, \"name\": {\"field_list\": [{\"field_value\": \"1. mcts.test ASHA\", \"properties\": {\"lang\": \"en\"}}, {\"field_value\": \"1. परीक्षण आशा MCTS\", \"properties\": {\"lang\": \"hin\"}}]}}, \"fixture_type\": \"asha\", \"id\": \"dad9669188de51982fd6287a65f4c310\", \"resource_uri\": \"\"}, {\"fields\": {\"group_id\": {\"field_list\": [{\"field_value\": \"ea313b8eed45c14d4579e6c3cffd2ebd\", \"properties\": {}}]}, \"id\": {\"field_list\": [{\"field_value\": \"1234\", \"properties\": {}}]}, \"name\": {\"field_list\": [{\"field_value\": \"2. mcts.rishad ASHA\", \"properties\": {\"lang\": \"en\"}}, {\"field_value\": \"2. MCTS Rishad आशा\", \"properties\": {\"lang\": \"hin\"}}]}}, \"fixture_type\": \"asha\", \"id\": \"dad9669188de51982fd6287a65f4b09b\", \"resource_uri\": \"\"}]}";
		res = (Data) XmlStringToObjectConverter.unmarshal(resp, Data.class);
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

		when(stubDataService.getFixtureData()).thenReturn(res);
		when(careDataRepository.getHealthWorkerfromId(anyString())).thenReturn(mctsHealthworker);
	}

	@Test
	public void testJSONTOJava() {
		try {

			System.out.println(res.getObjects().size());
			System.out.println(res.getObjects().get(0).getFixtureType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldUpdateGroupId() throws Exception {
		fixtureDataService.updateGroupId();
		verify(careDataRepository, times(2)).saveOrUpdate((MctsHealthworker)any());
	}
	
	@Ignore
	@Test
	//TODO commenting it out as care-reporting is not being deployed where location-id is stored
	public void shouldGetCaseGroupIdfromAshaId() {
	    when(careDataRepository.getCaseGroupIdfromAshaId(15)).thenReturn(null).thenReturn(null);
	    when(careDataRepository.getOwnerIdFromLocationId("1234567890")).thenReturn("123");
	    String groupId = fixtureDataService.getCaseGroupIdfromAshaId(15,"1234567890");
	    
	    assertEquals("123",groupId);
	    
	}
}
