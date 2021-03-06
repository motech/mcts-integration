package org.motechproject.mcts.integration.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherServiceUpdate;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
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

        MctsPregnantMother mctsPregnantMother1 = createMctsPregnantMother(
                "mctsId1", motherCase1);
        getCurrentSession().save(mctsPregnantMother1);
        MctsPregnantMother mctsPregnantMother2 = createMctsPregnantMother(
                "mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother2);
        MctsPregnantMother mctsPregnantMother3 = createMctsPregnantMother(
                "mctsId3", motherCase3);
        getCurrentSession().save(mctsPregnantMother3);

        getCurrentSession().flush();

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(),
                "mctsId1", 2, now, "9999911110", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(),
                "mctsId1", 3, now, "9999911111", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(),
                "mctsId1", 4, now, "9999911171", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 5, now, "9999911181", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 6, now, "9999911161", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 7, now, "9999911151", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother3.getId(),
                "mctsId3", 8, now, "9999911131", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother3.getId(),
                "mctsId3", 9, now, "9999911121", 1, null, null, null));

        List<Beneficiary> beneficiariesToSync = careDataRepository
                .getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
                        .now().plusDays(1));
        // TODO the test cases should be improved to insert hemoglobin levels
        // and also mobile numbers
        assertEquals(
                (beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
                        .size()), expectedBeneficiaries.size());
        // assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
        // beneficiariesToSync));
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

        MctsPregnantMother mctsPregnantMother1 = createMctsPregnantMother(
                "mctsId1", motherCase1);
        getCurrentSession().save(mctsPregnantMother1);
        MctsPregnantMother mctsPregnantMother2 = createMctsPregnantMother(
                "mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother2);

        getCurrentSession().flush();

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 5, now, "9999911111", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 6, now, "9999911911", 1, null, null, 8));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 7, now, "9999911191", 1, null, null, 6));

        List<Beneficiary> beneficiariesToSync = careDataRepository
                .getBeneficiariesToSync(startDate, endDate);
        assertEquals(
                (beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
                        .size()), expectedBeneficiaries.size());
        // assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
        // beneficiariesToSync));
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

        MctsPregnantMother mctsPregnantMother1 = createMctsPregnantMother(
                "mctsId1", motherCase1);
        MctsPregnantMother mctsPregnantMother2 = createMctsPregnantMother(
                "mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother1);
        getCurrentSession().save(mctsPregnantMother2);
        MctsPregnantMotherServiceUpdate serviceUpdate1 = new MctsPregnantMotherServiceUpdate();
        serviceUpdate1.setMctsPregnantMother(mctsPregnantMother1);
        serviceUpdate1.setServiceType(Short.valueOf("2"));
        serviceUpdate1.setServiceDeliveryDate(now);
        serviceUpdate1.setServiceUpdateTime(null);
        getCurrentSession().save(serviceUpdate1);

        MctsPregnantMotherServiceUpdate serviceUpdate2 = new MctsPregnantMotherServiceUpdate();
        serviceUpdate2.setMctsPregnantMother(mctsPregnantMother2);
        serviceUpdate2.setServiceType(Short.valueOf("5"));
        serviceUpdate2.setServiceDeliveryDate(now);
        serviceUpdate2.setServiceUpdateTime(null);
        getCurrentSession().save(serviceUpdate2);

        getCurrentSession().flush();

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(),
                "mctsId1", 3, now, "9999911111", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(),
                "mctsId1", 4, now, "9999911191", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 6, now, "9999911171", 1, null, null, null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(),
                "mctsId2", 7, now, "9999911161", 1, null, null, null));

        List<Beneficiary> beneficiariesToSync = careDataRepository
                .getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime
                        .now().plusDays(1));
        assertEquals(
                (beneficiariesToSync.size() - beneficiariesToSyncPresentAlreadyInDb
                        .size()), expectedBeneficiaries.size());
        // assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries,
        // beneficiariesToSync));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void shouldSaveNewEntity() throws BeneficiaryException {
        shouldClearMctsPregnantMotherTables();
        String mctsId = "mctsId";
        MotherCase motherCase = new MotherCase();

        MctsPregnantMother mctsPregnantMother = createMctsPregnantMother(
                mctsId, motherCase);
        getCurrentSession().save(motherCase);

        careDataRepository.saveOrUpdate(mctsPregnantMother);

        List dbRecords = getCurrentSession().createCriteria(
                MctsPregnantMother.class).list();
        assertEquals(1, dbRecords.size());
        MctsPregnantMother mctsPregnantMotherFromDb = (MctsPregnantMother) dbRecords
                .get(0);
        assertEquals(mctsPregnantMother, mctsPregnantMotherFromDb);
        shouldClearMctsPregnantMotherTables();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void shouldUpdateGivenEntity() {
        shouldClearMctsPregnantMotherTables();
        String newMctsId = "newMctsId";
        MotherCase motherCase = new MotherCase();

        MctsPregnantMother mctsPregnantMother = createMctsPregnantMother(
                "mctsId", motherCase);
        getCurrentSession().save(motherCase);
        getCurrentSession().save(mctsPregnantMother);

        mctsPregnantMother.setMctsId(newMctsId);
        careDataRepository.saveOrUpdate(mctsPregnantMother);

        List dbRecords = getCurrentSession().createCriteria(
                MctsPregnantMother.class).list();
        assertEquals(1, dbRecords.size());
        MctsPregnantMother mctsPregnantMotherFromDb = (MctsPregnantMother) dbRecords
                .get(0);
        assertEquals(newMctsId, mctsPregnantMotherFromDb.getMctsId());
        assertEquals(motherCase, mctsPregnantMotherFromDb.getMotherCase());
        shouldClearMctsPregnantMotherTables();
    }

    private MctsPregnantMother createMctsPregnantMother(String mctsId,
            MotherCase motherCase) {
        MctsPregnantMother mctsPregMother = new MctsPregnantMother();
        mctsPregMother.setMotherCase(motherCase);
        mctsPregMother.setMctsId(mctsId);
        return mctsPregMother;
    }

    @Test
    public void shouldFindEntityByGivenFieldValue() {
        MotherCase motherCase = new MotherCase();

        MctsPregnantMother mctsPregnantMother = createMctsPregnantMother(
                "mctsId", motherCase);
        getCurrentSession().save(motherCase);
        getCurrentSession().save(mctsPregnantMother);
        MctsPregnantMother actualMctsPregnantMother = careDataRepository
                .findEntityByField(MctsPregnantMother.class, "motherCase",
                        motherCase);
        assertEquals(mctsPregnantMother, actualMctsPregnantMother);
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
