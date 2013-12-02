package motech.care.data.repository;

import motech.care.data.domain.Beneficiary;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CareDataRepositoryIT extends BaseRepositoryIT {
    @Autowired
    private CareDataRepository careDataRepository;

    @Before
    @After
    public void setUp() {
        session.createSQLQuery("DELETE FROM report.bp_form").executeUpdate();
        session.createSQLQuery("DELETE FROM report.delivery_mother_form").executeUpdate();
        session.createSQLQuery("DELETE FROM report.child_case").executeUpdate();
        session.createSQLQuery("DELETE FROM report.mother_case").executeUpdate();
    }

    @Test
    public void shouldGetBeneficiariesToSync_AndMapToServiceType() {
        DateTime now = DateTime.now();
        DateTime startDate = now.minusDays(1);
        DateTime endDate = now;
        DateTime creationTime = endDate.minusHours(4);
        DateTime visitTime = now.minusDays(12);
        createMotherCase(1, "mctsId1", "caseId1");
        createMotherCase(2, "mctsId2", "caseId2");

        createBpForm(1, "bpForm1", 1, creationTime, "anc2_date", visitTime);
        createBpForm(2, "bpForm2", 1, creationTime, "anc3_date", visitTime);
        createBpForm(4, "bpForm4", 2, creationTime, "anc4_date", visitTime);

        createDeliveryMotherForm(1, "deliveryMotherForm1", 1, creationTime, visitTime);
        createDeliveryMotherForm(2, "deliveryMotherForm2", 2, creationTime, visitTime);

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(startDate, endDate);

        assertEquals(5, beneficiariesToSync.size());
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId1", 2)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId1", 3)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId2", 4)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId1", 9)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId2", 9)));
    }

    @Test
    public void shouldConsiderGreatestOfAllVisitDatesIfAFormHasMoreThanOneVisit_AndMapToServiceType() {
        DateTime now = DateTime.now();
        DateTime startDate = now.minusDays(1);
        DateTime endDate = now;
        DateTime creationTime = endDate.minusHours(4);
        DateTime visitTime1 = now.minusDays(10);
        DateTime visitTime2 = now.minusDays(12);
        createMotherCase(1, "mctsId1", "caseId1");
        createMotherCase(2, "mctsId2", "caseId2");

        createBpForm(3, "bpForm3", 1, creationTime, "tt1_date", visitTime1, "tt2_date", visitTime2);
        createBpForm(6, "bpForm6", 2, creationTime, "tt2_date", visitTime1, "tt_booster_date", visitTime2);

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(startDate, endDate);

        assertEquals(2, beneficiariesToSync.size());
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId1", 5)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId2", 6)));
    }

    @Test
    public void shouldGetBeneficiariesToSyncWithinDateRange_AndMapToServiceType() {
        DateTime now = DateTime.now();
        DateTime startDate = now.minusDays(1);
        DateTime endDate = now;
        DateTime creationTimeWithinRange = endDate.minusHours(4);
        DateTime creationTimeOutOfRange = endDate.plusHours(4);
        DateTime visitTime = now.minusDays(12);
        createMotherCase(1, "mctsId1", "caseId1");
        createMotherCase(2, "mctsId2", "caseId2");

        createBpForm(5, "bpForm5", 1, creationTimeWithinRange, "tt_booster_date", visitTime);
        createBpForm(7, "bpForm7", 2, creationTimeOutOfRange, "tt1_date", visitTime);

        createDeliveryMotherForm(3, "deliveryMotherForm3", 2, creationTimeWithinRange, visitTime);
        createDeliveryMotherForm(4, "deliveryMotherForm4", 1, creationTimeOutOfRange, visitTime);

        List<Beneficiary> beneficiariesToSync = careDataRepository.getBeneficiariesToSync(startDate, endDate);

        assertEquals(2, beneficiariesToSync.size());
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId1", 7)));
        assertTrue(beneficiariesToSync.contains(new Beneficiary("mctsId2", 9)));
    }

    @Test
    public void shouldUpdateMotherCaseWithGivenMCTSId() {
        createMotherCase(1, null, "motherCaseId");
        createChildCase(1, "oldMctsId", "childCaseId");

        careDataRepository.updateCase("motherCaseId", "mctsId");

        List motherCasesFromDb = session.createSQLQuery("SELECT case_id, mcts_id FROM report.mother_case").list();
        assertEquals(1, motherCasesFromDb.size());
        Object[] motherCase = (Object[]) motherCasesFromDb.get(0);
        assertEquals("motherCaseId", motherCase[0]);
        assertEquals("mctsId", motherCase[1]);

        List childCasesFromDb = session.createSQLQuery("SELECT case_id, mcts_id FROM report.child_case").list();
        assertEquals(1, childCasesFromDb.size());
        Object[] childCase = (Object[]) childCasesFromDb.get(0);
        assertEquals("childCaseId", childCase[0]);
        assertEquals("oldMctsId", childCase[1]);
    }

    @Test
    public void shouldUpdateChildCaseWithGivenMCTSIdIfMotherCaseDoesNotExistWithGivenCaseId() {
        createMotherCase(1, "oldMctsId", "motherCaseId");
        createChildCase(2, null, "childCaseId");

        careDataRepository.updateCase("childCaseId", "mctsId");

        List motherCasesFromDb = session.createSQLQuery("SELECT case_id, mcts_id FROM report.mother_case").list();
        assertEquals(1, motherCasesFromDb.size());
        Object[] motherCase = (Object[]) motherCasesFromDb.get(0);
        assertEquals("motherCaseId", motherCase[0]);
        assertEquals("oldMctsId", motherCase[1]);

        List childCasesFromDb = session.createSQLQuery("SELECT case_id, mcts_id FROM report.child_case").list();
        assertEquals(1, childCasesFromDb.size());
        Object[] childCase = (Object[]) childCasesFromDb.get(0);
        assertEquals("childCaseId", childCase[0]);
        assertEquals("mctsId", childCase[1]);
    }

    private void createMotherCase(int id, String mctsId, String caseId) {
        String queryString = String.format("INSERT INTO report.mother_case (id, mcts_id, case_id) VALUES (%d, '%s', '%s')", id, mctsId, caseId);

        session.createSQLQuery(queryString).executeUpdate();
    }

    private void createChildCase(int id, String mctsId, String caseId) {
        String queryString = String.format("INSERT INTO report.child_case (id, mcts_id, case_id) VALUES (%d, '%s', '%s')", id, mctsId, caseId);

        session.createSQLQuery(queryString).executeUpdate();
    }

    private void createBpForm(int id, String instanceId, int motherCaseId, DateTime creationTime, String dateFieldName, DateTime dateFieldValue) {
        String queryString = String.format("INSERT INTO report.bp_form (id, instance_id, case_id, creation_time, %s) VALUES (%d, '%s', '%s', '%s', '%s')",
                dateFieldName, id, instanceId, motherCaseId, creationTime, dateFieldValue);

        session.createSQLQuery(queryString).executeUpdate();
    }

    private void createBpForm(int id, String instanceId, int motherCaseId, DateTime creationTime, String dateFieldName1, DateTime dateFieldValue1, String dateFieldName2, DateTime dateFieldValue2) {
        String queryString = String.format("INSERT INTO report.bp_form (id, instance_id, case_id, creation_time, %s, %s) VALUES (%d, '%s', '%s', '%s', '%s', '%s')",
                dateFieldName1, dateFieldName2, id, instanceId, motherCaseId, creationTime, dateFieldValue1, dateFieldValue2);

        session.createSQLQuery(queryString).executeUpdate();
    }

    private void createDeliveryMotherForm(int id, String instanceId, int motherCaseId, DateTime creationTime, DateTime visitTime) {
        String queryString = String.format("INSERT INTO report.delivery_mother_form (id, instance_id, case_id, creation_time, date_last_visit) VALUES (%d, '%s', %d, '%s', '%s')",
                id, instanceId, motherCaseId, creationTime, visitTime);

        session.createSQLQuery(queryString).executeUpdate();
    }

}
