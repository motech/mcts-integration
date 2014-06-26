package org.motechproject.mcts.integration.commcare.test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.commcare.Data;
import org.motechproject.mcts.integration.commcare.UpdateCaseXmlService;
import org.motechproject.mcts.integration.commcare.UpdateData;
import org.motechproject.mcts.integration.commcare.UpdateTask;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ObjectToXMLConverter.class)
public class UpdateCaseXmlServicTest {
	@InjectMocks UpdateCaseXmlService updateCaseXmlService = new UpdateCaseXmlService();
	 @Mock PropertyReader propertyReader;
	 
	 @Mock FixtureDataService fixtureDataService;
	 
	 @Before
		public void setUp() throws BeneficiaryException {
		// MockitoAnnotations.initMock(this);
		
		 when(propertyReader.getUserIdforCommcare()).thenReturn("abc");
			when(fixtureDataService.getCaseGroupIdfromAshaId(anyInt()))
					.thenReturn("6ed07f7dca6e2fb170a17446c2499ba7");
	 }
	 
	 @Test
	 public void shouldCreateUpdateTask() throws Exception {
		 MctsPregnantMother m = new MctsPregnantMother();
			m.setId(1);
			m.setName("mother");
			PowerMockito.mockStatic(ObjectToXMLConverter.class);
			Mockito.when(ObjectToXMLConverter.converObjectToXml((UpdateData)any(),eq(UpdateData.class))).thenReturn("abc");
			updateCaseXmlService.updateXml(m);
			PowerMockito.verifyStatic(Mockito.times(1));
			ObjectToXMLConverter.converObjectToXml((UpdateData)any(),eq(UpdateData.class));
			
	 }
	 
	 
	 
}
