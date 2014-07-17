package org.motechproject.mcts.integration.service;

import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.MCTSBatchConstants;
import org.motechproject.mcts.utils.PropertyReader;
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

	@Autowired
	private StubDataService stubDataService;
	@Autowired
	private CareDataRepository careDataRepository;
	
	@Autowired PropertyReader propertyReader;

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
	public void updateGroupId() throws BeneficiaryException {
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
				;
				careDataRepository.saveOrUpdate(mctsHealthworker);
			} else {
				//TODO log the error !
			}

		}
	}

	public String getCaseGroupIdfromAshaId(int id) throws BeneficiaryException {
		/*
		 * if (healthworkerId=="") { return "6ed07f7dca6e2fb170a17446c2499ba7";
		 * }
		 */

		String caseGroupId = careDataRepository.getCaeGroupIdfromAshaId(id);
		if (caseGroupId == null) {
			updateGroupId();
			String caseId = careDataRepository.getCaeGroupIdfromAshaId(id);
			if (caseId == null) {
				return propertyReader.getOwnerId();
			} else {
				return caseGroupId;
			}

		} else {
			return caseGroupId;
		}

	}

}
