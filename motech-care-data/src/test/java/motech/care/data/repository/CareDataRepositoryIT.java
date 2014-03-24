package motech.care.data.repository;

import motech.care.data.domain.Beneficiary;
import motech.care.data.domain.MCTSPregnantMother;
import motech.care.data.domain.MCTSPregnantMotherServiceUpdate;
import motech.care.data.domain.MotherCase;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CareDataRepositoryIT extends BaseRepositoryIT {
    @Autowired
    private CareDataRepository careDataRepository;

    @Before
    @After
    public void setUp() {
        getCurrentSession().createSQLQuery("DELETE FROM report.mcts_pregnant_mother_service_update").executeUpdate();
        getCurrentSession().createSQLQuery("DELETE FROM report.mcts_pregnant_mother").executeUpdate();
        getCurrentSession().createSQLQuery("DELETE FROM report.mother_case").executeUpdate();
    }

    @Test
    public void shouldGetBeneficiariesToSyncForAllServiceTypes() {
        Date now = DateTime.now().toDateMidnight().toDate();
        MotherCase motherCase1 = setUpMotherCaseWithAncDates(now, now);
        MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, now);
        MotherCase motherCase3 = setUpMotherCaseWithIFADateAndAdd(now, now);
        MCTSPregnantMother mctsPregnantMother1 = new MCTSPregnantMother("mctsId1", motherCase1);
        getCurrentSession().save(mctsPregnantMother1);
        MCTSPregnantMother mctsPregnantMother2 = new MCTSPregnantMother("mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother2);
        MCTSPregnantMother mctsPregnantMother3 = new MCTSPregnantMother("mctsId3", motherCase3);
        getCurrentSession().save(mctsPregnantMother3);

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(), "mctsId1", 2, now, "9999911110", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(), "mctsId1", 3, now, "9999911111", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(), "mctsId1", 4, now, "9999911171",1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 5, now, "9999911181", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 6, now, "9999911161", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 7, now, "9999911151", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother3.getId(), "mctsId3", 8, now, "9999911131", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother3.getId(), "mctsId3", 9, now, "9999911121",1, null, null,null));

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime.now().plusDays(1));
        //TODO the test cases should be improved to insert hemoglobin levels and also mobile numbers
        assertEquals(beneficiariesToSync.size(), expectedBeneficiaries.size());
        //assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries, beneficiariesToSync));
    }
    
    @Test
    public void shouldGetBeneficiariesToSyncWithinGiveDateRange() {
        Date now = DateTime.now().toDateMidnight().toDate();
        DateTime startDate = DateTime.now().minusDays(1);
        DateTime endDate = DateTime.now();
        Date dateOutOfRange = DateTime.now().minusDays(3).toDate();
        Date dateInRange = DateTime.now().minusHours(3).toDate();
        MotherCase motherCase1 = setUpMotherCaseWithAncDates(now, dateOutOfRange);
        MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, dateInRange);
        MCTSPregnantMother mctsPregnantMother1 = new MCTSPregnantMother("mctsId1", motherCase1);
        getCurrentSession().save(mctsPregnantMother1);
        MCTSPregnantMother mctsPregnantMother2 = new MCTSPregnantMother("mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother2);

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 5, now, "9999911111", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 6, now, "9999911911", 1, null, null,8));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 7, now, "9999911191",1, null, null,6));

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(startDate, endDate);
        assertEquals(beneficiariesToSync.size(), expectedBeneficiaries.size());
        //assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries, beneficiariesToSync));
    }

    @Test
    public void shouldNotGetBenefeciariesWhichAreAlreadyUpdatedToMCTS() {
        Date now = DateTime.now().toDateMidnight().toDate();
        MotherCase motherCase1 = setUpMotherCaseWithAncDates(now, now);
        MotherCase motherCase2 = setUpMotherCaseWithTTDates(now, now);
        MCTSPregnantMother mctsPregnantMother1 = new MCTSPregnantMother("mctsId1", motherCase1);
        MCTSPregnantMother mctsPregnantMother2 = new MCTSPregnantMother("mctsId2", motherCase2);
        getCurrentSession().save(mctsPregnantMother1);
        getCurrentSession().save(mctsPregnantMother2);
        getCurrentSession().save(new MCTSPregnantMotherServiceUpdate(mctsPregnantMother1, 2, now, null));
        getCurrentSession().save(new MCTSPregnantMotherServiceUpdate(mctsPregnantMother2, 5, now, null));

        List<Beneficiary> expectedBeneficiaries = new ArrayList<>();
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(), "mctsId1", 3, now, "9999911111",1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother1.getId(), "mctsId1", 4, now, "9999911191", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 6, now, "9999911171", 1, null, null,null));
        expectedBeneficiaries.add(new Beneficiary(mctsPregnantMother2.getId(), "mctsId2", 7, now, "9999911161",1, null, null,null));

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(DateTime.now().minusDays(1), DateTime.now().plusDays(1));
        assertEquals(beneficiariesToSync.size(), expectedBeneficiaries.size());
        //assertTrue(CollectionUtils.isEqualCollection(expectedBeneficiaries, beneficiariesToSync));
    }

    @Test
    public void shouldSaveNewEntity() {
        String mctsId = "mctsId";
        MotherCase motherCase = new MotherCase();
        MCTSPregnantMother mctsPregnantMother = new MCTSPregnantMother(mctsId, motherCase);
        getCurrentSession().save(motherCase);

        careDataRepository.saveOrUpdate(mctsPregnantMother);

        List dbRecords = getCurrentSession().createCriteria(MCTSPregnantMother.class).list();
        assertEquals(1, dbRecords.size());
        MCTSPregnantMother mctsPregnantMotherFromDb = (MCTSPregnantMother) dbRecords.get(0);
        assertEquals(mctsPregnantMother, mctsPregnantMotherFromDb);
    }

    @Test
    public void shouldUpdateGivenEntity() {
        String newMctsId = "newMctsId";
        MotherCase motherCase = new MotherCase();
        MCTSPregnantMother mctsPregnantMother = new MCTSPregnantMother("mctsId", motherCase);
        getCurrentSession().save(motherCase);
        getCurrentSession().save(mctsPregnantMother);

        mctsPregnantMother.updateMctsId(newMctsId);
        careDataRepository.saveOrUpdate(mctsPregnantMother);

        List dbRecords = getCurrentSession().createCriteria(MCTSPregnantMother.class).list();
        assertEquals(1, dbRecords.size());
        MCTSPregnantMother mctsPregnantMotherFromDb = (MCTSPregnantMother) dbRecords.get(0);
        assertEquals(newMctsId, mctsPregnantMotherFromDb.getMctsId());
        assertEquals(motherCase, mctsPregnantMotherFromDb.getMotherCase());
    }

    @Test
    public void shouldFindEntityByGivenFieldValue() {
        MotherCase motherCase = new MotherCase();
        MCTSPregnantMother mctsPregnantMother = new MCTSPregnantMother("mctsId", motherCase);
        getCurrentSession().save(motherCase);
        getCurrentSession().save(mctsPregnantMother);

        MCTSPregnantMother actualMCTSPregnantMother = careDataRepository.findEntityByField(MCTSPregnantMother.class, "motherCase", motherCase);

        assertEquals(mctsPregnantMother, actualMCTSPregnantMother);
    }

    private MotherCase setUpMotherCaseWithAncDates(Date serviceDate, Date lastModifiedTime) {
        MotherCase motherCase1 = new MotherCase();
        motherCase1.setCaseId("caseId1");
        motherCase1.setAnc2Date(serviceDate);
        motherCase1.setAnc3Date(serviceDate);
        motherCase1.setAnc4Date(serviceDate);
        motherCase1.setLastModifiedTime(lastModifiedTime);
        getCurrentSession().save(motherCase1);
        return motherCase1;
    }

    private MotherCase setUpMotherCaseWithTTDates(Date serviceDate, Date lastModifiedTime) {
        MotherCase motherCase2 = new MotherCase();
        motherCase2.setCaseId("caseId2");
        motherCase2.setTt1Date(serviceDate);
        motherCase2.setTt2Date(serviceDate);
        motherCase2.setTtBoosterDate(serviceDate);
        motherCase2.setLastModifiedTime(lastModifiedTime);
        getCurrentSession().save(motherCase2);
        return motherCase2;
    }

    private MotherCase setUpMotherCaseWithIFADateAndAdd(Date serviceDate, Date lastModifiedTime) {
        MotherCase motherCase3 = new MotherCase();
        motherCase3.setCaseId("caseId3");
        motherCase3.setIfaTablets100(serviceDate);
        motherCase3.setAdd(serviceDate);
        motherCase3.setLastModifiedTime(lastModifiedTime);
        getCurrentSession().save(motherCase3);
        return motherCase3;
    }
}
