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
@Table(name = "dont_know_form")
public class DontKnowForm {
    private int id;
    private String dontKnow;
    private String known;
    private String unconfirmed; 
    private String mctsHusbandName;
    private String mctsFullName;
    private String nameSpace;
    private String dateModified;
    private String instanceId;
    private String deviceId;
    private String ownerId;
    private String appVersion;
    private String userId;
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

    @Column(name = "dont_know")
    public String getDontKnow() {
        return dontKnow;
    }

    public void setDontKnow(String dontKnow) {
        this.dontKnow = dontKnow;
    }

    @Column(name = "known")
    public String getKnown() {
        return known;
    }

    public void setKnown(String known) {
        this.known = known;
    }

    @Column(name = "unconfirmed")
    public String getUnconfirmed() {
        return unconfirmed;
    }

    public void setUnconfirmed(String unconfirmed) {
        this.unconfirmed = unconfirmed;
    }

    @Column(name = "mcts_husband_name")
    public String getMctsHusbandName() {
        return mctsHusbandName;
    }

    public void setMctsHusbandName(String mctsHusbandName) {
        this.mctsHusbandName = mctsHusbandName;
    }

    @Column(name = "mcts_full_name")
    public String getMctsFullName() {
        return mctsFullName;
    }

    public void setMctsFullName(String mctsFullName) {
        this.mctsFullName = mctsFullName;
    }

    @Column(name = "date_modified")
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @Column(name = "namespace")
    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @Column(name = "instance_id")
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "owner_id")
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Column(name = "app_version")
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    public MotherCase getMotherCase() {
        return this.motherCase;
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
