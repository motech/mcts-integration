package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherCaseAuthorisedStatus;
import org.motechproject.mcts.care.common.lookup.MCTSPregnantMotherMatchStatus;
import org.motechproject.mcts.care.common.mds.dimension.MotherCase;
import org.motechproject.mcts.care.common.mds.measure.CaseAlreadyClosedForm;
import org.motechproject.mcts.care.common.mds.measure.DontKnowForm;
import org.motechproject.mcts.care.common.mds.measure.MapExistingForm;
import org.motechproject.mcts.care.common.mds.measure.MappingToApproveForm;
import org.motechproject.mcts.care.common.mds.measure.UnapprovedToDiscussForm;
import org.motechproject.mcts.care.common.mds.measure.UnmappedToReviewForm;
import org.motechproject.mcts.care.common.mds.model.MctsPregnantMother;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.repository.MctsRepository;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.motechproject.mcts.utils.CommcareNamespaceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormsProcessor {
    @Autowired
    private CareDataService careDataService;
    @Autowired
    private MCTSFormUpdateService mctsFormUpdateService;
    @Autowired
    MctsRepository careDataRepository;
    private MctsPregnantMother mctsPregnantMother;
    private MotherCase motherCase;

    private static final Logger LOGGER = LoggerFactory
            .getLogger("mcts-forms-processor");

    public void processForm(Map<String, String> motherForm)
            throws BeneficiaryException {
        String nameSpace = null;
        if (motherForm.containsKey("namespace")) {
            nameSpace = motherForm.get("namespace");
        } else {
            LOGGER.debug("No namespace found in this xml");
            return;
        }

        if (nameSpace.equals(CommcareNamespaceConstants.MAPPING_TO_APPROVE)) {
            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, "caseId", motherForm.get("caseId"));

            if (motherCase == null) {
                LOGGER.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get("pregnancyId")));
                return;
            }
            mctsPregnantMother = careDataService
                    .getMctsPregnantMotherFromCaseId(Integer
                            .toString(careDataRepository.getDetachedFieldId(motherCase)));

            // MotherCaseMctsAuthorizedStatus authorizeStatus =
            // getAuthorizedStatus(moth)

            mctsPregnantMother
                    .setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm
                            .get("authorized")));
            careDataService.saveOrUpdate(mctsPregnantMother);

            MappingToApproveForm mappingToApproveForm = new MappingToApproveForm();
            mappingToApproveForm.setApproved(motherForm.get("approved"));
            mappingToApproveForm.setConfirmMappingApproval(motherForm
                    .get("confirmMappingApproval"));
            mappingToApproveForm.setDateAuthorized(motherForm
                    .get("dateAuthorized"));
            mappingToApproveForm.setDateAuthorizedInt(motherForm
                    .get("dateAuthorizedInt"));
            mappingToApproveForm.setAppVersion(motherForm.get("appVersion"));
            mappingToApproveForm
                    .setDateModified(motherForm.get("dateModified"));
            mappingToApproveForm.setDeviceId(motherForm.get("deviceID"));
            mappingToApproveForm.setInstanceId(motherForm.get("instanceID"));
            mappingToApproveForm.setUserId(motherForm.get("userID"));
            mappingToApproveForm.setMotherCase(motherCase);
            mappingToApproveForm.setMctsPregnantMother(mctsPregnantMother);
            mappingToApproveForm.setNamespace(motherForm.get("namespace"));

            if (motherForm.containsKey("close")) {
                mappingToApproveForm.setClose(true);
            } else {
                mappingToApproveForm.setClose(false);
            }
            careDataService.saveOrUpdate(mappingToApproveForm);
        } else if (nameSpace.equals(CommcareNamespaceConstants.DONT_KNOW_CASE)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get("caseId"));

            mctsPregnantMother
                    .setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm
                            .get("mctsMatch")));

            careDataService.saveOrUpdate(mctsPregnantMother);

            DontKnowForm dontKnowForm = new DontKnowForm();
            dontKnowForm.setDontKnow(motherForm.get("dontKnow"));
            dontKnowForm.setKnown(motherForm.get("known"));
            dontKnowForm.setDateModified(motherForm.get("dateModified"));

            dontKnowForm.setMctsFullName(motherForm.get("mctsFullName"));
            dontKnowForm.setMctsHusbandName(motherForm.get("mctsHusbandName"));
            dontKnowForm.setAppVersion(motherForm.get("appVersion"));
            dontKnowForm.setDeviceId(motherForm.get("deviceId"));
            dontKnowForm.setInstanceId(motherForm.get("instanceId"));
            dontKnowForm.setNameSpace(motherForm.get("namespace"));

            dontKnowForm.setMctsPregnantMother(mctsPregnantMother);

            careDataService.saveOrUpdate(dontKnowForm);
        } else if (nameSpace
                .equals(CommcareNamespaceConstants.MAP_EXISTING_CASE)) {
            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, "caseId", motherForm.get("pregnancyId"));

            if (motherCase == null) {
                LOGGER.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get("pregnancyId")));
                return;
            }

            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get("caseId"));

            mctsPregnantMother
                    .setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm
                            .get("mctsMatch")));
            mctsPregnantMother
                    .setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm
                            .get("authorized")));
            mctsPregnantMother.setMotherCase(motherCase);

            careDataService.saveOrUpdate(mctsPregnantMother);

            MapExistingForm mapExistingForm = new MapExistingForm();
            mapExistingForm.setConfirmMapping(motherForm.get("confirmMapping"));
            mapExistingForm.setSuccess(motherForm.get("success"));
            mapExistingForm.setMctsId(motherForm.get("mctsId"));
            mapExistingForm.setFullMctsId(motherForm.get("fullMctsId"));
            mapExistingForm.setDateAuthorized(motherForm.get("dateAuthorized"));
            mapExistingForm.setDateAuthorizedInt(motherForm
                    .get("dateAuthorizedInt"));
            mapExistingForm.setDateModified(motherForm.get("dateModified"));

            mapExistingForm.setOwnerId(motherForm.get("ownerId"));
            mapExistingForm.setAppVersion(motherForm.get("appVersion"));
            mapExistingForm.setDeviceID(motherForm.get("deviceID"));
            mapExistingForm.setInstanceID(motherForm.get("instanceID"));
            mapExistingForm.setNameSpace(motherForm.get("namespace"));
            mapExistingForm.setUserID(motherForm.get("userID"));

            mapExistingForm.setMotherCase(motherCase);
            mapExistingForm.setMctsPregnantMother(mctsPregnantMother);

            careDataService.saveOrUpdate(mapExistingForm);
        } else if (nameSpace
                .equals(CommcareNamespaceConstants.UNAPPROVE_TO_DISCUSS)) {
            motherCase = careDataService.findEntityByField(MotherCase.class,
                    "caseId", motherForm.get("caseId"));
            mctsPregnantMother = careDataService
                    .getMctsPregnantMotherFromCaseId(Integer
                            .toString(careDataRepository.getDetachedFieldId(motherCase)));

            UnapprovedToDiscussForm unapprovedToDiscussForm = new UnapprovedToDiscussForm();
            unapprovedToDiscussForm.setAnmClose(motherForm.get("anmClose"));
            unapprovedToDiscussForm.setAshaCanFix(motherForm.get("ashaCanFix"));
            unapprovedToDiscussForm.setReasonDisapproved(motherForm
                    .get("reasonDisapproved"));
            unapprovedToDiscussForm.setShowReasonDisapproved(motherForm
                    .get("showReasonDisapproved"));
            unapprovedToDiscussForm.setBadMapping(motherForm.get("badMapping"));
            unapprovedToDiscussForm.setAshaNeedsToClose(motherForm
                    .get("ashaNeedsToClose"));
            unapprovedToDiscussForm.setConfirmAnmClose(motherForm
                    .get("confirmAnmClose"));
            unapprovedToDiscussForm.setDateModified(motherForm
                    .get("dateModified"));

            if (motherForm.containsKey("close")) {
                unapprovedToDiscussForm.setClose(true);
            } else {
                unapprovedToDiscussForm.setClose(false);
            }

            unapprovedToDiscussForm.setNamespace(motherForm.get("namespace"));
            unapprovedToDiscussForm.setDeviceID(motherForm.get("deviceID"));
            unapprovedToDiscussForm.setInstanceID(motherForm.get("instanceID"));
            unapprovedToDiscussForm.setAppVersion(motherForm.get("appVersion"));
            unapprovedToDiscussForm.setUserID(motherForm.get("userID"));

            unapprovedToDiscussForm.setMctsPregnantMother(mctsPregnantMother);
            unapprovedToDiscussForm.setMotherCase(motherCase);

            careDataService.saveOrUpdate(unapprovedToDiscussForm);
        } else if (nameSpace
                .equals(CommcareNamespaceConstants.UNMAPPED_TO_REVIEW)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get("caseId"));

            mctsPregnantMother
                    .setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm
                            .get("mctsMatch")));
            careDataService.saveOrUpdate(mctsPregnantMother);

            UnmappedToReviewForm unmappedToReviewForm = new UnmappedToReviewForm();
            unmappedToReviewForm.setKnown(motherForm.get("known"));
            unmappedToReviewForm.setDontKnow(motherForm.get("dontKnow"));
            unmappedToReviewForm.setPrevAshaId(motherForm.get("prevAshaId"));
            unmappedToReviewForm.setAshaName(motherForm.get("ashaName"));
            unmappedToReviewForm.setMctsHusbandName(motherForm
                    .get("mctsHusbandName"));
            unmappedToReviewForm
                    .setMctsFullName(motherForm.get("mctsFullName"));
            unmappedToReviewForm.setLangCode(motherForm.get("lang-code"));
            unmappedToReviewForm
                    .setDateModified(motherForm.get("dateModified"));
            unmappedToReviewForm.setAshaId(motherForm.get("ashaId"));

            if (motherForm.containsKey("close")) {
                unmappedToReviewForm.setClose(true);
            }
            unmappedToReviewForm.setAppVersion(motherForm.get("appVersion"));
            unmappedToReviewForm.setDeviceID(motherForm.get("deviceID"));
            unmappedToReviewForm.setInstanceID(motherForm.get("instanceID"));
            unmappedToReviewForm.setUserID(motherForm.get("userID"));
            unmappedToReviewForm.setNamespace(motherForm.get("namespace"));

            unmappedToReviewForm.setMctsPregnantMother(mctsPregnantMother);
            careDataService.saveOrUpdate(unmappedToReviewForm);
        } else if (nameSpace
                .equals(CommcareNamespaceConstants.CASE_ALREADY_CLOSED)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get("caseId"));
            mctsPregnantMother.setHhNumber(motherForm.get("hhNumber"));
            mctsPregnantMother.setFamilyNumber(motherForm.get("familyNumber"));
            mctsPregnantMother
                    .setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm
                            .get("mctsMatch")));
            careDataService.saveOrUpdate(mctsPregnantMother);

            mctsFormUpdateService
                    .updateMctsPregnantMotherForm(careDataRepository.getDetachedFieldId(mctsPregnantMother));

            CaseAlreadyClosedForm caseAlreadyClosedForm = new CaseAlreadyClosedForm();
            caseAlreadyClosedForm.setPermanentMove(motherForm
                    .get("permanentMove"));
            caseAlreadyClosedForm.setDateMoveKnown(motherForm
                    .get("permanentMove"));
            caseAlreadyClosedForm.setMoveDate(motherForm.get("moveDate"));
            caseAlreadyClosedForm.setHhNumber(motherForm.get("hhNumber"));
            caseAlreadyClosedForm.setFamilyNumber(motherForm
                    .get("familyNumber"));
            caseAlreadyClosedForm.setSuccessClose(motherForm
                    .get("successClose"));
            caseAlreadyClosedForm.setMctsFullName(motherForm
                    .get("mctsFullName"));
            caseAlreadyClosedForm.setMctsHusbandName(motherForm
                    .get("mctsHusbandName"));
            caseAlreadyClosedForm.setCloseReason(motherForm.get("closeReason"));
            caseAlreadyClosedForm.setDateModified(motherForm
                    .get("dateModified"));

            caseAlreadyClosedForm.setMctsPregnantMother(mctsPregnantMother);

            if (motherForm.containsKey("close")) {
                caseAlreadyClosedForm.setClose(true);
            } else {
                caseAlreadyClosedForm.setClose(false);
            }
            caseAlreadyClosedForm.setNamespace(motherForm.get("namespace"));
            caseAlreadyClosedForm.setUserID(motherForm.get("userID"));
            caseAlreadyClosedForm.setInstanceID(motherForm.get("instanceID"));
            caseAlreadyClosedForm.setDeviceID(motherForm.get("deviceID"));
            caseAlreadyClosedForm.setAppVersion(motherForm.get("appVersion"));

            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get("caseId"));
            motherCase = mctsPregnantMother.getMotherCase();
            caseAlreadyClosedForm.setMotherCase(motherCase);
            careDataService.saveOrUpdate(caseAlreadyClosedForm);
        } else if (nameSpace.equals(CommcareNamespaceConstants.CREATE_NEW_CASE)) {
            mctsPregnantMother = (MctsPregnantMother) careDataService
                    .findEntityByField(MctsPregnantMother.class,
                            "mctsPersonaCaseUId", motherForm.get("caseId"));

            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, "caseId", motherForm.get("pregnancyId"));
            if (motherCase == null) {
                LOGGER.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get("pregnancyId")));
                return;
            }
            mctsPregnantMother
                    .setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm
                            .get("mctsMatch")));
            mctsPregnantMother.setMotherCase(motherCase);

            careDataService.saveOrUpdate(mctsPregnantMother);
        }

    }

    private MCTSPregnantMotherMatchStatus getMatchStatus(String status) {
        if ("closed".equals(status)) {
            return MCTSPregnantMotherMatchStatus.CLOSED;
        } else if ("yes".equals(status)) {
            return MCTSPregnantMotherMatchStatus.YES;
        } else if ("no".equals(status)) {
            return MCTSPregnantMotherMatchStatus.NO;
        } else if ("unknown".equals(status)) {
            return MCTSPregnantMotherMatchStatus.UNKNOWN;
        } else if ("archive".equals(status)) {
            return MCTSPregnantMotherMatchStatus.ARCHIVE;
        } else if ("blank".equals(status)) {
            return MCTSPregnantMotherMatchStatus.BLANK;
        } else {
            return null;
        }
    }

    private MCTSPregnantMotherCaseAuthorisedStatus getAuthorizedStatus(
            String status) {
        if ("pending".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.PENDING;
        } else if ("approved".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.APPROVED;
        } else if ("denied".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.DENIED;
        } else if ("blank".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.BLANK;
        } else {
            return null;
        }
    }
}
