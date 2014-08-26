package org.motechproject.mcts.integration.service;

import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.MCTSBatchConstants;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to update the table mcts_Healthworker in the database
 *
 * @author aman
 *
 */
@Transactional
@Service
public class FixtureDataService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FixtureDataService.class);

    @Autowired
    private StubDataService stubDataService;
    @Autowired
    private CareDataRepository careDataRepository;

    @Autowired
    private PropertyReader propertyReader;

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    /**
     * Method to update the mcts_Healthworker table in database. Create
     * case_groupId.
     *
     * @throws BeneficiaryException
     */
    @MotechListener(subjects = MCTSBatchConstants.EVENT_SUBJECT)
    public void updateGroupId() {
        Data listData = stubDataService.getFixtureData();
        for (int i = 0; i < listData.getObjects().size(); i++) {
            String id = listData.getObjects().get(i).getFields().getId()
                    .getFieldList().get(0).getFieldValue();

            MctsHealthworker mctsHealthworker = careDataRepository
                    .getHealthWorkerfromId(id);
            if (mctsHealthworker != null) {
                mctsHealthworker.setCareGroupid(listData.getObjects().get(i)
                        .getFields().getGroupId().getFieldList().get(0)
                        .getFieldValue());
                careDataRepository.saveOrUpdate(mctsHealthworker);
            } else {
                LOGGER.error("No MCTS Healthworker found with id : " + id);
            }

        }
    }

    public String getCaseGroupIdfromAshaId(Integer id, String mctsId) {
        
        String caseGroupId;
        if (id == null) {
            //TODO remove the comments when care-reporting location-code is deployed.
            /*
            String locationId = mctsId.substring(0, 10);
            caseGroupId = careDataRepository.getOwnerIdFromLocationId(locationId);
            if (caseGroupId == null) {
                return null;
            }
            else {
                return caseGroupId;
            }
        */
        return null;    
        }
        caseGroupId = careDataRepository.getCaseGroupIdfromAshaId(id);
        if (caseGroupId == null) {
            
            updateGroupId();
            caseGroupId = careDataRepository.getCaseGroupIdfromAshaId(id);
            if (caseGroupId == null) {
                //TODO remove the comments when care-reporting location-code is deployed.
                /*
            
                String locationId = mctsId.substring(0, 10);
                caseGroupId = careDataRepository.getOwnerIdFromLocationId(locationId);
                if (caseGroupId == null) {
                    return null;
                }
                else {
                    return caseGroupId;
                }
                
                
            */
              return null;  
            } else {
                return caseGroupId;
            }

        } else {
            return caseGroupId;
        }

    }

}
