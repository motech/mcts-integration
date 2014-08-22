package org.motechproject.mcts.integration.mds.model;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class CaseAlreadyClosedForm {
    private String permanentMove;
    private String dateMoveKnown;
    private String moveDate;
    private String hhNumber;
    private String familyNumber;
    private String successClose;
    private String mctsFullName;
    private String mctsHusbandName;
    private String closeReason;
    private String dateModified;
    private boolean close;
    private String namespace;
    private String userID;
    private String instanceID;
    private String deviceID;
    private String appVersion;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;

   

    @Field
    public String getPermanentMove() {
        return permanentMove;
    }

    public void setPermanentMove(String permanentMove) {
        this.permanentMove = permanentMove;
    }

    @Field
    public String getDateMoveKnown() {
        return dateMoveKnown;
    }

    public void setDateMoveKnown(String dateMoveKnown) {
        this.dateMoveKnown = dateMoveKnown;
    }

    @Field
    public String getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(String moveDate) {
        this.moveDate = moveDate;
    }

    @Field
    public String getHhNumber() {
        return hhNumber;
    }

    public void setHhNumber(String hhNumber) {
        this.hhNumber = hhNumber;
    }

    @Field
    public String getFamilyNumber() {
        return familyNumber;
    }

    public void setFamilyNumber(String familyNumber) {
        this.familyNumber = familyNumber;
    }

    @Field
    public String getSuccessClose() {
        return successClose;
    }

    public void setSuccessClose(String successClose) {
        this.successClose = successClose;
    }

    @Field(name = "mcts_full_name")
    public String getMctsFullName() {
        return mctsFullName;
    }

    public void setMctsFullName(String mctsFullName) {
        this.mctsFullName = mctsFullName;
    }

    @Field
    public String getMctsHusbandName() {
        return mctsHusbandName;
    }

    public void setMctsHusbandName(String mctsHusbandName) {
        this.mctsHusbandName = mctsHusbandName;
    }

    @Field
    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    @Field
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @Field
    public boolean getClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Field
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Field
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Field
    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    @Field
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Field
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Field
    @Cascade(persist = true, update = true, delete = false)
    public MotherCase getMotherCase() {
        return motherCase;
    }

    public void setMotherCase(MotherCase motherCase) {
        this.motherCase = motherCase;
    }

    @Field
    @Cascade(persist = true, update = true, delete = false)
    public MctsPregnantMother getMctsPregnantMother() {
        return mctsPregnantMother;
    }

    public void setMctsPregnantMother(MctsPregnantMother mctsPregnantMother) {
        this.mctsPregnantMother = mctsPregnantMother;
    }

}
