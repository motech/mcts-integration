package org.motechproject.mcts.integration.commcare.test;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.mcts.integration.commcare.CreateCaseXmlService;
import org.motechproject.mcts.integration.commcare.Data;
import org.motechproject.mcts.integration.commcare.UpdateTask;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest(ObjectToXMLConverter.class)
public class MCTSPersonaToXmlConvertorTest {

	@InjectMocks
	CreateCaseXmlService createCaseXmlService = new CreateCaseXmlService();
	
	@Mock CareDataRepository careDataRepository;

	@Mock FixtureDataService fixtureDataService ;
	
	@Mock PropertyReader propertyReader ;
	
	@Mock MCTSHttpClientService mCTSHttpClientService;
	
	
	
	List<MctsPregnantMother> mother = new ArrayList<MctsPregnantMother>();
	MctsPregnantMother m = new MctsPregnantMother();
	@Before
	public void setUp() throws Exception {
		
		
		
		
		m.setId(1);
		m.setName("mother");
		m.setHindiName("a");
		mother.add(m);
		Mockito.when(propertyReader.getUserIdforCommcare()).thenReturn("abc");
		Mockito.when(fixtureDataService.getCaseGroupIdfromAshaId(anyInt()))
				.thenReturn("6ed07f7dca6e2fb170a17446c2499ba7");
		Mockito.when(careDataRepository.getMctsPregnantMother()).thenReturn(mother);
		
	}

	@Test
	public void shouldcreateCaseXml() throws Exception  {
		
		PowerMockito.mockStatic(ObjectToXMLConverter.class);
		Mockito.when(ObjectToXMLConverter.converObjectToXml((Data)any(),eq(Data.class))).thenReturn("abc");
		createCaseXmlService.createCaseXml();
		
		PowerMockito.verifyStatic(Mockito.times(1));
		ObjectToXMLConverter.converObjectToXml((Data)any(),eq(Data.class));
		

	}
	
	@Test
	public void shouldUpdateDataandReturn() throws BeneficiaryException {
		UpdateTask task = createCaseXmlService.updateTaskandReturn(m, 50, "asdf");
		assertEquals("mother",task.getMctsFullname_en());
	}
}
