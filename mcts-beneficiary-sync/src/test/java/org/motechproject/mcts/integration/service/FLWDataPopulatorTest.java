package org.motechproject.mcts.integration.service;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworkerErrorLog;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FLWDataPopulator;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class FLWDataPopulatorTest {
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
		mctsPhc = new MctsPhc();
		mctsPhc.setId(10);
		mctsPhc.setName("SaurBazar");
		mctsPhc.setPhcId(175);
		when(careDataRepository.getMctsPhc(175)).thenReturn(mctsPhc);
		when(
				careDataRepository.findEntityByField(MctsHealthworker.class,
						"healthworkerId", 69735)).thenReturn(null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToDatabase() throws Exception {

		File file = new File("src/test/resources/FLW2.csv");
		DiskFileItem fileItem = new DiskFileItem("file",
				"application/vnd.ms-excel", false, file.getName(),
				(int) file.length(), file.getParentFile());
		fileItem.getOutputStream();
		MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
		fLWDataPopulator.populateFLWData(multipartFile, "10");
		ArgumentCaptor<MctsHealthworker> captor = ArgumentCaptor
				.forClass(MctsHealthworker.class);
		ArgumentCaptor<MctsHealthworkerErrorLog> captor2 = ArgumentCaptor
				.forClass(MctsHealthworkerErrorLog.class);
		verify(careDataRepository).saveOrUpdate(captor2.capture());
		MctsHealthworkerErrorLog mctsFlwData = captor2.getValue();
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsHealthworker mctsHealthworker = captor.getValue();
		assertEquals("SaurBazar", mctsHealthworker.getMctsPhc().getName());
		assertEquals("ASHA", mctsHealthworker.getType());
		assertEquals(175, mctsHealthworker.getMctsPhc().getPhcId());
		assertEquals((Integer) 10, mctsHealthworker.getMctsPhc().getId());
		verify(careDataRepository).saveOrUpdate((MctsHealthworker) any());
		verify(careDataRepository, times(1)).saveOrUpdate(
				(MctsHealthworker) any());
		verify(careDataRepository, times(0)).saveOrUpdate(
				(MctsHealthworkerErrorLog) any());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSyncCsvDataToflw() throws Exception {

		File file = new File(propertyReader.getFLWCsvFileLocation());// TODOAman
																		// chang
																		// to
																		// test
																		// res
		// fLWDataPopulator.flwDataPopulator(file);
		ArgumentCaptor<MctsHealthworkerErrorLog> captor = ArgumentCaptor
				.forClass(MctsHealthworkerErrorLog.class);
		verify(careDataRepository).saveOrUpdate(captor.capture());
		MctsHealthworkerErrorLog mctsFlwData = captor.getValue();
		assertEquals("Anita Kumari", mctsFlwData.getName());
		assertEquals("ASHA", mctsFlwData.getType());
		verify(careDataRepository).saveOrUpdate((MctsHealthworker) any());
		verify(careDataRepository, times(1)).saveOrUpdate(
				(MctsHealthworker) any());

	}
}
