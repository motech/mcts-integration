package org.motechproject.mcts.integration.commcare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.FixtureDataService;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.motechproject.mcts.utils.CommcareConstants;
import org.motechproject.mcts.utils.MctsConstants;
import org.motechproject.mcts.utils.ObjectToXMLConverter;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to convert Object to xml
 *
 * @author aman
 */
@Transactional
@Service
public class CreateCaseXmlService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CreateCaseXmlService.class);


    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    private CareDataRepository careDataRepository;

    @Autowired
    private MCTSHttpClientService mCTSHttpClientService;

    @Autowired
    private FixtureDataService fixtureDataService;

    public CareDataRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public void createCaseXml() {

        List<MctsPregnantMother> mctsPregnantMother = careDataRepository
                .getMctsPregnantMother();
        LOGGER.debug("size :" + mctsPregnantMother.size());
        int size = mctsPregnantMother.size();
        if (size > 0) {
            int sizeOfXml = propertyReader.sizeOfXml();
            int times = size / sizeOfXml;
            if (times > 0) {
                for (int i = 0; i <= times; i++) {
                    Data data = createXml(mctsPregnantMother.subList(i
                            * sizeOfXml, (i + 1) * sizeOfXml - 1));
                    String returnvalue = ObjectToXMLConverter
                            .converObjectToXml(data, Data.class);
                    LOGGER.debug("returned : " + returnvalue);
                    HttpStatus status = mCTSHttpClientService
                            .syncToCommcare(data);
                    if (status.value() / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE) {
                        updateCaseUId(data);

                    }
                }
            } else {
                Data data = createXml(mctsPregnantMother);
                String returnvalue = ObjectToXMLConverter.converObjectToXml(
                        data, Data.class);
                LOGGER.debug("returned : " + returnvalue);

                // post xml to the url if response is 200 then only add
                // case UUID to the database
                HttpStatus status = mCTSHttpClientService.syncToCommcare(data);
                if (status.value() / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE) {
                    updateCaseUId(data);
                }
            }
        }

    }

    /**
     * This method is called after commcareHQ returns success after saving cases
     * @param data
     * @throws BeneficiaryException
     */
    private void updateCaseUId(Data data) {
        List<Case> cases = data.getCases();
        for (Case curr : cases) {
            MctsPregnantMother mother = careDataRepository
                    .getMotherFromPrimaryId(curr.getMctsPregnantMotherId());
            mother.setMctsPersonaCaseUId(curr.getCaseId());
            mother.setDateOpened(new DateTime().toString());
            careDataRepository.saveOrUpdate(mother);

        }
    }

    /**
     * Method which takes 50 or less cases at a time and creates Data Object.
     *
     * @param mctsPregnantMother
     * @return
     * @throws BeneficiaryException
     * @throws Exception
     */
    public Data createXml(List<MctsPregnantMother> mctsPregnantMother) {
        Data data = new Data();
        List<Case> cases = new ArrayList<Case>();
        data.setXmlns(CommcareConstants.DATAXMLNS);
        String userId = propertyReader.getUserIdforCommcare();
        Meta meta = createMetaandReturn(userId);
        meta.setTimeEnd(new DateTime().toString());
        data.setMeta(meta);
        for (int i = 0; i < mctsPregnantMother.size(); i++) {
            Case caseTask = createCaseForBeneficiary(mctsPregnantMother.get(i),
                    userId);

            if ((caseTask.getCreateTask().getCaseName() != null)
                    && (caseTask.getUpdateTask().getMctsHusbandName() != null)
                    && (caseTask.getUpdateTask().getMctsHusbandNameEn() != null)
                    && (caseTask.getUpdateTask().getMctsFullnameEn() != null)) {
                cases.add(caseTask);
            }

        }
        data.setCases(cases);

        return data;
    }

    /**
     * Method to create Case for indiavidual mother and returns it.
     *
     * @param mctsPregnantMother
     * @param userId
     * @return
     * @throws BeneficiaryException
     * @throws Exception
     */
    public Case createCaseForBeneficiary(MctsPregnantMother mctsPregnantMother,
            String userId) {

        Case caseTask = new Case();
        DateTime date = new DateTime();
        int workerId;

        if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
            workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
                    .getHealthworkerId();
        } else {
            workerId = -1;
        }
        String ownerId = mctsPregnantMother.getOwnerId();
        String caseId = UUID.randomUUID().toString();
        String dateModified = date.toString();
        CreateTask task = createTaskandReturn(mctsPregnantMother, workerId,
                ownerId);
        UpdateTask updatedTask = updateTaskandReturn(mctsPregnantMother,
                workerId, ownerId);
        caseTask.setCreateTask(task);
        caseTask.setUpdateTask(updatedTask);
        caseTask.setXmlns(CommcareConstants.XMLNS);
        caseTask.setDateModified(dateModified);
        caseTask.setCaseId(caseId);
        caseTask.setUserId(userId);
        caseTask.setMctsPregnantMotherId(mctsPregnantMother.getId());

        return caseTask;

    }

    /**
     * Method to create Object updateTask and return it
     *
     * @param mctsPregnantMother
     * @return
     * @throws BeneficiaryException
     */
    public UpdateTask updateTaskandReturn(
            MctsPregnantMother mctsPregnantMother, int workerId, String ownerId) {
        UpdateTask updateTask = new UpdateTask();

        String mctsName = mctsPregnantMother.getHindiName();
        String mctsNameEn = mctsPregnantMother.getName();
        String husbandName = mctsPregnantMother.getHindiFatherHusbandName();
        String husbandNameEn = mctsPregnantMother.getFatherHusbandName();
        String mctsId = mctsPregnantMother.getMctsId();
        String phone = mctsPregnantMother.getMobileNo();
        Date birth = mctsPregnantMother.getBirthDate();
        DateTime birthDate = new DateTime(mctsPregnantMother.getBirthDate());

        DateTime date = new DateTime();
        String age = "";
        String dob = "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        if (birth != null) {
            dob = fmt.print(birthDate);
            age = Integer.toString(Days.daysBetween(
                    birthDate.withTimeAtStartOfDay(),
                    date.withTimeAtStartOfDay()).getDays() / MctsConstants.NUMBER_OF_DAYS_IN_YEAR);
        }

        if (mctsPregnantMother.getLmpDate() != null) {
            DateTime lmpDate = new DateTime(mctsPregnantMother.getLmpDate());
            DateTime edd = lmpDate.plusDays(MctsConstants.EDD_CONSTANT);
            String eddDate = fmt.print(edd);
            updateTask.setMctsEdd(eddDate);
        }

        updateTask.setMctsHusbandName(husbandName);
        updateTask.setMctsHusbandNameEn(husbandNameEn);
        updateTask.setMctsFullname(mctsName);
        updateTask.setMctsFullnameEn(mctsNameEn);
        updateTask.setMctsAge(age);
        updateTask.setMctsDob(dob);
        updateTask.setMctsId(mctsId);
        updateTask.setMctsPhoneNumber(phone);
        updateTask.setAshaId(Integer.toString(workerId));

        return updateTask;
    }

    /**
     * Method to create Object Meta and return it.
     *
     * @param userId
     * @return
     */
    private Meta createMetaandReturn(String userId) {
        Meta meta = new Meta();
        meta.setXmlns(CommcareConstants.METAXMLNS);
        meta.setInstanceID(UUID.randomUUID().toString());
        meta.setTimeStart(new DateTime().toString());
        meta.setUserID(userId);

        return meta;
    }

    /**
     * Method to create Object createTask and return it
     *
     * @param mctsPregnantMother
     * @return
     * @throws BeneficiaryException
     */
    private CreateTask createTaskandReturn(
            MctsPregnantMother mctsPregnantMother, int workerId, String ownerId) {
        CreateTask createTask = new CreateTask();

        createTask.setCaseType(CommcareConstants.CASETYPE);
        createTask.setCaseName(mctsPregnantMother.getHindiName());
        LOGGER.debug("workerId : " + workerId);
        LOGGER.debug("ownerId : " + ownerId);
        createTask.setOwnerId(ownerId);

        return createTask;
    }
}
