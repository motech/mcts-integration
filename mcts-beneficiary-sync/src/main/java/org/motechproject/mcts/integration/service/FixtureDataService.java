package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthworker;
import org.motechproject.mcts.integration.model.DataList;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service to  update the table
 * mcts_Healthworker in the database
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

	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	
	/**
	 * Method to update the mcts_Healthworker table in database. Create
	 * case_groupId.
	 * @throws BeneficiaryException 
	 */
	public void updateGroupId() throws BeneficiaryException {
		DataList listData = stubDataService.getFixtureData();
		for (int i = 0; i < listData.getDataList().size(); i++) {
			String id = listData.getDataList().get(i).getFields().getId();
			MctsHealthworker mctsHealthworker = careDataRepository
					.getHealthWorkerfromId(id);
			if (mctsHealthworker != null) {
				mctsHealthworker.setCareGroupid(listData.getDataList().get(i)
						.getFields().getGroupId());
				;
				careDataRepository.saveOrUpdate(mctsHealthworker);
			}

		}
	}
	
	public String getCaseGroupIdfromAshaId(int healthworkerId) {
		String caseGroupId = careDataRepository.getCaeGroupIdfromAshaId(healthworkerId);
		if (caseGroupId == null) {
			return "caseGroupId not present";
		}
		else {
			return caseGroupId;
		}
		
	}

}
