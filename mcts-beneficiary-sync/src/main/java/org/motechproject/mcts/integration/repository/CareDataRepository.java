package org.motechproject.mcts.integration.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherCaseAuthorisedStatus;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherMatchStatus;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;
import org.motechproject.mcts.care.common.mds.service.MctsDistrictMDSService;
import org.motechproject.mcts.care.common.mds.service.MctsPregnantMotherMDSService;
import org.motechproject.mcts.care.common.mds.service.MctsTalukMDSService;
import org.motechproject.mcts.care.common.mds.service.MctsVillageMDSService;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mds.query.EqualProperty;
import org.motechproject.mds.query.Property;
import org.motechproject.mds.query.PropertyBuilder;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.query.QueryExecutor;
import org.motechproject.mds.query.RangeProperty;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CareDataRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CareDataRepository.class);

	@Autowired
	private SessionFactory sessionFactory;

	private static final String SEQUENCE = "report.locationdata_location_id_seq";

	@Autowired
	public CareDataRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Autowired
	private MctsPregnantMotherMDSService mctsPregnantMotherMDSService;
	
	@Autowired
	private MctsVillageMDSService mctsVillageMDSService;
	
	@Autowired
	private MctsDistrictMDSService mctsDistrictMDSService; 
	
	@Autowired
	private MctsTalukMDSService mctsTalukMDSService;

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
		try {
			getCurrentSession().saveOrUpdate(entity);
		} catch (HibernateException e) {
			throw new BeneficiaryException(
					ApplicationErrors.DATABASE_OPERATION_FAILED, e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T findEntityByField(Class<T> entityClass, String fieldName,
			Object fieldValue) {
		try {
			Criteria criteria = getCurrentSession().createCriteria(entityClass);
			criteria.add(Restrictions.eq(fieldName, fieldValue));
			return (T) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new BeneficiaryException(
					ApplicationErrors.DATABASE_OPERATION_FAILED, e);
		}

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
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName, fieldValue));
		return (List<T>) criteria.list();
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
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		for (String key : fieldParams.keySet()) {
			criteria.add(Restrictions.eq(key, fieldParams.get(key)));
		}
		return (List<T>) criteria.list();
	}

	public <T> T load(Class<T> entityClass, Integer id) {
		return (T) getCurrentSession().load(entityClass, id);
	}

	public <T> List<T> findEntityByFieldWithConstarint(Class<T> entityClass,
			String fieldName, Object lowerFieldValue, Object higherFieldValue) {
		LOGGER.debug(String
				.format("Params received are Class: [%s], fieladName: [%s], lowerFieldValue: [%s], higherFieldValue: [%s]",
						entityClass.getSimpleName(), fieldName,
						lowerFieldValue, higherFieldValue));
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.between(fieldName, lowerFieldValue,
				higherFieldValue));
		List<T> listOfObjects = (List<T>) criteria.list();
		LOGGER.debug(listOfObjects.toString());
		return listOfObjects;
	}

	public <T> T findEntityByFieldWithSingleConstarint(Class<T> entityClass,
			String fieldName, Object value) {
		LOGGER.debug(String
				.format("Params received are Class: [%s], fieladName: [%s], FieldValue: [%s]",
						entityClass.getSimpleName(), fieldName, value));
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName, value));
		List<T> listOfObjects = (List<T>) criteria.list();
		if (listOfObjects.size() != 0) {
			return listOfObjects.get(0);
		} else {
			return null;
		}
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public long getNextKey() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(
				"select nextval('" + SEQUENCE + "')");
		return (Long.parseLong(query.uniqueResult().toString()));
	}

	public void flush() {
		getCurrentSession().flush();
	}

	/**
	 * Method to get PHC object from phc_id
	 * 
	 * @param phcId
	 * @return
	 */
	public MctsPhc getMctsPhc(int phcId) {
		String queryString = "select phc from MctsPhc phc where phc.phcId='"
				+ phcId + "'";
		List<Object> phc = getCurrentSession().createQuery(queryString).list();
		if (phc.size() != 0) {
			return (MctsPhc) phc.get(0);
		} else {
			return null;
		}
	}

	public List<Object[]> getmctsIdcaseId() {
		String queryString = "select m.motherCase.id, n.mctsId from AwwRegisterMotherForm m, MctsPregnantMother n where substring(m.fullMctsId,12,18) = substring(n.mctsId,12,18)";
		return (getCurrentSession().createQuery(queryString).list());
	}

	public void updateQuery(String queryString) {
		getCurrentSession().createQuery(queryString).executeUpdate();
	}

	public List<Object[]> getmctsIdcaseIdfromEditForm() {
		String queryString = "select m.motherCase.id, n.mctsId from MotherEditForm m, MctsPregnantMother n where substring(m.fullMctsId,12,18) = substring(n.mctsId,12,18)";
		return (getCurrentSession().createQuery(queryString).list());
	}

	public MctsHealthworker getHealthWorkerfromId(String id) {
		int healthWorkerId = Integer.parseInt(id);
		String queryString = "from MctsHealthworker worker where worker.healthworkerId='"
				+ healthWorkerId + "' and worker.type='ASHA'";
		LOGGER.debug("queryString" + queryString);
		List<Object> worker = getCurrentSession().createQuery(queryString)
				.list();

		if (worker.size() != 0) {
			return (MctsHealthworker) worker.get(0);
		} else {
			return null;
		}

	}

	public MctsDistrict findUniqueDistrict(final int disctrictId, final Integer id) {
		@SuppressWarnings("unchecked")
		List<MctsDistrict> list = mctsDistrictMDSService.executeQuery(new QueryExecution<List>() {

			@Override
			public List execute(javax.jdo.Query query,
					InstanceSecurityRestriction restriction) {
				EqualProperty<Integer> ep = (EqualProperty<Integer>) PropertyBuilder.create("disctrictId", disctrictId);
				EqualProperty<Integer> ep1 = (EqualProperty<Integer>) PropertyBuilder.create("mctsState.id", id);
				
				List<Property> properties = new ArrayList<Property>();
				properties.add(ep);
				properties.add(ep1);
				return (List) QueryExecutor.executeWithArray(query,
						properties);
			}
			
		});
		if (list.size() != 0) {
			return (MctsDistrict) list.get(0);
		} else {
			return null;
		}
	}

	public MctsTaluk findUniqueTaluk(final int talukId, final Integer id) {
		@SuppressWarnings("unchecked")
		List<MctsTaluk> list = mctsTalukMDSService.executeQuery(new QueryExecution<List>() {
			@Override
			public List execute(javax.jdo.Query query,
					InstanceSecurityRestriction restriction) {
				EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder.create("talukId", talukId);
				EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder.create("mctsDistrict.id", id);
				List<Property> properties = new ArrayList<Property>();
				properties.add(eq);
				properties.add(eq1);
				return (List) QueryExecutor.executeWithArray(query,
						properties);
			}
		});
		if (list.size() != 0) {
			return (MctsTaluk) list.get(0);
		} else {
			return null;
		}
		
	}

	public MctsHealthblock findUniqueHealthBlock(final int healthblockId, final Integer id) {
		@SuppressWarnings("unchecked")
		List<MctsHealthblock> list = mctsTalukMDSService.executeQuery(new QueryExecution<List>() {
			@Override
			public List execute(javax.jdo.Query query,
					InstanceSecurityRestriction restriction) {
				EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder.create("healthblockId", healthblockId);
				EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder.create("mctsTaluk.id", id);
				List<Property> properties = new ArrayList<Property>();
				properties.add(eq);
				properties.add(eq1);
				return (List) QueryExecutor.executeWithArray(query,
						properties);
			}
		});
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public MctsPhc findUniquePhc(final int phcId, final Integer id) {
		@SuppressWarnings("unchecked")
		List<MctsPhc> list = mctsTalukMDSService.executeQuery(new QueryExecution<List>() {
			@Override
			public List execute(javax.jdo.Query query,
					InstanceSecurityRestriction restriction) {
				EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder.create("phcId", phcId);
				EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder.create("mctsHealthblock.id", id);
				List<Property> properties = new ArrayList<Property>();
				properties.add(eq);
				properties.add(eq1);
				return (List) QueryExecutor.executeWithArray(query,
						properties);
			}
		});
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
		
	}

	public MctsSubcenter findUniqueSubcentre(final int subcenterId, final Integer id) {
		@SuppressWarnings("unchecked")
		List<MctsSubcenter> list = mctsTalukMDSService.executeQuery(new QueryExecution<List>() {
			@Override
			public List execute(javax.jdo.Query query,
					InstanceSecurityRestriction restriction) {
				EqualProperty<Integer> eq = (EqualProperty<Integer>) PropertyBuilder.create("subcenterId", subcenterId);
				EqualProperty<Integer> eq1 = (EqualProperty<Integer>) PropertyBuilder.create("mctsPhc.id", id);
				List<Property> properties = new ArrayList<Property>();
				properties.add(eq);
				properties.add(eq1);
				return (List) QueryExecutor.executeWithArray(query,
						properties);
			}
		});
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public MctsVillage findUniqueVillage(final int villageId, final Integer id, Integer id2) {
		
		@SuppressWarnings("unchecked")
		final List<MctsVillage> list = mctsVillageMDSService
				.executeQuery(new QueryExecution<List>() {
					@Override
					public List execute(javax.jdo.Query query,
							InstanceSecurityRestriction restriction) {
						EqualProperty<Integer> ep = (EqualProperty<Integer>) PropertyBuilder
								.create("villageId", villageId);
						EqualProperty<Integer> ep1 = (EqualProperty<Integer>) PropertyBuilder
								.create("mctsSubcenter.id", id);
						EqualProperty<Integer> ep2 = (EqualProperty<Integer>) PropertyBuilder
								.create("mctsTaluk.id", id);
						List<Property> properties = new ArrayList<Property>();
						properties.add(ep);
						properties.add(ep1);
						properties.add(ep2);
						return (List) QueryExecutor.executeWithArray(query,
								properties);
					}
				});
		
		if (list.size() != 0) {
			return (MctsVillage) list.get(0);
		} else {
			return null;
		}
		/*String queryString = "from MctsVillage village where village.villageId='"
				+ villageId
				+ "' and village.mctsSubcenter.id='"
				+ id
				+ "' and village.mctsTaluk.id='" + id2 + "'";
		List<MctsVillage> village = getCurrentSession()
				.createQuery(queryString).list();
		if (village.size() != 0) {
			return (MctsVillage) village.get(0);
		} else {
			return null;
		}
*/
	}

	public String getCaseGroupIdfromAshaId(int id) {
		String queryString = "select worker.careGroupid from MctsHealthworker worker where worker.healthworkerId='"
				+ id + "'";
		LOGGER.debug("query : " + queryString);
		List<String> caseGroupId = getCurrentSession().createQuery(queryString)
				.list();
		if (caseGroupId.size() == 0) {
			return null;
		}
		return caseGroupId.get(0);
	}

	public List<MctsPregnantMother> getMctsPregnantMother() {
		String caseId = null;
		String ownerId = null;
		String queryString = "from MctsPregnantMother mother where mother.mctsPersonaCaseUId="
				+ caseId + " and mother.ownerId<>" + ownerId + "";
		LOGGER.debug("query : " + queryString);
		List<MctsPregnantMother> mother = getCurrentSession().createQuery(
				queryString).list();

		return mother;
	}

	public MctsPregnantMother getMotherFromPrimaryId(int primaryId) {
		String queryString = "from MctsPregnantMother mother where mother.id='"
				+ primaryId + "'";
		LOGGER.debug("queryString : " + queryString);
		List<MctsPregnantMother> mother = getCurrentSession().createQuery(
				queryString).list();
		if (mother.size() == 0) {
			return null;
		}
		return mother.get(0);

	}

	public MotherCase matchMctsPersonawithMotherCase(int hhNum, int familyNum,
			String ownerId) {
		String queryString = "from MotherCase mcase where mcase.hhNumber='"
				+ hhNum + "' and mcase.familyNumber='" + familyNum
				+ "' and mcase.flwGroup.groupId=" + ownerId + "";
		List<MotherCase> motherCase = getCurrentSession().createQuery(
				queryString).list();
		if (motherCase.size() == 0) {
			return null;
		} else if (motherCase.size() > 1) {
			LOGGER.info("error : multiple match found");
			return null;
		} else {
			return motherCase.get(0);
		}

	}

	public MctsPregnantMother getMctsPregnantMotherFromCaseId(String id) {

		String queryString = "from MctsPregnantMother mPregMother where mPregMother.motherCase.id='"
				+ id + "'";
		List<MctsPregnantMother> motherList = getCurrentSession().createQuery(
				queryString).list();
		return motherList.get(0);
	}

	public String getOwnerIdFromLocationId(String locationId) {
		String queryString = "select flw.flwId from Flw flw where flw.locationCode='"
				+ locationId + "'";
		LOGGER.debug("query : " + queryString);
		List<String> ownerId = getCurrentSession().createQuery(queryString)
				.list();
		if (ownerId.size() == 0) {
			return null;
		}
		return ownerId.get(0);
	}

	public List<MctsPregnantMother> getMctsPregnantMotherForClosedCases() {
		DateTime date = new DateTime();
		DateTime lastDate = date.minusDays(180);
		final Date endDate = lastDate.toDate();

		@SuppressWarnings("unchecked")
		List<MctsPregnantMother> list = mctsPregnantMotherMDSService
				.executeQuery(new QueryExecution<List>() {
					@Override
					public List execute(javax.jdo.Query query,
							InstanceSecurityRestriction restriction) {
						RangeProperty<Date> rp = (RangeProperty<Date>) PropertyBuilder
								.create("creationTime", new Range<Date>(null,
										endDate));
						EqualProperty<MCTSPregnantMotherCaseAuthorisedStatus> ep = (EqualProperty<MCTSPregnantMotherCaseAuthorisedStatus>) PropertyBuilder
								.create("mCTSPregnantMotherCaseAuthorisedStatus",
										null);
						EqualProperty<MCTSPregnantMotherMatchStatus> ep1 = (EqualProperty<MCTSPregnantMotherMatchStatus>) PropertyBuilder
								.create("mCTSPregnantMotherMatchStatus", null);
                        
						List<Property> proeprties = new ArrayList<Property>();
						proeprties.add(ep);
						proeprties.add(ep);
						proeprties.add(ep1);
						// query.setFilter("creationTime <  param0");
						// query.setFilter("mCTSPregnantMotherCaseAuthorisedStatus==null");
						// query.setFilter("mCTSPregnantMotherMatchStatus==null");
						// query.declareParameters("java.util.Date param0");
						return (List) QueryExecutor.executeWithArray(query,
								proeprties);
					}
				});
		return list;
		/**
		 * DateTime date = new DateTime(); DateTime lastDate =
		 * date.minusDays(180); String queryString =
		 * "from MctsPregnantMother mother where mother.creationTime<DATE('" +
		 * lastDate.toString("yyyy-MM-dd") +
		 * "') and mother.mCTSPregnantMotherCaseAuthorisedStatus=null and mother.mCTSPregnantMotherMatchStatus=null"
		 * ; List<MctsPregnantMother> mother = getCurrentSession().createQuery(
		 * queryString).list(); if (mother.size() == 0) { return null; } return
		 * mother;
		 */
	}
}
