package org.motechproject.mcts.integration.service;

import java.util.List;

import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.care.common.mds.model.MctsHealthworker;
import org.motechproject.mcts.integration.model.Data;
import org.motechproject.mcts.integration.repository.MctsRepository;
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
@Service
public class FixtureDataService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FixtureDataService.class);

	@Autowired
	private StubDataService stubDataService;
	@Autowired
	private MctsRepository careDataRepository;

	@Autowired
	private PropertyReader propertyReader;

	public MctsRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(MctsRepository careDataRepository) {
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
		List<Data> list = stubDataService.getFixtureData();
		for (int j = 0; j < list.size(); j++) {
			Data listData = list.get(j);
			for (int i = 0; i < listData.getObjects().size(); i++) {
				String id = listData.getObjects().get(i).getFields().getId()
						.getFieldList().get(0).getFieldValue();
                int healthWorkerId = Integer.parseInt(id);
				MctsHealthworker mctsHealthworker = careDataRepository
						.findEntityByField(MctsHealthworker.class,
								"healthworkerId", healthWorkerId);
				if (mctsHealthworker != null) {
					mctsHealthworker.setCareGroupid(listData.getObjects()
							.get(i).getFields().getGroupId().getFieldList()
							.get(0).getFieldValue());
					careDataRepository.saveOrUpdate(mctsHealthworker);
				} else {
					LOGGER.error("No MCTS Healthworker found with id : " + id);
				}

			}
		}

	}

	public String getCaseGroupIdfromAshaId(Integer id, String mctsId) {
		String locationId;
		String caseGroupId;
		if (id == null) {
			locationId = mctsId.substring(0, 10);
			caseGroupId = careDataRepository
					.getOwnerIdFromLocationId(locationId);
			if (caseGroupId == null) {
				return caseGroupId;
			}
		}
		caseGroupId = careDataRepository.getCaseGroupIdfromAshaId(id);
		if (caseGroupId == null) {

			updateGroupId();
			caseGroupId = careDataRepository.getCaseGroupIdfromAshaId(id);
			if (caseGroupId == null) {
				locationId = mctsId.substring(0, 10);
				caseGroupId = careDataRepository
						.getOwnerIdFromLocationId(locationId);
				if (caseGroupId == null) {
					return "6ed07f7dca6e2fb170a17446c2499ba7";
				}
				return caseGroupId;
			} else {
				return caseGroupId;
			}

		} else {
			return caseGroupId;
		}

	}

}
