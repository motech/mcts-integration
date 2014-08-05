package org.motechproject.mcts.integration.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "case_already_closed_form")
public class CaseAlreadyClosedForm {
    private int id;
    private String permanentMove;
    private String dateMoveKnown;
    private String moveDate;
    private String hhNumber;
    private String familyNumber;
    private String successClose;
    private String successStillOpen;
    private String died;
    private String dateDeathKnown;
    private String dateDeath;
    private String siteDeath;
    private String diedVillage;
    private String placeDeath;
    private String abortion;
    private String abortionType;
    private String dateAbortionKnown;
    private String dateAborted;
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
    
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "permanent_move")
    public String getPermanentMove() {
        return permanentMove;
    }

    public void setPermanentMove(String permanentMove) {
        this.permanentMove = permanentMove;
    }

    @Column(name = "date_move_known")
    public String getDateMoveKnown() {
        return dateMoveKnown;
    }

    public void setDateMoveKnown(String dateMoveKnown) {
        this.dateMoveKnown = dateMoveKnown;
    }

    @Column(name = "move_date")
    public String getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(String moveDate) {
        this.moveDate = moveDate;
    }

    @Column(name = "hh_number")
    public String getHhNumber() {
        return hhNumber;
    }

    public void setHhNumber(String hhNumber) {
        this.hhNumber = hhNumber;
    }

    @Column(name = "family_number")
    public String getFamilyNumber() {
        return familyNumber;
    }

    public void setFamilyNumber(String familyNumber) {
        this.familyNumber = familyNumber;
    }

    @Column(name = "success_close")
    public String getSuccessClose() {
        return successClose;
    }

    public void setSuccessClose(String successClose) {
        this.successClose = successClose;
    }

    @Column(name = "mcts_full_name")
    public String getMctsFullName() {
        return mctsFullName;
    }

    public void setMctsFullName(String mctsFullName) {
        this.mctsFullName = mctsFullName;
    }

    @Column(name = "mcts_husband_name")
    public String getMctsHusbandName() {
        return mctsHusbandName;
    }

    public void setMctsHusbandName(String mctsHusbandName) {
        this.mctsHusbandName = mctsHusbandName;
    }

    @Column(name = "close_reason")
    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    @Column(name = "date_modified")
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @Column(name = "close")
    public boolean getClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Column(name = "namespace")
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Column(name = "user_id")
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Column(name = "instance_id")
    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    @Column(name = "device_id")
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Column(name = "app_version")
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    
    @Column(name = "success_still_open")
    public String getSuccessStillOpen() {
        return successStillOpen;
    }

    public void setSuccessStillOpen(String successStillOpen) {
        this.successStillOpen = successStillOpen;
    }

    @Column(name = "died")
    public String getDied() {
        return died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    @Column(name = "date_death_known")
    public String getDateDeathKnown() {
        return dateDeathKnown;
    }

    public void setDateDeathKnown(String dateDeathKnown) {
        this.dateDeathKnown = dateDeathKnown;
    }

    @Column(name = "date_death")
    public String getDateDeath() {
        return dateDeath;
    }

    public void setDateDeath(String dateDeath) {
        this.dateDeath = dateDeath;
    }

    @Column(name = "site_death")
    public String getSiteDeath() {
        return siteDeath;
    }

    public void setSiteDeath(String siteDeath) {
        this.siteDeath = siteDeath;
    }

    @Column(name = "died_village")
    public String getDiedVillage() {
        return diedVillage;
    }

    public void setDiedVillage(String diedVillage) {
        this.diedVillage = diedVillage;
    }

    @Column(name = "place_death")
    public String getPlaceDeath() {
        return placeDeath;
    }

    public void setPlaceDeath(String placeDeath) {
        this.placeDeath = placeDeath;
    }

    @Column(name = "abortion")
    public String getAbortion() {
        return abortion;
    }

    public void setAbortion(String abortion) {
        this.abortion = abortion;
    }

    @Column(name = "abortion_type")
    public String getAbortionType() {
        return abortionType;
    }

    public void setAbortionType(String abortionType) {
        this.abortionType = abortionType;
    }

    @Column(name = "date_abortion_known")
    public String getDateAbortionKnown() {
        return dateAbortionKnown;
    }

    public void setDateAbortionKnown(String dateAbortionKnown) {
        this.dateAbortionKnown = dateAbortionKnown;
    }

    @Column(name = "date_aborted")
    public String getDateAborted() {
        return dateAborted;
    }

    public void setDateAborted(String dateAborted) {
        this.dateAborted = dateAborted;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    public MotherCase getMotherCase() {
        return motherCase;
    }

    public void setMotherCase(MotherCase motherCase) {
        this.motherCase = motherCase;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcts_case_id")
    public MctsPregnantMother getMctsPregnantMother() {
        return mctsPregnantMother;
    }

    public void setMctsPregnantMother(MctsPregnantMother mctsPregnantMother) {
        this.mctsPregnantMother = mctsPregnantMother;
    }

}
