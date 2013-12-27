package motech.care.data.repository;

import motech.care.data.domain.Beneficiary;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CareDataRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public CareDataRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        String queryString =
                "WITH \n" +
                "updatable_cases AS\n" +
                "  (SELECT id AS mother_case_id, service_type, service_delivery_date\n" +
                "   FROM\n" +
                "     (SELECT id, 2 AS service_type, anc_2_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE anc_2_date IS NOT NULL\n" +
                "      UNION \n" +
                "      SELECT id, 3 AS service_type, anc_3_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE anc_3_date IS NOT NULL\n" +
                "      UNION\n" +
                "      SELECT id, 4 AS service_type, anc_4_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE anc_4_date IS NOT NULL\n" +
                "      UNION\n" +
                "      SELECT id, 5 AS service_type, tt_1_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE tt_1_date IS NOT NULL\n" +
                "      UNION \n" +
                "      SELECT id, 6 AS service_type, tt_2_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE tt_2_date IS NOT NULL\n" +
                "      UNION \n" +
                "      SELECT id, 7 AS service_type, tt_booster_date AS service_delivery_date, last_modified_time FROM report.mother_case WHERE tt_booster_date IS NOT NULL\n" +
                "      UNION \n" +
                "      SELECT id, 8 AS service_type, ifa_tablets_100 AS service_delivery_date, last_modified_time FROM report.mother_case WHERE ifa_tablets_100 IS NOT NULL\n" +
                "      UNION \n" +
                "      SELECT id, 9 AS service_type, add AS service_delivery_date, last_modified_time FROM report.mother_case WHERE add IS NOT NULL\n" +
                "     ) all_cases\n" +
                "   WHERE last_modified_time BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "'\n" +
                "  ),\n" +
                "mcts_updated_services AS\n" +
                "  (SELECT mother.id, su.service_type \n" +
                "   FROM report.mcts_pregnant_mother mother JOIN report.mcts_pregnant_mother_service_update su ON mother.id = su.mcts_id\n" +
                "  )\n" +
                "SELECT mother.id,\n" +
                "       mother.mcts_id,\n" +
                "       updatable_cases.service_type AS \"Service Type\",\n" +
                "       updatable_cases.service_delivery_date\n" +
                "FROM updatable_cases\n" +
                "JOIN report.mcts_pregnant_mother mother ON updatable_cases.mother_case_id = mother.case_id\n" +
                "WHERE (mother.id, service_type) NOT IN (SELECT * FROM mcts_updated_services);\n";

        return mapToBeneficiaryList(getCurrentSession().createSQLQuery(queryString).list());
    }

    private List<Beneficiary> mapToBeneficiaryList(List<Object[]> result) {
        List<Beneficiary> beneficiaries = new ArrayList<>();
        for (Object[] record : result) {
            beneficiaries.add(new Beneficiary((Integer) record[0], (String) record[1], (Integer) record[2], (Date) record[3]));
        }
        return beneficiaries;
    }

    public <T> void saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public <T> T findEntityByField(Class<T> entityClass, String fieldName, Object fieldValue) {
        Criteria criteria = getCurrentSession().createCriteria(entityClass);
        criteria.add(Restrictions.eq(fieldName, fieldValue));
        return (T) criteria.uniqueResult();
    }

    public <T> T load(Class<T> entityClass, Integer id) {
        return (T) getCurrentSession().load(entityClass, id);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
