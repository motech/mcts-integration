package org.motechproject.mcts.integration.commcare;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.CreateTask;
import org.motechproject.commcare.domain.UpdateTask;

import org.motechproject.event.listener.EventRelay;

import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.motechproject.mcts.integration.service.StubDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author aman Class to convert Object to xml
 */
@Transactional
@Service
public class CreateCaseXmlService {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSFormUpdateService.class);



	@Autowired
	StubDataService stubDataService;

	
	private CommcareCaseConvertor caseToXmlConvertor;


	public CommcareCaseConvertor getCaseToXmlConvertor() {
		return caseToXmlConvertor;
	}

	public void setCaseToXmlConvertor(CommcareCaseConvertor caseToXmlConvertor) {
		this.caseToXmlConvertor = caseToXmlConvertor;
	}

	@Autowired
	CareDataRepository careDataRepository;

	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
	FixtureDataService fixtureDataService;

	//TODO renove all class vvariabes,,call tat workerid only once
	private String returnvalue;
	private String dateModified;
	private String userId;
	private CreateTask createTask;
	private UpdateTask updateTask;
	private CaseTask caseTask;

	public void createCaseXml() throws BeneficiaryException {

		List<MctsPregnantMother> mctsPregnantMother = careDataRepository
				.getMctsPregnantMother();
		LOGGER.debug("size :" + mctsPregnantMother.size());

		for (int i = 0; i < mctsPregnantMother.size(); i++) {

			createXmlForBeneficiary(mctsPregnantMother.get(i));
		}

	}

	public void createXmlForBeneficiary(MctsPregnantMother mctsPregnantMother)
			throws BeneficiaryException {

		if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
			String xmlns = "http://commcarehq.org/case/transaction/v2";
			String caseId = UUID.randomUUID().toString();
			userId = Integer.toString(mctsPregnantMother
					.getMctsHealthworkerByAshaId().getHealthworkerId());
			caseTask = new CaseTask();
			DateTime date = new DateTime();
			dateModified = date.toString();

			CreateTask task = createTaskandReturn(mctsPregnantMother);
			UpdateTask updatedTask = updateTaskandReturn(mctsPregnantMother);
			caseTask.setCreateTask(task);
			caseTask.setUpdateTask(updatedTask);
			caseTask.setXmlns(xmlns);
			caseTask.setDateModified(dateModified);
			caseTask.setCaseId(caseId);
			caseTask.setUserId(userId);

			returnvalue = caseToXmlConvertor.convertToCaseXml(caseTask);
			LOGGER.debug("returned : " + returnvalue);
		} else {
			// throw new BeneficiaryException("");
		}
	}

	/**
	 * Method to create Object updateTask and return it
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	private UpdateTask updateTaskandReturn(MctsPregnantMother mctsPregnantMother)
			throws BeneficiaryException {
		updateTask = new UpdateTask();

		updateTask.setCaseName(mctsPregnantMother.getName());
		updateTask.setCaseType("mcts_persona");
		updateTask.setDateOpened(new DateTime().toString());
		String husbandName = mctsPregnantMother.getFatherHusbandName();
		String mctsId = mctsPregnantMother.getMctsId();
		String phone = mctsPregnantMother.getMobileNo();

		if (husbandName == null) {
			husbandName = " ";
		}
		if (mctsId == null) {
			mctsId = " ";
		}
		if (phone == null) {
			phone = " ";
		}
		Date birth = mctsPregnantMother.getBirthDate();
		DateTime birthDate = new DateTime(mctsPregnantMother.getBirthDate());

		int workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
				.getHealthworkerId();
		String ownerId = fixtureDataService.getCaseGroupIdfromAshaId(workerId);
		DateTime date = new DateTime();
		Integer age;
		String dob;
		if (birth != null) {
			dob = birthDate.toString();
			age = Days.daysBetween(date.withTimeAtStartOfDay(),
					birthDate.withTimeAtStartOfDay()).getDays() / 365;
		} else {
			age = 30;
			dob = " ";
		}

		updateTask.setOwnerId(ownerId);
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("mcts_fullname", mctsPregnantMother.getName());
		hm.put("mcts_husband_name", husbandName);
		hm.put("mcts_age", age.toString());
		hm.put("mcts_dob", dob);
		hm.put("mcts_edd", date.toString());
		hm.put("mcts_id", mctsId);
		hm.put("mcts_phone_number", phone);
		hm.put("asha_id", Integer.toString(workerId));

		updateTask.setFieldValues(hm);
		return updateTask;

	}

	/**
	 * Method to create Object createTask and return it
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	private CreateTask createTaskandReturn(MctsPregnantMother mctsPregnantMother)
			throws BeneficiaryException {
		createTask = new CreateTask();

		createTask.setCaseType("mcts_persona");
		createTask.setCaseName(mctsPregnantMother.getName());
		int workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
				.getHealthworkerId();
		String ownerId = fixtureDataService.getCaseGroupIdfromAshaId(workerId);

		createTask.setOwnerId(ownerId);

		return createTask;

	}
}