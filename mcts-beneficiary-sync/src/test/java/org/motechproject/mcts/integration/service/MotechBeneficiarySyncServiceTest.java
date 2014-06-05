package org.motechproject.mcts.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;

@PrepareForTest(FileUtils.class)
@RunWith(PowerMockRunner.class)
public class MotechBeneficiarySyncServiceTest {
	@Mock
	private CareDataService careDataService;
	@Mock
	private MCTSHttpClientService mctsHttpClientService;
	@Mock
	private PropertyReader propertyReader;
	@Mock
	private Publisher publisher;
	@Mock
	private ObjectToXMLConverter objectToXML;

	@InjectMocks
	private MotechBeneficiarySyncService motechBeneficiarySyncService;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
	}

	public List<Beneficiary> getListOfBeneficiariesToSync() {
		Date serviceDeliveryDate1 = new Date();
		Date serviceDeliveryDate2 = new Date();
		Date serviceDeliveryDate3 = new Date();
		Date serviceDeliveryDate4 = new Date();
		List<Beneficiary> beneficiaries = Arrays.asList(
				new Beneficiary(1, "mcts_id1", 2, serviceDeliveryDate1,
						"9999900000", 1, 5, 11, 20), new Beneficiary(2,
						"mcts_id2", 4, serviceDeliveryDate2, "9999900001", 1,
						4, 11, 14), new Beneficiary(3, "mcts_id3", 6,
						serviceDeliveryDate3, "9999900002", 1, 4, 11, 14),
				new Beneficiary(3, "mcts_id4", 3, serviceDeliveryDate4,
						"9999900003", 1, 4, 10, 14));
		return beneficiaries;
	}
	
	public BeneficiaryRequest getListOfBeneficiaryDetailsToSync(){
		Date serviceDeliveryDate1 = new Date();
		Date serviceDeliveryDate2 = new Date();
		Date serviceDeliveryDate3 = new Date();
		Date serviceDeliveryDate4 = new Date();
		BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
		beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(31,
						"mcts_id1", 2, serviceDeliveryDate1, "9999900000", "3"));
		beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(31,
						"mcts_id2", 4, serviceDeliveryDate2, "9999900001", "1"));
		beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(31,
				"mcts_id3", 6, serviceDeliveryDate3, "9999900002", ""));
		beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(31,
				"mcts_id4", 3, serviceDeliveryDate4, "9999900003", "2"));
		return beneficiaryRequest;
	}

	@Test
	public void shouldGetMotechBeneficiaryDetailsFromDB() throws Exception {
		DateTime startDate = DateTime.now().minusDays(1);
		DateTime endDate = DateTime.now();
		List<Beneficiary> beneficiaries = getListOfBeneficiariesToSync();
		when(careDataService.getBeneficiariesToSync(startDate, endDate))
				.thenReturn(beneficiaries);

		List<Beneficiary> beneficiariesFromDb = motechBeneficiarySyncService
				.getBeneficiariesToSync(startDate, endDate);

		verify(careDataService).getBeneficiariesToSync(startDate, endDate);
		assertEquals(beneficiaries.size(), beneficiariesFromDb.size());
		assertTrue(beneficiariesFromDb.contains(beneficiaries.get(0)));
		assertTrue(beneficiariesFromDb.contains(beneficiaries.get(1)));
		assertTrue(beneficiariesFromDb.contains(beneficiaries.get(2)));
		assertTrue(beneficiariesFromDb.contains(beneficiaries.get(3)));
	}

	@Test
	public void shouldMapToBeneficiaryRequest() {
		Date serviceDeliveryDate1 = new Date();
		Date serviceDeliveryDate2 = new Date();
		Date serviceDeliveryDate3 = new Date();
		Date serviceDeliveryDate4 = new Date();
		when(propertyReader.getStateId()).thenReturn(31);
		List<Beneficiary> beneficiaries = getListOfBeneficiariesToSync();
		
		BeneficiaryRequest actualRequest = motechBeneficiarySyncService.mapToBeneficiaryRequest(beneficiaries);
		List<BeneficiaryDetails> beneficiaryDetails = actualRequest
				.getAllBeneficiaryDetails();
		assertEquals(4, beneficiaryDetails.size());

		// TODO handle mobile_number and hemoglobin details in test cases
		assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31,
				"mcts_id1", 2, serviceDeliveryDate1, "9999900000", "3")));
		assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31,
				"mcts_id2", 4, serviceDeliveryDate2, "9999900001", "1")));
		assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31,
				"mcts_id3", 6, serviceDeliveryDate3, "9999900002", "")));
		assertTrue(beneficiaryDetails.contains(new BeneficiaryDetails(31,
				"mcts_id4", 3, serviceDeliveryDate4, "9999900003", "2")));
	}
	
	@Test
	public void shouldSyncToMcts(){
		BeneficiaryRequest beneficiaryRequest =  getListOfBeneficiaryDetailsToSync();
		HttpStatus httpStatus = motechBeneficiarySyncService.syncTo(beneficiaryRequest);
		when(mctsHttpClientService.syncTo(beneficiaryRequest)).thenReturn(HttpStatus.OK);
		verify(mctsHttpClientService).syncTo(beneficiaryRequest);
	}
	
	@Test
	public void shouldWriteSyncDataToFile() throws Exception{
		BeneficiaryRequest beneficiaryRequest =  getListOfBeneficiaryDetailsToSync();
		when(propertyReader.getUpdateXmlOutputFileLocation()).thenReturn("testXML");
		//when(propertyReader.getUpdateXmlOutputFileLocation()).thenReturn(MotechBeneficiarySyncServiceTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "testXML");
		when(propertyReader.getUpdateUrlOutputFileLocation()).thenReturn("testURL");
		
		motechBeneficiarySyncService.writeSyncDataToFile(beneficiaryRequest);
		verify(objectToXML).converObjectToXml((BeneficiaryRequest)anyObject(),
					(Class)any());
		verify(propertyReader).getUpdateXmlOutputFileLocation();
		verify(propertyReader).getUpdateUrlOutputFileLocation();
	}

	@Test
	public void shouldNotSyncBeneficiaryDetailsIfThereAreNoRecordsToSync()
			throws Exception {
		DateTime now = DateTime.now();
		when(careDataService.getBeneficiariesToSync(now, now)).thenReturn(
				new ArrayList<Beneficiary>());

		motechBeneficiarySyncService.syncBeneficiaryData(now, now);

		verifyZeroInteractions(mctsHttpClientService);
		verify(careDataService, never()).updateSyncedBeneficiaries(
				any(List.class));
	}
}
