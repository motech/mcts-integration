package motech.care.data.repository;

import motech.care.data.domain.Beneficiary;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CareDataRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataRepository.class);

    private SessionFactory sessionFactory;

    public CareDataRepository() {
    }

    @Autowired
    public CareDataRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        String queryString = "WITH \n" +
                "form_result AS\n" +
                "  ( SELECT instance_id,\n" +
                "           GREATEST(anc2_date, anc3_date, anc4_date, tt1_date, tt2_date, tt_booster_date) AS latest_date\n" +
                "   FROM report.bp_form\n" +
                "   WHERE creation_time BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "' \n" +
                "   UNION \n" +
                "   SELECT instance_id,\n" +
                "                date_last_visit AS latest_date\n" +
                "   FROM report.delivery_mother_form\n" +
                "   WHERE creation_time BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "' \n" +
                " ),\n" +
                "final_cases AS\n" +
                "  ( SELECT bpf.case_id AS case_id,\n" +
                "          CASE\n" +
                "              WHEN form_result.latest_date = anc2_date THEN 2\n" +
                "              WHEN form_result.latest_date = anc3_date THEN 3\n" +
                "              WHEN form_result.latest_date = anc4_date THEN 4\n" +
                "              WHEN form_result.latest_date = tt1_date THEN 5\n" +
                "              WHEN form_result.latest_date = tt2_date THEN 6\n" +
                "              WHEN form_result.latest_date = tt_booster_date THEN 7\n" +
                "          END AS service_type\n" +
                "   FROM form_result\n" +
                "   JOIN report.bp_form bpf ON form_result.instance_id = bpf.instance_id\n" +
                "   UNION ALL \n" +
                "   SELECT dmf.case_id AS case_id,\n" +
                "            CASE\n" +
                "                  WHEN form_result.latest_date = dmf.date_last_visit THEN 9\n" +
                "            END AS service_type\n" +
                "   FROM form_result\n" +
                "   JOIN report.delivery_mother_form dmf ON form_result.instance_id = dmf.instance_id \n" +
                "  )\n" +
                "  \n" +
                "SELECT mc.mcts_id,\n" +
                "       final_cases.service_type AS \"Service Type\"\n" +
                "FROM final_cases\n" +
                "JOIN report.mother_case mc ON final_cases.case_id = mc.id\n" +
                "AND final_cases.service_type IS NOT NULL";

        return mapToBeneficiaryList(getCurrentSession().createSQLQuery(queryString).list());
    }

    private List<Beneficiary> mapToBeneficiaryList(List<Object[]> result) {
        List<Beneficiary> beneficiaries = new ArrayList<>();
        for (Object[] record : result) {
            beneficiaries.add(new Beneficiary((String) record[0], (Integer) record[1]));
        }
        return beneficiaries;
    }

    public void updateCase(String caseId, String mctsId) {
        String motherCaseUpdateQuery = "UPDATE report.mother_case SET mcts_id = '" + mctsId + "' WHERE case_id = '" + caseId + "'";
        int updatedRowCount = getCurrentSession().createSQLQuery(motherCaseUpdateQuery).executeUpdate();
        if (updatedRowCount > 0) {
            LOGGER.info(String.format("Mother case with Case Id: %s is updated with MCTS Id: %s", caseId, mctsId));
            return;
        }

        String childCaseUpdateQuery = "UPDATE report.child_case SET mcts_id = '" + mctsId + "' WHERE case_id = '" + caseId + "'";
        updatedRowCount = getCurrentSession().createSQLQuery(childCaseUpdateQuery).executeUpdate();
        if(updatedRowCount > 0) {
            LOGGER.info(String.format("Child case with Case Id: %s is updated with MCTS Id: %s", caseId, mctsId));
            return;
        }

        LOGGER.info(String.format("Case not updated. Mother case and Child case not found for Case Id: %s", caseId));
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
