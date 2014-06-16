package org.motechproject.mcts.integration.service;


import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMotherServiceUpdate;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CareDataService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataService.class);

    @Autowired
    private CareDataRepository careDataRepository;

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        return careDataRepository.getBeneficiariesToSync(startDate, endDate);
    }

    public void mapMotherCaseToMctsPregnantMother(String caseId, String mctsId) throws BeneficiaryException {
        MotherCase motherCase = careDataRepository.findEntityByField(MotherCase.class, "caseId", caseId);
        if (motherCase == null) {
            LOGGER.info(String.format("MCTS Pregnant Mother not updated. Mother case not found for Case Id: %s", caseId));
            return;
        }

        MctsPregnantMother MctsPregnantMother = getExistingOrNewMctsPregnantMother(mctsId, motherCase);
        careDataRepository.saveOrUpdate(MctsPregnantMother);
    }

    private MctsPregnantMother getExistingOrNewMctsPregnantMother(String mctsId, MotherCase motherCase) {
        MctsPregnantMother MctsPregnantMother = careDataRepository.findEntityByField(MctsPregnantMother.class, "motherCase", motherCase);

        if (MctsPregnantMother != null) {
            LOGGER.info(String.format("MCTS Pregnant Mother already exists with MCTS Id: %s for Mother Case: %s. Updating it with new MCTS Id: %s.", MctsPregnantMother.getMctsId(), motherCase.getCaseId(), mctsId));
            MctsPregnantMother.setMctsId(mctsId);
        } else {
            LOGGER.info(String.format("Creating MCTS Pregnant Mother with MCTS Id: %s for Mother Case: %s.", mctsId, motherCase.getCaseId()));
            MctsPregnantMother = new MctsPregnantMother();
            MctsPregnantMother.setMctsId(mctsId);
            MctsPregnantMother.setMotherCase(motherCase);
       }
        return MctsPregnantMother;
    }

    public void updateSyncedBeneficiaries(List<Beneficiary> syncedBeneficiaries) throws BeneficiaryException {
        LOGGER.info(String.format("Updating %s beneficiaries as updated to MCTS", syncedBeneficiaries.size()));
        DateTime serviceUpdateTime = DateTime.now();
        for (Beneficiary syncedBeneficiary : syncedBeneficiaries) {
            MctsPregnantMother MctsPregnantMother = careDataRepository.load(MctsPregnantMother.class, syncedBeneficiary.getMctsPregnantMotherId());
            MctsPregnantMotherServiceUpdate mctsPregnantMotherServiceUpdate =
                    new MctsPregnantMotherServiceUpdate();
            mctsPregnantMotherServiceUpdate.setMctsPregnantMother(MctsPregnantMother);
            mctsPregnantMotherServiceUpdate.setServiceDeliveryDate(syncedBeneficiary.getServiceDeliveryDate());
            mctsPregnantMotherServiceUpdate.setServiceType(Short.valueOf(syncedBeneficiary.getServiceType().toString()));
            mctsPregnantMotherServiceUpdate.setServiceUpdateTime(new Timestamp(serviceUpdateTime.getMillis()));
            
            careDataRepository.saveOrUpdate(mctsPregnantMotherServiceUpdate);
        }
    }
    
    /**
     * Method to get entities from db with constraints of upper and lower value on a particular field 
     * @param entityClass: class whose data is to be fetched
     * @param fieldName: name of the field on which constraint is to be apllied
     * @param lowerFieldValue: lower value of the constraint
     * @param higherFieldValue: higher value of the constraint
     * @return list of the values of passes entity class with the constraints
     */
    public <T> List<T> findEntityByFieldWithConstarint(Class<T> entityClass, String fieldName,
			Object lowerFieldValue, Object higherFieldValue) {
    	LOGGER.debug(String.format("Params received are Class: [%s], fieladName: [%s], lowerFieldValue: [%s], higherFieldValue: [%s]", entityClass.getSimpleName(), fieldName, lowerFieldValue, higherFieldValue));
		return (List<T>) careDataRepository.findEntityByFieldWithConstarint(entityClass, fieldName, lowerFieldValue, higherFieldValue);
	}
    
    /**
     * Method to get the unique element of the <code>entityClass</code> having a specific value for a field
     * @param entityClass: class whose data is to fetched from db
     * @param fieldName: field name whose value is to be matched
     * @param fieldValue: value to be matched
     * @return list of entities with matching field values
     */
    public <T> T findEntityByField(Class<T> entityClass, String fieldName,
			Object fieldValue) {
    	LOGGER.debug(String.format("Params received are Class: [%s], fieladName: [%s], fieldValue: [%s]", entityClass.getSimpleName(), fieldName, fieldValue));
		return careDataRepository.findEntityByField(entityClass, fieldName,
			fieldValue);
	}

    /**
     * Method to get all the element of the <code>entityClass</code> having a specific value for a field
     * @param entityClass: class whose data is to fetched from db
     * @param fieldName: field name whose value is to be matched
     * @param fieldValue: value to be matched
     * @return list of entities with matching field values
     */
    public <T> List<T> findListOfEntitiesByField(Class<T> entityClass, String fieldName,
			Object fieldValue) {
    	LOGGER.debug(String.format("Params received are Class: [%s], fieladName: [%s], fieldValue: [%s]", entityClass.getSimpleName(), fieldName, fieldValue));
		return careDataRepository.findListOfEntitiesByField(entityClass, fieldName,
			fieldValue);
	}

    /**
     * Method to Save of Update the entity in Db
     * @param entity
     * @throws BeneficiaryException 
     */
    public <T> void saveOrUpdate(T entity) throws BeneficiaryException {
		careDataRepository.saveOrUpdate(entity);
	}
}
