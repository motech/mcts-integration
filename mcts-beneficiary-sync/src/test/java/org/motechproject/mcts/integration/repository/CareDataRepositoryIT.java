package org.motechproject.mcts.integration.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherServiceUpdate;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;


public class CareDataRepositoryIT extends BaseRepositoryIT {
	
	@Autowired
	private CareDataRepository careDataRepository;

	@After
	@Before
	public void shouldClearMctsPregnantMotherTables() {
		getCurrentSession().createSQLQuery(
				"DELETE FROM report.mcts_pregnant_mother_service_update")
				.executeUpdate();
		getCurrentSession().createSQLQuery(
				"DELETE FROM report.mcts_pregnant_mother").executeUpdate();
	}
	
	@Test
	public void we(){
		System.out.println("runned");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldGetBeneficiariesToSyncForAllServiceTypes() {
		List<Beneficiary> beneficiariesToSyncPresentAlreadyInDb = careDataRepository
				.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
						.now().plusDays(1));
		
		Date now = DateTime.now().toDateMidnight().toDate();
		MotherCase motherCase1 = setUpMotherCaseWithAncDates(now, now);
		MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, now);
		MotherCase motherCase3 = setUpMotherCaseWithIFADateAndAdd(now, now);
		// TODO update test cases
//		MctsPregnantMother MctsPregnantMother1 = new MctsPregnantMother(
//				"mctsId1", motherCase1);
//		getCurrentSession().save(MctsPregnantMother1);
//		MctsPregnantMother MctsPregnantMother2 = new MctsPregnantMother(
//				"mctsId2", motherCase2);
//		getCurrentSession().save(MctsPregnantMother2);
//		MctsPregnantMother MctsPregnantMother3 = new MctsPregnantMother(
//				"mctsId3", motherCase3);
//		getCurrentSession().save(MctsPregnantMother3);
//		
//		List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother1.getId(),
//				"mctsId1", 2, now, "9999911110", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother1.getId(),
//				"mctsId1", 3, now, "9999911111", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother1.getId(),
//				"mctsId1", 4, now, "9999911171", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 5, now, "9999911181", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 6, now, "9999911161", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 7, now, "9999911151", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother3.getId(),
//				"mctsId3", 8, now, "9999911131", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother3.getId(),
//				"mctsId3", 9, now, "9999911121", 1, null, null, null));
//
//		List<Beneficiary> beneficiariesToSync = careDataRepository
//				.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
//						.now().plusDays(1));
//		// TODO the test cases should be improved to insert hemoglobin levels
//		// and also mobile numbers
//		assertEquals(
//				(beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
//						.size()), expectedBeneficiaries.size());
//		// assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
//		// beneficiariesToSync));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldGetBeneficiariesToSyncWithinGiveDateRange() {
		Date now = DateTime.now().toDateMidnight().toDate();
		DateTime startDate = DateTime.now().minusDays(1);
		DateTime endDate = DateTime.now();
		List<Beneficiary> beneficiariesToSyncPresentAlreadyInDb = careDataRepository
				.getBeneficiariesToSync(startDate, endDate);
		Date dateOutOfRange = DateTime.now().minusDays(3).toDate();
		Date dateInRange = DateTime.now().minusHours(3).toDate();
		MotherCase motherCase1 = setUpMotherCaseWithAncDates(now,
				dateOutOfRange);
		MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, dateInRange);
		//TODO update test cases
		
//		MctsPregnantMother MctsPregnantMother1 = new MctsPregnantMother(
//				"mctsId1", motherCase1);
//		getCurrentSession().save(MctsPregnantMother1);
//		MctsPregnantMother MctsPregnantMother2 = new MctsPregnantMother(
//				"mctsId2", motherCase2);
//		getCurrentSession().save(MctsPregnantMother2);
//
//		List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 5, now, "9999911111", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 6, now, "9999911911", 1, null, null, 8));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 7, now, "9999911191", 1, null, null, 6));
//
//		List<Beneficiary> beneficiariesToSync = careDataRepository
//				.getBeneficiariesToSync(startDate, endDate);
//		assertEquals(
//				(beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
//						.size()), expectedBeneficiaries.size());
//		// assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
//		// beneficiariesToSync));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldNotGetBenefeciariesWhichAreAlreadyUpdatedToMCTS() {
		List<Beneficiary> beneficiariesToSyncPresentAlreadyInDb = careDataRepository
				.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
						.now().plusDays(1));
		Date now = DateTime.now().toDateMidnight().toDate();
		MotherCase motherCase1 = setUpMotherCaseWithAncDates(now, now);
		MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, now);
		//TODO update test cases
//		MctsPregnantMother MctsPregnantMother1 = new MctsPregnantMother(
//				"mctsId1", motherCase1);
//		MctsPregnantMother MctsPregnantMother2 = new MctsPregnantMother(
//				"mctsId2", motherCase2);
//		getCurrentSession().save(MctsPregnantMother1);
//		getCurrentSession().save(MctsPregnantMother2);
//		getCurrentSession().save(
//				new MctsPregnantMotherServiceUpdate(MctsPregnantMother1, 2,
//						now, null));
//		getCurrentSession().save(
//				new MctsPregnantMotherServiceUpdate(MctsPregnantMother2, 5,
//						now, null));
//
//		List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother1.getId(),
//				"mctsId1", 3, now, "9999911111", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother1.getId(),
//				"mctsId1", 4, now, "9999911191", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 6, now, "9999911171", 1, null, null, null));
//		expectedBeneficiaries.add(new Beneficiary(MctsPregnantMother2.getId(),
//				"mctsId2", 7, now, "9999911161", 1, null, null, null));
//
//		List<Beneficiary> beneficiariesToSync = careDataRepository
//				.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
//						.now().plusDays(1));
//		assertEquals(
//				(beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
//						.size()), expectedBeneficiaries.size());
		// assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
		// beneficiariesToSync));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldSaveNewEntity() {
		shouldClearMctsPregnantMotherTables();
		String mctsId = "mctsId";
		MotherCase motherCase = new MotherCase();
		//TODO update test cases
//		MctsPregnantMother MctsPregnantMother = new MctsPregnantMother(mctsId,
//				motherCase);
//		getCurrentSession().save(motherCase);
//
//		careDataRepository.saveOrUpdate(MctsPregnantMother);
//
//		List dbRecords = getCurrentSession().createCriteria(
//				MctsPregnantMother.class).list();
//		assertEquals(1, dbRecords.size());
//		MctsPregnantMother MctsPregnantMotherFromDb = (MctsPregnantMother) dbRecords
//				.get(0);
//		assertEquals(MctsPregnantMother, MctsPregnantMotherFromDb);
//		shouldClearMctsPregnantMotherTables();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldUpdateGivenEntity() {
		shouldClearMctsPregnantMotherTables();
		String newMctsId = "newMctsId";
		MotherCase motherCase = new MotherCase();
		//TODO update test cases
//		MctsPregnantMother MctsPregnantMother = new MctsPregnantMother(
//				"mctsId", motherCase);
//		getCurrentSession().save(motherCase);
//		getCurrentSession().save(MctsPregnantMother);
//
//		MctsPregnantMother.updateMctsId(newMctsId);
//		careDataRepository.saveOrUpdate(MctsPregnantMother);
//
//		List dbRecords = getCurrentSession().createCriteria(
//				MctsPregnantMother.class).list();
//		assertEquals(1, dbRecords.size());
//		MctsPregnantMother MctsPregnantMotherFromDb = (MctsPregnantMother) dbRecords
//				.get(0);
//		assertEquals(newMctsId, MctsPregnantMotherFromDb.getMctsId());
//		assertEquals(motherCase, MctsPregnantMotherFromDb.getMotherCase());
//		shouldClearMctsPregnantMotherTables();
	}

	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldFindEntityByGivenFieldValue() {
		MotherCase motherCase = new MotherCase();
		//TODO update test cases
//		MctsPregnantMother MctsPregnantMother = new MctsPregnantMother(
//				"mctsId", motherCase);
//		getCurrentSession().save(motherCase);
//		getCurrentSession().save(MctsPregnantMother);
//		MctsPregnantMother actualMctsPregnantMother = careDataRepository
//				.findEntityByField(MctsPregnantMother.class, "motherCase",
//						motherCase);
//		assertEquals(MctsPregnantMother, actualMctsPregnantMother);
	}

	private MotherCase setUpMotherCaseWithAncDates(Date serviceDate,
			Date lastModifiedTime) {
		MotherCase motherCase1 = new MotherCase();
		motherCase1.setCaseId("caseId1");
		motherCase1.setAnc2Date(serviceDate);
		motherCase1.setAnc3Date(serviceDate);
		motherCase1.setAnc4Date(serviceDate);
		motherCase1.setLastModifiedTime(lastModifiedTime);
		getCurrentSession().save(motherCase1);
		return motherCase1;
	}

	private MotherCase setUpMotherCaseWithTTDates(Date serviceDate,
			Date lastModifiedTime) {
		MotherCase motherCase2 = new MotherCase();
		motherCase2.setCaseId("caseId2");
		motherCase2.setTt1Date(serviceDate);
		motherCase2.setTt2Date(serviceDate);
		motherCase2.setTtBoosterDate(serviceDate);
		motherCase2.setLastModifiedTime(lastModifiedTime);
		getCurrentSession().save(motherCase2);
		return motherCase2;
	}

	private MotherCase setUpMotherCaseWithIFADateAndAdd(Date serviceDate,
			Date lastModifiedTime) {
		MotherCase motherCase3 = new MotherCase();
		motherCase3.setCaseId("caseId3");
		motherCase3.setIfaTablets100(serviceDate);
		motherCase3.setAdd(serviceDate);
		motherCase3.setLastModifiedTime(lastModifiedTime);
		getCurrentSession().save(motherCase3);
		return motherCase3;
	}

}
