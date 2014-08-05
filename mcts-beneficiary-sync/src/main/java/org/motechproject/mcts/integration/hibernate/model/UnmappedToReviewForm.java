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
@Table(name = "unmapped_to_review_form")
public class UnmappedToReviewForm {
    private int id;
    private String known;
    private String dontKnow;
    private String prevAshaId;
    private String ashaId;
    private String ashaName;
    private String mctsHusbandName;
    private String mctsFullName;
    private String langCode;
    private String isCorrectAsha;
    private String noAshaExisting;
    private String noAsha;
    private String newAsha;
    private String newAssignment;
    private String sameAssignment;
    private String newAshaName;
    private String dateModified;
    private boolean close;
    private String namespace;
    private String deviceID;
    private String instanceID;
    private String userID;
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
    
    @Column(name = "known")
    public String getKnown() {
        return known;
    }
    public void setKnown(String known) {
        this.known = known;
    }
    
    @Column(name = "dont_know")
    public String getDontKnow() {
        return dontKnow;
    }
    public void setDontKnow(String dontKnow) {
        this.dontKnow = dontKnow;
    }
    
    @Column(name = "prev_asha_id")
    public String getPrevAshaId() {
        return prevAshaId;
    }
    public void setPrevAshaId(String prevAshaId) {
        this.prevAshaId = prevAshaId;
    }
    
    @Column(name = "asha_id")
    public String getAshaId() {
        return ashaId;
    }
    public void setAshaId(String ashaId) {
        this.ashaId = ashaId;
    }
    
    @Column(name = "asha_name")
    public String getAshaName() {
        return ashaName;
    }
    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
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
    
    @Column(name = "lang_code")
    public String getLangCode() {
        return langCode;
    }
    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }
    
    @Column(name = "is_correct_asha")
    public String getIsCorrectAsha() {
        return isCorrectAsha;
    }
    public void setIsCorrectAsha(String isCorrectAsha) {
        this.isCorrectAsha = isCorrectAsha;
    }
    
    @Column(name = "no_asha_existing")
    public String getNoAshaExisting() {
        return noAshaExisting;
    }
    public void setNoAshaExisting(String noAshaExisting) {
        this.noAshaExisting = noAshaExisting;
    }
    
    @Column(name = "no_asha")
    public String getNoAsha() {
        return noAsha;
    }
    public void setNoAsha(String noAsha) {
        this.noAsha = noAsha;
    }
    
    @Column(name = "new_asha")
    public String getNewAsha() {
        return newAsha;
    }
    public void setNewAsha(String newAsha) {
        this.newAsha = newAsha;
    }
    
    @Column(name = "new_assignment")
    public String getNewAssignment() {
        return newAssignment;
    }
    public void setNewAssignment(String newAssignment) {
        this.newAssignment = newAssignment;
    }
    
    @Column(name = "same_assignment")
    public String getSameAssignment() {
        return sameAssignment;
    }
    public void setSameAssignment(String sameAssignment) {
        this.sameAssignment = sameAssignment;
    }
    
    @Column(name = "new_asha_name")
    public String getNewAshaName() {
        return newAshaName;
    }
    public void setNewAshaName(String newAshaName) {
        this.newAshaName = newAshaName;
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
    
    @Column(name = "device_id")
    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    
    @Column(name = "instance_id")
    public String getInstanceID() {
        return instanceID;
    }
    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }
    
    @Column(name = "user_id")
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    @Column(name = "app_version")
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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
