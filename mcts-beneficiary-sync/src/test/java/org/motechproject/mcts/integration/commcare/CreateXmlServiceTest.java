package org.motechproject.mcts.integration.commcare;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class CreateXmlServiceTest {

	@InjectMocks
	private CreateCaseXmlService createCaseXmlService = new CreateCaseXmlService();

	@Mock
	CareDataRepository careDataRepository;

	@Mock
	PropertyReader propertyReader;

	@Mock
	MCTSHttpClientService mCTSHttpClientService;
	
	@Mock
	FixtureDataService fixtureDataService;

	List<MctsPregnantMother> motherList;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		motherList = new ArrayList<MctsPregnantMother>();

		MctsPregnantMother mother1 = new MctsPregnantMother();
		mother1.setName("soniya devi");
		mother1.setFatherHusbandName("Dharmandra Sada");
		mother1.setHindiName("soniya devi");
		mother1.setHindiFatherHusbandName("Dharmandra Sada");
		//mother1.setId(50);

		MctsPregnantMother mother2 = new MctsPregnantMother();
		mother2.setName("Ranju Devi");
		mother2.setFatherHusbandName("Dilkush Kamat");
		mother2.setHindiName("Ranju Devi");
		mother2.setHindiFatherHusbandName("Dilkush Kamat");
		//mother2.setId(2);

		motherList.add(mother1);
		motherList.add(mother2);

		HttpStatus status = HttpStatus.ACCEPTED;

		Mockito.when(careDataRepository.getMctsPregnantMother()).thenReturn(
				motherList);
		Mockito.when(propertyReader.sizeOfXml()).thenReturn(50);
		Mockito.when(mCTSHttpClientService.syncToCommcare((Data) any()))
				.thenReturn(status);
		Mockito.when(propertyReader.getUserIdforCommcare()).thenReturn("1234");
		Mockito.when(fixtureDataService.getCaseGroupIdfromAshaId(anyInt(),anyString())).thenReturn("6efbnkfb");
		Mockito.when(careDataRepository
					.getMotherFromPrimaryId(anyInt())).thenReturn(mother1).thenReturn(mother2);
		
	}

	@Test
	public void shouldCreateCaseXml() throws BeneficiaryException {
		createCaseXmlService.createCaseXml();
		verify(careDataRepository, times(2)).saveOrUpdate((MctsPregnantMother)any());
	}

	@Test
	public void shouldCreateXml() throws BeneficiaryException {
		createCaseXmlService.createXml(motherList);
	}
}
