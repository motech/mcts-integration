package org.motechproject.mcts.integration.commcare;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherMatchStatus;
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

@Service
public class CloseCaseXmlService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CloseCaseXmlService.class);

    
    @Autowired
    private MctsRepository careDataRepository;
    
    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    private MCTSHttpClientService mCTSHttpClientService;

    public void createCloseCaseXml() {
        List<MctsPregnantMother> mctsPregnantMother = careDataRepository
                .getMctsPregnantMotherForClosedCases();
        LOGGER.debug("size : " + mctsPregnantMother.size());
        CloseData data = createCloseDataAndReturn(mctsPregnantMother);
        String returnvalue = ObjectToXMLConverter.converObjectToXml(data,
                CloseData.class);
        LOGGER.debug("returned : " + returnvalue);
        HttpStatus status = mCTSHttpClientService.syncToCloseCommcare(data);
        if (status.value() / MctsConstants.STATUS_DIVISOR == MctsConstants.STATUS_VALUE_2XX) {
            updateCaseStatus(data);
        }

    }

    private void updateCaseStatus(CloseData data) {
        List<Case> cases = data.getCases();
        for (Case curr : cases) {
            MctsPregnantMother mother = careDataRepository
                    .getMotherFromPrimaryId(curr.getMctsPregnantMotherId());
            mother.setmCTSPregnantMotherMatchStatus(MCTSPregnantMotherMatchStatus.ARCHIVE);
            mother.setDateOpened(new DateTime().toString());
            careDataRepository.saveOrUpdate(mother);

        }

    }

    private CloseData createCloseDataAndReturn(
            List<MctsPregnantMother> mctsPregnantMother) {
        CloseData data = new CloseData();
        List<Case> cases = new ArrayList<Case>();
        data.setXmlns(CommcareConstants.DATAXMLNS);
        String userId = propertyReader.getUserIdforCommcare();
        Meta meta = createMetaandReturn(userId);
        meta.setTimeEnd(new DateTime().toString());
        data.setMeta(meta);
        for (int i = 0; i < mctsPregnantMother.size(); i++) {
            mctsPregnantMother.get(i).setmCTSPregnantMotherMatchStatus(
                    MCTSPregnantMotherMatchStatus.ARCHIVE);
            Case caseTask = createCaseForBeneficiary(mctsPregnantMother.get(i),
                    userId);
            cases.add(caseTask);
        }
        data.setCases(cases);
        return data;
    }

    private Case createCaseForBeneficiary(
            MctsPregnantMother mctsPregnantMother, String userId) {
        String caseId = mctsPregnantMother.getMctsPersonaCaseUId();

        Case caseTask = new Case();
        DateTime date = new DateTime();

        String dateModified = date.toString();
        caseTask.setXmlns(CommcareConstants.XMLNS);
        caseTask.setDateModified(dateModified);
        caseTask.setCaseId(caseId);
        caseTask.setUserId(userId);
        caseTask.setMctsPregnantMotherId(careDataRepository.getDetachedFieldId(mctsPregnantMother));

        return caseTask;
    }

    private Meta createMetaandReturn(String userId) {
        Meta meta = new Meta();
        meta.setXmlns(CommcareConstants.METAXMLNS);
        meta.setInstanceID(UUID.randomUUID().toString());
        meta.setTimeStart(new DateTime().toString());
        meta.setUserID(userId);

        return meta;
    }

}
