package org.motechproject.mcts.integration.processor;

import java.util.Map;

import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.CaseAlreadyClosedForm;
import org.motechproject.mcts.integration.hibernate.model.DontKnowForm;
import org.motechproject.mcts.integration.hibernate.model.MapExistingForm;
import org.motechproject.mcts.integration.hibernate.model.MappingToApproveForm;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.hibernate.model.MotherCase;
import org.motechproject.mcts.integration.hibernate.model.UnapprovedToDiscussForm;
import org.motechproject.mcts.integration.hibernate.model.UnmappedToReviewForm;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.motechproject.mcts.lookup.MCTSPregnantMotherCaseAuthorisedStatus;
import org.motechproject.mcts.lookup.MCTSPregnantMotherMatchStatus;
import org.motechproject.mcts.utils.CommcareFieldsConstants;
import org.motechproject.mcts.utils.CommcareNamespaceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormsProcessor {
    @Autowired
    CareDataService careDataService;
    @Autowired
    MCTSFormUpdateService mctsFormUpdateService;
    MctsPregnantMother mctsPregnantMother = null;
    MotherCase motherCase = null;

    private static final Logger logger = LoggerFactory
            .getLogger("mcts-forms-processor");

    public void processForm(Map<String, String> motherForm)
            throws BeneficiaryException {
        String nameSpace = null;
        if (motherForm.containsKey(CommcareFieldsConstants.NAMESPACE)) {
            nameSpace = motherForm.get(CommcareFieldsConstants.NAMESPACE);
        } else {
            logger.debug("No namespace found in this xml");
            return;
        }

        if (nameSpace.equals(CommcareNamespaceConstants.MAPPING_TO_APPROVE)) {
            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, CommcareFieldsConstants.CASE_ID, motherForm.get(CommcareFieldsConstants.CASE_ID));

            if (motherCase == null) {
                logger.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get(CommcareFieldsConstants.PREGNANCY_ID)));
                return;
            }
            mctsPregnantMother = careDataService
                    .getMctsPregnantMotherFromCaseId(Integer
                            .toString(motherCase.getId()));
            if(motherForm.containsKey(CommcareFieldsConstants.AUTHORIZED)) {
            mctsPregnantMother
                    .setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm.get(CommcareFieldsConstants.AUTHORIZED)));
            }
            careDataService.saveOrUpdate(mctsPregnantMother);

            MappingToApproveForm mappingToApproveForm = new MappingToApproveForm();
            mappingToApproveForm.setApproved(motherForm.get(CommcareFieldsConstants.APPROVED));
            mappingToApproveForm.setConfirmMappingApproval(motherForm
                    .get(CommcareFieldsConstants.CONFIRM_MAPPING_APPROVAL));
            mappingToApproveForm.setDateAuthorized(motherForm
                    .get(CommcareFieldsConstants.DATE_AUTHORIZED));
            mappingToApproveForm.setDateAuthorizedInt(motherForm
                    .get(CommcareFieldsConstants.DATE_AUTHORIZED_INT));
            mappingToApproveForm.setConfirmNewCaseApproval(motherForm.get(CommcareFieldsConstants.CONFIRM_NEW_CASE_APPROVAL));
            mappingToApproveForm.setDisapproved(motherForm.get(CommcareFieldsConstants.DISAPPROVED));
            mappingToApproveForm.setReasonDisapproved(motherForm.get(CommcareFieldsConstants.REASON_DISAPPROVED));
            mappingToApproveForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));
            mappingToApproveForm
                    .setDateModified(motherForm.get(CommcareFieldsConstants.DATE_MODIFIED));
            mappingToApproveForm.setDeviceId(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            mappingToApproveForm.setInstanceId(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            mappingToApproveForm.setUserId(motherForm.get(CommcareFieldsConstants.USER_ID));
            mappingToApproveForm.setMotherCase(motherCase);
            mappingToApproveForm.setMctsPregnantMother(mctsPregnantMother);
            mappingToApproveForm.setNamespace(motherForm.get(CommcareFieldsConstants.NAMESPACE));

            if (motherForm.containsKey(CommcareFieldsConstants.CLOSE)) {
                mappingToApproveForm.setClose(true);
            }

            else {
                mappingToApproveForm.setClose(false);
            }

            careDataService.saveOrUpdate(mappingToApproveForm);
        }

        else if (nameSpace.equals(CommcareNamespaceConstants.DONT_KNOW_CASE)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get(CommcareFieldsConstants.CASE_ID));
            if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
            }
            careDataService.saveOrUpdate(mctsPregnantMother);

            DontKnowForm dontKnowForm = new DontKnowForm();
            dontKnowForm.setDontKnow(motherForm.get(CommcareFieldsConstants.DONT_KNOW));
            dontKnowForm.setKnown(motherForm.get(CommcareFieldsConstants.KNOWN));
            dontKnowForm.setUnconfirmed(motherForm.get(CommcareFieldsConstants.UNCONFIRMED));
            dontKnowForm.setDateModified(motherForm.get(CommcareFieldsConstants.DATE_MODIFIED));
            dontKnowForm.setMctsFullName(motherForm.get(CommcareFieldsConstants.MCTS_FULL_NAME));
            dontKnowForm.setMctsHusbandName(motherForm.get(CommcareFieldsConstants.MCTS_HUSBAND_NAME));
            dontKnowForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));
            dontKnowForm.setDeviceId(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            dontKnowForm.setInstanceId(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            dontKnowForm.setUserId(motherForm.get(CommcareFieldsConstants.USER_ID));
            dontKnowForm.setNameSpace(motherForm.get(CommcareFieldsConstants.NAMESPACE));

            dontKnowForm.setMctsPregnantMother(mctsPregnantMother);

            careDataService.saveOrUpdate(dontKnowForm);
        }

        else if (nameSpace.equals(CommcareNamespaceConstants.MAP_EXISTING_CASE)) {
            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, CommcareFieldsConstants.CASE_ID, motherForm.get(CommcareFieldsConstants.PREGNANCY_ID));

            if (motherCase == null) {
                logger.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get(CommcareFieldsConstants.PREGNANCY_ID)));
                return;
            }

            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get(CommcareFieldsConstants.CASE_ID));
          
            if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
            }
            if(motherForm.containsKey(CommcareFieldsConstants.AUTHORIZED)) {
            mctsPregnantMother
            .setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm.get(CommcareFieldsConstants.AUTHORIZED)));
            }
            mctsPregnantMother.setMotherCase(motherCase);

            careDataService.saveOrUpdate(mctsPregnantMother);

            MapExistingForm mapExistingForm = new MapExistingForm();
            mapExistingForm.setConfirmMapping(motherForm.get(CommcareFieldsConstants.CONFIRM_MAPPING));
            mapExistingForm.setSuccess(motherForm.get(CommcareFieldsConstants.SUCCESS));
            mapExistingForm.setNotMapped(motherForm.get(CommcareFieldsConstants.NOT_MAPPED));
            mapExistingForm.setMctsId(motherForm.get(CommcareFieldsConstants.MCTS_ID));
            mapExistingForm.setFullMctsId(motherForm.get(CommcareFieldsConstants.FULL_MCTS_ID));
            mapExistingForm.setDateAuthorized(motherForm.get(CommcareFieldsConstants.DATE_AUTHORIZED));
            mapExistingForm.setDateAuthorizedInt(motherForm
                    .get(CommcareFieldsConstants.DATE_AUTHORIZED_INT));
            mapExistingForm.setDateModified(motherForm.get(CommcareFieldsConstants.DATE_MODIFIED));

            mapExistingForm.setOwnerId(motherForm.get(CommcareFieldsConstants.OWNER_ID));
            mapExistingForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));
            mapExistingForm.setDeviceID(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            mapExistingForm.setInstanceID(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            mapExistingForm.setNameSpace(motherForm.get(CommcareFieldsConstants.NAMESPACE));
            mapExistingForm.setUserID(motherForm.get(CommcareFieldsConstants.USER_ID));

            mapExistingForm.setMotherCase(motherCase);
            mapExistingForm.setMctsPregnantMother(mctsPregnantMother);

            careDataService.saveOrUpdate(mapExistingForm);
        }

        else if (nameSpace
                .equals(CommcareNamespaceConstants.UNAPPROVE_TO_DISCUSS)) {
            motherCase = careDataService.findEntityByField(MotherCase.class,
                    CommcareFieldsConstants.CASE_ID, motherForm.get(CommcareFieldsConstants.CASE_ID));
            mctsPregnantMother = careDataService
                    .getMctsPregnantMotherFromCaseId(Integer
                            .toString(motherCase.getId()));
            if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
            }
            if(motherForm.containsKey(CommcareFieldsConstants.AUTHORIZED)) {
            mctsPregnantMother.setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm.get(CommcareFieldsConstants.AUTHORIZED)));
            }
            careDataService.saveOrUpdate(mctsPregnantMother);
            
            UnapprovedToDiscussForm unapprovedToDiscussForm = new UnapprovedToDiscussForm();
            unapprovedToDiscussForm.setAnmClose(motherForm.get(CommcareFieldsConstants.ANM_CLOSE));
            unapprovedToDiscussForm.setAshaCanFix(motherForm.get(CommcareFieldsConstants.ASHA_CAN_FIX));
            unapprovedToDiscussForm.setMctsId(motherForm.get(CommcareFieldsConstants.MCTS_ID));
            unapprovedToDiscussForm.setFullMctsId(motherForm.get(CommcareFieldsConstants.FULL_MCTS_ID));
            unapprovedToDiscussForm.setReasonDisapproved(motherForm
                    .get(CommcareFieldsConstants.REASON_DISAPPROVED));
            unapprovedToDiscussForm.setShowReasonDisapproved(motherForm
                    .get(CommcareFieldsConstants.SHOW_REASON_DISAPPROVED));
            unapprovedToDiscussForm.setBadMapping(motherForm.get(CommcareFieldsConstants.BAD_MAPPING));
            unapprovedToDiscussForm.setAshaNeedsToClose(motherForm
                    .get(CommcareFieldsConstants.ASHA_NEEDS_TO_CLOSE));
            unapprovedToDiscussForm.setConfirmAnmClose(motherForm
                    .get(CommcareFieldsConstants.CONFIRM_ANM_CLOSE));
            unapprovedToDiscussForm.setDateAuthorized(motherForm.get(CommcareFieldsConstants.DATE_AUTHORIZED));
            unapprovedToDiscussForm.setDateAuthorizedInt(motherForm.get(CommcareFieldsConstants.DATE_AUTHORIZED_INT));
            unapprovedToDiscussForm.setPromptFix(motherForm.get(CommcareFieldsConstants.PROMPT_FIX));
            unapprovedToDiscussForm.setAshaFixed(motherForm.get(CommcareFieldsConstants.ASHA_FIXED));
            unapprovedToDiscussForm.setNeedToRemap(motherForm.get(CommcareFieldsConstants.NEED_TO_REMAP));
            unapprovedToDiscussForm.setApprovedFix(motherForm.get(CommcareFieldsConstants.APPROVED_FIX));
            unapprovedToDiscussForm.setUnconfirmed(motherForm.get(CommcareFieldsConstants.UNCONFIRMED));
            unapprovedToDiscussForm.setAshaNeedsToClose(motherForm.get(CommcareFieldsConstants.ASHA_NEEDS_TO_CLOSE));
            unapprovedToDiscussForm.setDateModified(motherForm
                    .get(CommcareFieldsConstants.DATE_MODIFIED));

            if (motherForm.containsKey(CommcareFieldsConstants.CLOSE)) {
                unapprovedToDiscussForm.setClose(true);
            } else {
                unapprovedToDiscussForm.setClose(false);
            }

            unapprovedToDiscussForm.setNamespace(motherForm.get(CommcareFieldsConstants.NAMESPACE));
            unapprovedToDiscussForm.setDeviceID(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            unapprovedToDiscussForm.setInstanceID(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            unapprovedToDiscussForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));
            unapprovedToDiscussForm.setUserID(motherForm.get(CommcareFieldsConstants.USER_ID));

            unapprovedToDiscussForm.setMctsPregnantMother(mctsPregnantMother);
            unapprovedToDiscussForm.setMotherCase(motherCase);

            careDataService.saveOrUpdate(unapprovedToDiscussForm);
        }

        else if (nameSpace
                .equals(CommcareNamespaceConstants.UNMAPPED_TO_REVIEW)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get(CommcareFieldsConstants.CASE_ID));
            if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
            }
            careDataService.saveOrUpdate(mctsPregnantMother);

            UnmappedToReviewForm unmappedToReviewForm = new UnmappedToReviewForm();
            unmappedToReviewForm.setKnown(motherForm.get(CommcareFieldsConstants.KNOWN));
            unmappedToReviewForm.setDontKnow(motherForm.get(CommcareFieldsConstants.DONT_KNOW));
            unmappedToReviewForm.setPrevAshaId(motherForm.get(CommcareFieldsConstants.PREV_ASHA_ID));
            unmappedToReviewForm.setAshaName(motherForm.get(CommcareFieldsConstants.ASHA_NAME));
            unmappedToReviewForm.setMctsHusbandName(motherForm
                    .get(CommcareFieldsConstants.MCTS_HUSBAND_NAME));
            unmappedToReviewForm
                    .setMctsFullName(motherForm.get(CommcareFieldsConstants.MCTS_FULL_NAME));
            unmappedToReviewForm.setLangCode(motherForm.get(CommcareFieldsConstants.LANG_CODE));
            unmappedToReviewForm.setIsCorrectAsha(motherForm.get(CommcareFieldsConstants.IS_CORRECT_ASHA));
            unmappedToReviewForm.setNoAshaExisting(motherForm.get(CommcareFieldsConstants.NO_ASHA_EXISTING));
            unmappedToReviewForm.setNoAsha(motherForm.get(CommcareFieldsConstants.NO_ASHA));
            unmappedToReviewForm.setNewAsha(motherForm.get(CommcareFieldsConstants.NEW_ASHA));
            unmappedToReviewForm.setNewAssignment(motherForm.get(CommcareFieldsConstants.NEW_ASSIGNMENT));
            unmappedToReviewForm.setSameAssignment(motherForm.get(CommcareFieldsConstants.SAME_ASSIGNMENT));
            unmappedToReviewForm.setNewAshaName(motherForm.get(CommcareFieldsConstants.NEW_ASHA_NAME));
            unmappedToReviewForm
                    .setDateModified(motherForm.get(CommcareFieldsConstants.DATE_MODIFIED));
            unmappedToReviewForm.setAshaId(motherForm.get(CommcareFieldsConstants.ASHA_ID));

            if (motherForm.containsKey(CommcareFieldsConstants.CLOSE)) {
                unmappedToReviewForm.setClose(true);
            }
            unmappedToReviewForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));
            unmappedToReviewForm.setDeviceID(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            unmappedToReviewForm.setInstanceID(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            unmappedToReviewForm.setUserID(motherForm.get(CommcareFieldsConstants.USER_ID));
            unmappedToReviewForm.setNamespace(motherForm.get(CommcareFieldsConstants.NAMESPACE));

            unmappedToReviewForm.setMctsPregnantMother(mctsPregnantMother);
            careDataService.saveOrUpdate(unmappedToReviewForm);
        }

        else if (nameSpace
                .equals(CommcareNamespaceConstants.CASE_ALREADY_CLOSED)) {
            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, CommcareFieldsConstants.MCTS_PERSONA_CASE_UID,
                    motherForm.get(CommcareFieldsConstants.CASE_ID));
            mctsPregnantMother.setHhNumber(motherForm.get(CommcareFieldsConstants.HH_NUMBER));
            mctsPregnantMother.setFamilyNumber(motherForm.get(CommcareFieldsConstants.FAMILY_NUMBER));
            if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
            }
            careDataService.saveOrUpdate(mctsPregnantMother);

            mctsFormUpdateService
                    .updateMctsPregnantMotherForm(mctsPregnantMother.getId());

            CaseAlreadyClosedForm caseAlreadyClosedForm = new CaseAlreadyClosedForm();

            caseAlreadyClosedForm.setHhNumber(motherForm.get(CommcareFieldsConstants.HH_NUMBER));
            caseAlreadyClosedForm.setFamilyNumber(motherForm
                    .get(CommcareFieldsConstants.FAMILY_NUMBER));
            caseAlreadyClosedForm.setPermanentMove(motherForm
                    .get(CommcareFieldsConstants.PERMANENT_MOVE));
            caseAlreadyClosedForm.setDateMoveKnown(motherForm.get(CommcareFieldsConstants.DATE_MOVE_KNOWN));
            caseAlreadyClosedForm.setMoveDate(motherForm.get(CommcareFieldsConstants.MOVE_DATE));

            caseAlreadyClosedForm.setSuccessClose(motherForm
                    .get(CommcareFieldsConstants.SUCCESS_CLOSE));
            caseAlreadyClosedForm.setSuccessStillOpen(motherForm.get(CommcareFieldsConstants.SUCCESS_STILL_OPEN));
            caseAlreadyClosedForm.setDied(motherForm.get(CommcareFieldsConstants.DIED));
            caseAlreadyClosedForm.setDateDeathKnown(motherForm.get(CommcareFieldsConstants.DATE_DEATH_KNOWN));
            caseAlreadyClosedForm.setDateDeath(motherForm.get(CommcareFieldsConstants.DATE_DEATH));
            caseAlreadyClosedForm.setSiteDeath(motherForm.get(CommcareFieldsConstants.SITE_DEATH));
            caseAlreadyClosedForm.setDiedVillage(motherForm.get(CommcareFieldsConstants.DIED_VILLAGE));
            caseAlreadyClosedForm.setPlaceDeath(motherForm.get(CommcareFieldsConstants.PLACE_DEATH));
            caseAlreadyClosedForm.setAbortion(motherForm.get(CommcareFieldsConstants.ABORTION));
            caseAlreadyClosedForm.setAbortionType(motherForm.get(CommcareFieldsConstants.ABORTION_TYPE));
            caseAlreadyClosedForm.setDateAbortionKnown(motherForm.get(CommcareFieldsConstants.DATE_ABORTION_KNOWN));
            caseAlreadyClosedForm.setDateAborted(motherForm.get(CommcareFieldsConstants.DATE_ABORTED));
            caseAlreadyClosedForm.setMctsFullName(motherForm
                    .get(CommcareFieldsConstants.MCTS_FULL_NAME));
            caseAlreadyClosedForm.setMctsHusbandName(motherForm
                    .get(CommcareFieldsConstants.MCTS_HUSBAND_NAME));
            caseAlreadyClosedForm.setCloseReason(motherForm.get(CommcareFieldsConstants.CLOSE_REASON));
            caseAlreadyClosedForm.setDateModified(motherForm
                    .get(CommcareFieldsConstants.DATE_MODIFIED));

            caseAlreadyClosedForm.setMctsPregnantMother(mctsPregnantMother);

            if (motherForm.containsKey(CommcareFieldsConstants.CLOSE)) {
                caseAlreadyClosedForm.setClose(true);
            } else {
                caseAlreadyClosedForm.setClose(false);
            }
            caseAlreadyClosedForm.setNamespace(motherForm.get(CommcareFieldsConstants.NAMESPACE));
            caseAlreadyClosedForm.setUserID(motherForm.get(CommcareFieldsConstants.USER_ID));
            caseAlreadyClosedForm.setInstanceID(motherForm.get(CommcareFieldsConstants.INSTANCE_ID));
            caseAlreadyClosedForm.setDeviceID(motherForm.get(CommcareFieldsConstants.DEVICE_ID));
            caseAlreadyClosedForm.setAppVersion(motherForm.get(CommcareFieldsConstants.APP_VERSION));

            mctsPregnantMother = careDataService.findEntityByField(
                    MctsPregnantMother.class, "mctsPersonaCaseUId",
                    motherForm.get(CommcareFieldsConstants.CASE_ID));
            motherCase = mctsPregnantMother.getMotherCase();
            caseAlreadyClosedForm.setMotherCase(motherCase);
            careDataService.saveOrUpdate(caseAlreadyClosedForm);
        }

        else if (nameSpace.equals(CommcareNamespaceConstants.CREATE_NEW_CASE)) {
            mctsPregnantMother = (MctsPregnantMother) careDataService
                    .findEntityByField(MctsPregnantMother.class,
                            CommcareFieldsConstants.MCTS_PERSONA_CASE_UID, motherForm.get(CommcareFieldsConstants.CASE_ID));

            MotherCase motherCase = careDataService.findEntityByField(
                    MotherCase.class, CommcareFieldsConstants.CASE_ID, motherForm.get(CommcareFieldsConstants.PREGNANCY_ID));
            if (motherCase == null) {
                logger.error(String
                        .format("Received case doesn't have Mother case with with case Id = %s",
                                motherForm.get(CommcareFieldsConstants.PREGNANCY_ID)));
                return;
            }
            if(mctsPregnantMother != null) {
                if(motherForm.containsKey(CommcareFieldsConstants.MCTS_MATCH)) {
            mctsPregnantMother.setmCTSPregnantMotherMatchStatus(getMatchStatus(motherForm.get(CommcareFieldsConstants.MCTS_MATCH)));
                }
            mctsPregnantMother.setMotherCase(motherCase);
            careDataService.saveOrUpdate(mctsPregnantMother);
            }
        } else if(nameSpace.equals(CommcareNamespaceConstants.REGISTRATION)) {
            motherCase = careDataService.findEntityByField(
                    MotherCase.class, CommcareFieldsConstants.CASE_ID, motherForm.get(CommcareFieldsConstants.CASE_ID));
            mctsPregnantMother = careDataService.getMctsPregnantMotherFromCaseId(Integer
                    .toString(motherCase.getId()));
            if(mctsPregnantMother != null) {
                if(motherForm.containsKey(CommcareFieldsConstants.AUTHORIZED)) {
                mctsPregnantMother.setmCTSPregnantMotherCaseAuthorisedStatus(getAuthorizedStatus(motherForm.get(CommcareFieldsConstants.AUTHORIZED)));
                }
                careDataService.saveOrUpdate(mctsPregnantMother);
            }
        }
        

    }
    
    private MCTSPregnantMotherMatchStatus getMatchStatus(String status) {
        if("closed".equals(status)) {
            return MCTSPregnantMotherMatchStatus.CLOSED;
        }
        else if("yes".equals(status)) {
            return MCTSPregnantMotherMatchStatus.YES;
        }
        else if("no".equals(status)) {
            return MCTSPregnantMotherMatchStatus.NO;
        }
        else if("unknown".equals(status)) {
            return MCTSPregnantMotherMatchStatus.UNKNOWN;
        }
        else if("archive".equals(status)) {
            return MCTSPregnantMotherMatchStatus.ARCHIVE;
        }
        else {
            return MCTSPregnantMotherMatchStatus.BLANK;
        }
    }
    
    
    private MCTSPregnantMotherCaseAuthorisedStatus getAuthorizedStatus(String status) {
        if("pending".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.PENDING;
        }
        else if("approved".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.APPROVED;
        }
        else if("denied".equals(status)) {
            return MCTSPregnantMotherCaseAuthorisedStatus.DENIED;
        }
        else {
            return MCTSPregnantMotherCaseAuthorisedStatus.BLANK;
        }
    }
}
