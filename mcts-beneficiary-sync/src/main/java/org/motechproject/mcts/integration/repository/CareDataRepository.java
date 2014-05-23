package org.motechproject.mcts.integration.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.motechproject.mcts.integration.hibernate.model.MctsPhc;
import org.motechproject.mcts.integration.hibernate.model.MctsSubcenter;
import org.motechproject.mcts.integration.hibernate.model.MctsVillage;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CareDataRepository {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CareDataRepository.class);

	@Autowired
	private SessionFactory sessionFactory;

	private static final String SEQUENCE = "report.locationdata_location_id_seq";
	
	@Autowired
	public CareDataRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
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
				+ "      UNION \n"
				+ "      SELECT id, 6 AS service_type, tt_2_date AS service_delivery_date, last_modified_time, mobile_number FROM report.mother_case WHERE tt_2_date IS NOT NULL\n"
				+ "      UNION \n"
				+ "      SELECT id, 7 AS service_type, tt_booster_date AS service_delivery_date, last_modified_time, mobile_number  FROM report.mother_case WHERE tt_booster_date IS NOT NULL\n"
				+ "      UNION \n"
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
				+ "		updatable_cases.mobile_number,\n"
				+ "	    bp.anc1_hemoglobin,\n"
				+ "	    bp.anc2_hemoglobin,\n"
				+ "	    bp.anc3_hemoglobin,\n"
				+ "	    bp.anc4_hemoglobin\n"
				+ "FROM updatable_cases\n"
				+ "LEFT JOIN bp_form bp on bp.case_id=updatable_cases.mother_case_id\n"
				+ "JOIN report.mcts_pregnant_mother mother ON updatable_cases.mother_case_id = mother.case_id\n"
				+ "WHERE (mother.id, service_type) NOT IN (SELECT * FROM mcts_updated_services);\n";

		return mapToBeneficiaryList(getCurrentSession().createSQLQuery(
				queryString).list());
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
		getCurrentSession().saveOrUpdate(entity);
	}

	public <T> T findEntityByField(Class<T> entityClass, String fieldName,
			Object fieldValue) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName, fieldValue));
		return (T) criteria.uniqueResult();
	}

	public <T> T load(Class<T> entityClass, Integer id) {
		return (T) getCurrentSession().load(entityClass, id);
	}
	
	public <T> List<T> findEntityByFieldWithConstarint(Class<T> entityClass, String fieldName,
			Object lowerFieldValue, Object higherFieldValue) {
		LOGGER.debug(String.format("Params received are Class: [%s], fieladName: [%s], lowerFieldValue: [%s], higherFieldValue: [%s]", entityClass.getSimpleName(), fieldName, lowerFieldValue, higherFieldValue));
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.between(fieldName, lowerFieldValue, higherFieldValue));
		List<T> listOfObjects = (List<T>) criteria.list();
		LOGGER.debug(listOfObjects.toString());
		return listOfObjects;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public long getNextKey() {
		  Query query = sessionFactory.getCurrentSession().createSQLQuery(
		    "select nextval('" + SEQUENCE + "')");
		  Long key = Long.parseLong(query.uniqueResult().toString());
		  return key;
		 }
	
	public void flush() {
		getCurrentSession().flush();
	}
	
	/**
	 * Method to get PHC object from phc_id
	 * @param phcId
	 * @return
	 */
	public MctsPhc getMctsPhc(int phcId) {
		String queryString = "select phc from MctsPhc phc where phc.phcId='"+phcId+"'";
		List<Object> phc = getCurrentSession().createQuery(queryString).list();
		if(phc.size()!=0) {
		return (MctsPhc)phc.get(0);
		}
		else {
			return null;
		}
	}
	
	public List<Integer> getAwwRegisterMotherFormMctsId() {
		String queryString = "select form.mctsId from AwwRegisterMotherForm from";
		List<Integer> mcts_Ids = getCurrentSession().createQuery(queryString).list();
		return mcts_Ids;
	}
	
	public List<Integer> getRegistrationMotherFormMctsId() {
		String queryString = "select form.mctsId from RegistrationMotherForm from";
		List<Integer> mcts_Ids = getCurrentSession().createQuery(queryString).list();
		return mcts_Ids;
	}

	public MctsSubcenter getMctsSubcentre(int subcentreId) {
		String queryString = "select subcentre from MctsSubcenter subcentre where subcentre.subcenterId='"+subcentreId+"'";
		List<Object> subCentre = getCurrentSession().createQuery(queryString).list();
		if(subCentre.size()!=0) {
			return (MctsSubcenter)subCentre.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public MctsVillage getMctsVillage(int villageId) {
		String queryString = "select village from MctsVillage village where village.villageId='"+villageId+"'";
		List<Object> village = getCurrentSession().createQuery(queryString).list();
		if(village.size()!=0) {
			return (MctsVillage)village.get(0);
		}
		else {
			return null;
		}
	}
	
}