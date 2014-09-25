package org.motechproject.mcts.integration.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherCaseAuthorisedStatus;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherMatchStatus;
import org.motechproject.mcts.care.common.mds.dimension.Flw;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;
import org.motechproject.mcts.care.common.mds.repository.MdsRepository;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mds.query.EqualProperty;
import org.motechproject.mds.query.Property;
import org.motechproject.mds.query.PropertyBuilder;
import org.motechproject.mds.query.RangeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MctsRepository {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MctsRepository.class);

    @Autowired
    private MdsRepository dbRepository;

    /*
     * @Autowired private SessionFactory sessionFactory;
     *
     * public SessionFactory getSessionFactory() { return sessionFactory; }
     *
     * public void setSessionFactory(SessionFactory sessionFactory) {
     * this.sessionFactory = sessionFactory; }
     *
     * private Session getCurrentSession() { return
     * sessionFactory.getCurrentSession(); }
     *
     * public <T> T load(Class<T> entityClass, Integer id) { return (T)
     * getCurrentSession().load(entityClass, id); }
     *
     * public void flush() { getCurrentSession().flush(); }
     *
     * private static final String SEQUENCE =
     * "report.locationdata_location_id_seq";
     *
     * public MctsRepository(SessionFactory sessionFactory) {
     * this.sessionFactory = sessionFactory; }
     */

    public MdsRepository getDbRepository() {
        return dbRepository;
    }

    public void setDbRepository(MdsRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    // TODO send only for authorized = approved
    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate,
            DateTime endDate) {
        String queryString = "WITH \n"
                + "updatable_cases AS\n"
                + "  (SELECT id AS mother_case_id, service_type, service_delivery_date,mobile_number\n"
                + "   FROM\n"
                + "     (SELECT id, 2 AS service_type, anc_2_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE anc_2_date IS NOT NULL\n"
                + "      UNION \n"
                + "      SELECT id, 3 AS service_type, anc_3_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE anc_3_date IS NOT NULL\n"
                + "      UNION\n"
                + "      SELECT id, 4 AS service_type, anc_4_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE anc_4_date IS NOT NULL\n"
                + "      UNION\n"
                + "      SELECT id, 5 AS service_type, tt_1_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE tt_1_date IS NOT NULL\n"
                + "      UNION  \n"
                + "      SELECT id, 6 AS service_type, tt_2_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE tt_2_date IS NOT NULL\n"
                + "      UNION \n"
                + "      SELECT id, 7 AS service_type, tt_booster_date AS service_delivery_date, last_modified_time, mobile_number  FROM report.mother_case WHERE tt_booster_date IS NOT NULL\n"
                + "      UNION  \n"
                + "      SELECT id, 8 AS service_type, ifa_tablets_100 AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE ifa_tablets_100 IS NOT NULL\n"
                + "      UNION \n"
                + "      SELECT id, 9 AS service_type, add AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE add IS NOT NULL\n"
                + "     ) all_cases\n"
                + "   WHERE last_modified_time BETWEEN '"
                + startDate.toString()
                + "' AND '"
                + endDate.toString()
                + "'\n"
                + "  ),\n"
                + "mcts_updated_services AS\n"
                + "  (SELECT mother.id, su.service_type \n"
                + "   FROM report.mcts_pregnant_mother mother JOIN report.mcts_pregnant_mother_service_update su ON mother.id = su.mcts_id\n"
                + "  ),\n"
                + "bp_form AS\n"
                + "(SELECT case_id, max(anc1_hemoglobin) as anc1_hemoglobin,\n"
                + "max(anc2_hemoglobin) as anc2_hemoglobin,\n"
                + "max(anc3_hemoglobin) as anc3_hemoglobin,\n"
                + "max(anc4_hemoglobin) as anc4_hemoglobin from report.bp_form group by case_id )\n"
                + "SELECT mother.id,\n"
                + "       mother.mcts_id,\n"
                + "       updatable_cases.service_type AS \"Service Type\",\n"
                + "       updatable_cases.service_delivery_date,\n"
                + "       updatable_cases.mobile_number,\n"
                + "       bp.anc1_hemoglobin,\n"
                + "       bp.anc2_hemoglobin,\n"
                + "       bp.anc3_hemoglobin,\n"
                + "       bp.anc4_hemoglobin\n"
                + "FROM updatable_cases\n"
                + "LEFT JOIN bp_form bp on bp.case_id=updatable_cases.mother_case_id\n"
                + "JOIN report.mcts_pregnant_mother mother ON updatable_cases.mother_case_id = mother.mother_case_id\n"
                + "WHERE (mother.id, service_type) NOT IN (SELECT * FROM mcts_updated_services);\n";
        // TODO split this statement.
        // return mapToBeneficiaryList(getCurrentSession().createSQLQuery(
        // queryString).list());
        return null;
    }

    private List<Beneficiary> mapToBeneficiaryList(List<Object[]> result) {
        List<Beneficiary> beneficiaries = new ArrayList<>();
        for (Object[] record : result) {
            beneficiaries.add(new Beneficiary((Integer) record[0],
                    (String) record[1], (Integer) record[2], (Date) record[3],
                    (String) record[4], (Integer) record[5],
                    (Integer) record[6], (Integer) record[7],
                    (Integer) record[8]));
        }
        return beneficiaries;
    }

    public <T> void saveOrUpdate(T entity) {
        dbRepository.save(entity);
    }

    @SuppressWarnings("unchecked")
    public <T> T findEntityByField(Class<T> entityClass, String fieldName,
            Object fieldValue) {
        return dbRepository.get(entityClass, fieldName, fieldValue);

    }

    public <T> Integer getDetachedFieldId(T instance) {
        return dbRepository.getDetachedFieldId(instance);
    }

    /**
     *
     * @param entityClass
     * @param fieldName
     * @param fieldValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findListOfEntitiesByField(Class<T> entityClass,
            String fieldName, Object fieldValue) {
        return dbRepository.findEntitiesByField(entityClass, fieldName,
                fieldValue);
    }

    /**
     *
     * @param entityClass
     * @param fieldName
     * @param fieldValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findListOfEntitiesByMultipleField(Class<T> entityClass,
            Map<String, Object> fieldParams) {
        return (List<T>) dbRepository
                .getListOfObjects(entityClass, fieldParams);

    }

    public <T> List<T> findEntityByFieldWithConstarint(Class<T> entityClass,
            String fieldName, Object lowerFieldValue, Object higherFieldValue) {

        List<T> listOfObjects = dbRepository.findEntitiesByFieldWithConstraint(
                entityClass, fieldName, lowerFieldValue, higherFieldValue);
        return listOfObjects;
    }

    public MctsDistrict findUniqueDistrict(final int disctrictId,
            final Integer id) {
        @SuppressWarnings("unchecked")
        EqualProperty<Integer> ep = (EqualProperty<Integer>) PropertyBuilder
                .create("disctrictId", disctrictId);
        @SuppressWarnings("unchecked")
        EqualProperty<Integer> ep1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsState.id", id);

        List<Property> properties = new ArrayList<Property>();
        properties.add(ep);
        properties.add(ep1);
        List<MctsDistrict> list = dbRepository.executeJDO(MctsDistrict.class,
                properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public MctsTaluk findUniqueTaluk(final int talukId, final Integer id) {
        EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder
                .create("talukId", talukId);
        EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsDistrict.id", id);
        List<Property> properties = new ArrayList<Property>();
        properties.add(eq);
        properties.add(eq1);
        List<MctsTaluk> list = dbRepository.executeJDO(MctsTaluk.class,
                properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    public MctsHealthblock findUniqueHealthBlock(final int healthblockId,
            final Integer id) {
        EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder
                .create("healthblockId", healthblockId);
        EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsTaluk.id", id);
        List<Property> properties = new ArrayList<Property>();
        properties.add(eq);
        properties.add(eq1);
        List<MctsHealthblock> list = dbRepository.executeJDO(
                MctsHealthblock.class, properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public MctsPhc findUniquePhc(final int phcId, final Integer id) {
        EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder
                .create("phcId", phcId);
        EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsHealthblock.id", id);
        List<Property> properties = new ArrayList<Property>();
        properties.add(eq);
        properties.add(eq1);
        List<MctsPhc> list = dbRepository.executeJDO(MctsPhc.class, properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public MctsSubcenter findUniqueSubcentre(final int subcenterId,
            final Integer id) {
        EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder
                .create("subcenterId", subcenterId);
        EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsPhc.id", id);
        List<Property> properties = new ArrayList<Property>();
        properties.add(eq);
        properties.add(eq1);
        List<MctsSubcenter> list = dbRepository.executeJDO(MctsSubcenter.class,
                properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public MctsVillage findUniqueVillage(final int villageId, final Integer id,
            final Integer id2) {

        EqualProperty<Integer> ep = (EqualProperty<Integer>) PropertyBuilder
                .create("villageId", villageId);
        EqualProperty<Integer> ep1 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsSubcenter.id", id);
        EqualProperty<Integer> ep2 = (EqualProperty<Integer>) PropertyBuilder
                .create("mctsTaluk.id", id2);
        List<Property> properties = new ArrayList<Property>();
        properties.add(ep);
        properties.add(ep1);
        properties.add(ep2);
        List<MctsVillage> list = dbRepository.executeJDO(MctsVillage.class,
                properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    public String getCaseGroupIdfromAshaId(int id) {
        MctsHealthworker worker = dbRepository.get(MctsHealthworker.class,
                "healthworkerId", id);
        if (worker != null) {
            return worker.getCareGroupid();
        }
        return null;

    }

    public List<MctsPregnantMother> getMctsPregnantMother() {
        EqualProperty<String> ep = (EqualProperty<String>) PropertyBuilder
                .create("mctsPersonaCaseUId", null);
        EqualProperty<String> ep1 = (EqualProperty<String>) PropertyBuilder
                .create("ownerId", null);
        List<Property> properties = new ArrayList<Property>();
        properties.add(ep);
        properties.add(ep1);
        List<MctsPregnantMother> list = dbRepository.executeJDO(
                MctsPregnantMother.class, properties);
        return list;

    }

    public MctsPregnantMother getMotherFromPrimaryId(int primaryId) {
        MctsPregnantMother mother = dbRepository.getObjectByPrimaryKey(
                MctsPregnantMother.class, primaryId);
        return mother;

    }

    public MotherCase matchMctsPersonawithMotherCase(final Integer hhNum,
            final Integer familyNum, final String ownerId) {
        EqualProperty<Integer> ep = (EqualProperty<Integer>) PropertyBuilder
                .create("hhNumber", hhNum);
        EqualProperty<Integer> ep1 = (EqualProperty<Integer>) PropertyBuilder
                .create("familyNum", familyNum);
        EqualProperty<Integer> ep2 = (EqualProperty<Integer>) PropertyBuilder
                .create("ownerId", ownerId);
        List<Property> properties = new ArrayList<Property>();
        properties.add(ep);
        properties.add(ep1);
        properties.add(ep2);
        List<MotherCase> list = dbRepository.executeJDO(MotherCase.class,
                properties);
        if (list != null && list.size() != 0) {
            return list.get(0);
        } else {
            return null;
        }

    }

    public String getOwnerIdFromLocationId(String locationId) {

        Flw flw = dbRepository.get(Flw.class, "locationCode", locationId);
        if (flw != null) {
            return flw.getFlwId();
        }
        return null;

    }

    public List<MctsPregnantMother> getMctsPregnantMotherForClosedCases() {
        DateTime date = new DateTime();
        DateTime lastDate = date.minusDays(180);
        final Date endDate = lastDate.toDate();

        RangeProperty<Date> rp = (RangeProperty<Date>) PropertyBuilder.create(
                "creationTime", new Range<Date>(null, endDate));
        EqualProperty<MCTSPregnantMotherCaseAuthorisedStatus> ep = (EqualProperty<MCTSPregnantMotherCaseAuthorisedStatus>) PropertyBuilder
                .create("mCTSPregnantMotherCaseAuthorisedStatus", null);
        EqualProperty<MCTSPregnantMotherMatchStatus> ep1 = (EqualProperty<MCTSPregnantMotherMatchStatus>) PropertyBuilder
                .create("mCTSPregnantMotherMatchStatus", null);

        List<Property> proeprties = new ArrayList<Property>();
        proeprties.add(ep);
        proeprties.add(ep);
        proeprties.add(ep1);
        List<MctsPregnantMother> list = dbRepository.executeJDO(
                MctsPregnantMother.class, proeprties);
        return list;
    }
}
