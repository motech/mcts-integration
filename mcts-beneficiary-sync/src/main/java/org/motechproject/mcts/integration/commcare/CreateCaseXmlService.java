package org.motechproject.mcts.integration.commcare;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.motechproject.mcts.integration.service.StubDataService;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
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
	
	@Autowired PropertyReader propertyReader;

	
	/*private CommcareCaseConvertor caseToXmlConvertor;


	public CommcareCaseConvertor getCaseToXmlConvertor() {
		return caseToXmlConvertor;
	}

	public void setCaseToXmlConvertor(CommcareCaseConvertor caseToXmlConvertor) {
		this.caseToXmlConvertor = caseToXmlConvertor;
	}*/

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


	public void createCaseXml() throws Exception {

		List<MctsPregnantMother> mctsPregnantMother = careDataRepository
				.getMctsPregnantMother();
		LOGGER.debug("size :" + mctsPregnantMother.size());

		for (int i = 0; i < mctsPregnantMother.size(); i++) {

			createXmlForBeneficiary(mctsPregnantMother.get(i));
		}

	}

	public void createXmlForBeneficiary(MctsPregnantMother mctsPregnantMother)
			throws Exception {
		int workerId;
		LOGGER.debug("size :" + mctsPregnantMother.getMctsId());
		if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
			workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
					.getHealthworkerId(); }
		else {
			workerId = 51;
		}
			
			String ownerId = fixtureDataService.getCaseGroupIdfromAshaId(workerId);
			String xmlns = "http://commcarehq.org/case/transaction/v2";
			String caseId = UUID.randomUUID().toString();
			String userId = propertyReader.getUserIdforCommcare();
			Case caseTask = new Case();
			DateTime date = new DateTime();
			String dateModified = date.toString();

			CreateTask task = createTaskandReturn(mctsPregnantMother, workerId, ownerId);
			UpdateTask updatedTask = updateTaskandReturn(mctsPregnantMother, workerId, ownerId);
			caseTask.setCreateTask(task);
			caseTask.setUpdateTask(updatedTask);
			caseTask.setXmlns(xmlns);
			caseTask.setDateModified(dateModified);
			caseTask.setCaseId(caseId);
			caseTask.setUserId(userId);

			String returnvalue = ObjectToXMLConverter.converObjectToXml(
					caseTask,
					Case.class);
			LOGGER.debug("returned : " + returnvalue);
		
	}

	/**
	 * Method to create Object updateTask and return it
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	private UpdateTask updateTaskandReturn(MctsPregnantMother mctsPregnantMother, int workerId, String ownerId)
			throws BeneficiaryException {
		UpdateTask updateTask = new UpdateTask();
		
		
		String husbandName = mctsPregnantMother.getFatherHusbandName();
		String mctsId = mctsPregnantMother.getMctsId();
		String phone = mctsPregnantMother.getMobileNo();
		Date birth = mctsPregnantMother.getBirthDate();
		DateTime birthDate = new DateTime(mctsPregnantMother.getBirthDate());
		DateTime date = new DateTime();
		String age;
		String dob;
		
		if (birth != null) {
			dob = birthDate.toString();
			age = Integer.toString(Days.daysBetween(date.withTimeAtStartOfDay(),
					birthDate.withTimeAtStartOfDay()).getDays() / 365);
		} else {
			age = " ";
			dob = " ";
		}
		if (husbandName == null) {
			husbandName = " ";
		}
		if (mctsId == null) {
			mctsId = " ";
		}
		if (phone == null) {
			phone = " ";
		}
		
		updateTask.setCaseName(mctsPregnantMother.getName());
		updateTask.setCaseType("mcts_persona");
		updateTask.setDateOpened(new DateTime().toString());
		updateTask.setOwnerId(ownerId);
		updateTask.setMctsHusbandName(husbandName);
		updateTask.setMctsFullname(mctsPregnantMother.getName());
		updateTask.setMctsAge(age);
		updateTask.setMctsDob(dob);
		updateTask.setMctsEdd(date.toString());
		updateTask.setMctsId(mctsId);
		updateTask.setMctsPhoneNumber(phone);
		updateTask.setAshaId(Integer.toString(workerId));

		return updateTask;

	}

	/**
	 * Method to create Object createTask and return it
	 * 
	 * @param mctsPregnantMother
	 * @return
	 * @throws BeneficiaryException
	 */
	private CreateTask createTaskandReturn(MctsPregnantMother mctsPregnantMother, int workerId, String ownerId)
			throws BeneficiaryException {
		CreateTask createTask = new CreateTask();

		createTask.setCaseType("mcts_persona");
		createTask.setCaseName(mctsPregnantMother.getName());
		
		

		createTask.setOwnerId(ownerId);

		return createTask;

	}
}
