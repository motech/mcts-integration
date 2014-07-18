package org.motechproject.mcts.integration.commcare;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class UpdateXmlServiceTest {

	@InjectMocks
	private UpdateCaseXmlService updateCaseXmlService = new UpdateCaseXmlService();

	@Mock
	PropertyReader propertyReader;

	@Mock
	MCTSHttpClientService mCTSHttpClientService;
	MctsPregnantMother mother1;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mother1 = new MctsPregnantMother();
		mother1.setName("soniya devi");
		mother1.setFatherHusbandName("Dharmandra Sada");
		mother1.setHindiName("soniya devi");
		mother1.setHindiFatherHusbandName("Dharmandra Sada");
		mother1.setId(50);

		HttpStatus status = HttpStatus.ACCEPTED;

		when(propertyReader.getUserIdforCommcare()).thenReturn("12345");
		when(mCTSHttpClientService.syncToCommcareUpdate((UpdateData) any()))
				.thenReturn(status);

	}
	@Test
	public void shouldUpdateXml() throws BeneficiaryException {
		updateCaseXmlService.updateXml(mother1);
		verify(mCTSHttpClientService, times(1)).syncToCommcareUpdate((UpdateData)any());
	}

}
