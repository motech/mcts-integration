package org.motechproject.mcts.integration.commcare;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.MctsRepository;
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
 * Class to create xml for update cases.
 *
 * @author aman
 *
 */
@Service
public class UpdateCaseXmlService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UpdateCaseXmlService.class);

    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    private MctsRepository careDataRepository;

    @Autowired
    private MCTSHttpClientService mCTSHttpClientService;

    public MctsRepository getCareDataRepository() {
        return careDataRepository;
    }

    public void setCareDataRepository(MctsRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public void updateXml(Integer mctsPregnantMotherId) {
        MctsPregnantMother mctsPregnantMother = careDataRepository
                .getMotherFromPrimaryId(mctsPregnantMotherId);
        String caseGuid = mctsPregnantMother.getMctsPersonaCaseUId();
        if (caseGuid != null && !StringUtils.isEmpty(caseGuid)) {
            updateXml(mctsPregnantMother);
        }
    }

    public void updateXml(MctsPregnantMother mctsPregnantMother) {
        UpdateData data = new UpdateData();
        String userId = propertyReader.getUserIdforCommcare();
        Meta meta = createMetaandReturn(userId);
        meta.setTimeEnd(new DateTime().toString());
        data.setMeta(meta);
        int workerId;

        if (mctsPregnantMother.getMctsHealthworkerByAshaId() != null) {
            workerId = mctsPregnantMother.getMctsHealthworkerByAshaId()
                    .getHealthworkerId();
        } else {
            workerId = -1;
        }
        Case caseTask = createCaseForBeneficiary(mctsPregnantMother, userId,
                workerId);
        if ((caseTask.getUpdateTask().getMctsFullname() != null)
                && (caseTask.getUpdateTask().getMctsHusbandName() != null)
                && (caseTask.getUpdateTask().getMctsHusbandNameEn() != null)
                && (caseTask.getUpdateTask().getMctsFullnameEn() != null)) {
            data.setCaseTask(caseTask);
        }

        data.setXmlns(CommcareConstants.UPDATEDATAXMLNS);
        String returnvalue = ObjectToXMLConverter.converObjectToXml(data,
                UpdateData.class);
        LOGGER.debug("returned : " + returnvalue);
        // POST the xml to the url
        HttpStatus status = mCTSHttpClientService.syncToCommcareUpdate(data);

        // Accepted status code is 2xx
        if (status.value() / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE_2XX) {
            LOGGER.debug("valid response received");
        }

    }

    private Case createCaseForBeneficiary(
            MctsPregnantMother mctsPregnantMother, String userId, int workerId) {

        String ownerId = mctsPregnantMother.getOwnerId();
        String caseId = mctsPregnantMother.getMctsPersonaCaseUId();

        Case caseTask = new Case();
        DateTime date = new DateTime();

        String dateModified = date.toString();

        UpdateTask updatedTask = updateTaskandReturn(mctsPregnantMother,
                workerId, ownerId);
        caseTask.setUpdateTask(updatedTask);
        caseTask.setXmlns(CommcareConstants.XMLNS);
        caseTask.setDateModified(dateModified);
        caseTask.setCaseId(caseId);
        caseTask.setUserId(userId);

        return caseTask;
    }

    /**
     * Mehtod to create Meta Object and return it.
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
     * Method to create UpdateTask Object and return it.
     *
     * @param mctsPregnantMother
     * @param userId
     * @param workerId
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
        String phone = mctsPregnantMother.getMobileNo();

        DateTime birthDate = new DateTime(mctsPregnantMother.getBirthDate());
        Date birth = mctsPregnantMother.getBirthDate();
        DateTime date = new DateTime();
        String age = "";
        String dob = "";
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        if (mctsPregnantMother.getLmpDate() != null) {
            DateTime lmpDate = new DateTime(mctsPregnantMother.getLmpDate());
            DateTime edd = lmpDate.plusDays(MctsConstants.EDD_CONSTANT);
            String eddDate = fmt.print(edd);
            updateTask.setMctsEdd(eddDate);
        }

        if (birth != null) {
            dob = fmt.print(birthDate);
            age = Integer.toString(Days.daysBetween(
                    birthDate.withTimeAtStartOfDay(),
                    date.withTimeAtStartOfDay()).getDays() / MctsConstants.NUMBER_OF_DAYS_IN_YEAR);
        }

        updateTask.setCaseName(mctsPregnantMother.getName());
        updateTask.setCaseType(CommcareConstants.CASETYPE);
        updateTask.setOwnerId(ownerId);
        updateTask.setMctsHusbandName(husbandName);
        updateTask.setMctsHusbandNameEn(husbandNameEn);
        updateTask.setMctsFullname(mctsName);
        updateTask.setMctsFullnameEn(mctsNameEn);
        updateTask.setMctsAge(age);
        updateTask.setMctsDob(dob);
        updateTask.setMctsPhoneNumber(phone);

        return updateTask;
    }

}
