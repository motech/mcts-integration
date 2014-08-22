package org.motechproject.mcts.integration.mds.model;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class UnmappedToReviewForm {
    private String known;
    private String dontKnow;
    private String prevAshaId;
    private String ashaId;
    private String ashaName;
    private String mctsHusbandName;
    private String mctsFullName;
    private String langCode;
    private String dateModified;
    private boolean close;
    private String namespace;
    private String deviceID;
    private String instanceID;
    private String userID;
    private String appVersion;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;
    
    
    
    @Field
    public String getKnown() {
        return known;
    }
    public void setKnown(String known) {
        this.known = known;
    }
    
    @Field
    public String getDontKnow() {
        return dontKnow;
    }
    public void setDontKnow(String dontKnow) {
        this.dontKnow = dontKnow;
    }
    
    @Field
    public String getPrevAshaId() {
        return prevAshaId;
    }
    public void setPrevAshaId(String prevAshaId) {
        this.prevAshaId = prevAshaId;
    }
    
    @Field
    public String getAshaId() {
        return ashaId;
    }
    public void setAshaId(String ashaId) {
        this.ashaId = ashaId;
    }
    
    @Field
    public String getAshaName() {
        return ashaName;
    }
    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
    }
    
    @Field
    public String getMctsHusbandName() {
        return mctsHusbandName;
    }
    public void setMctsHusbandName(String mctsHusbandName) {
        this.mctsHusbandName = mctsHusbandName;
    }
    
    @Field
    public String getMctsFullName() {
        return mctsFullName;
    }
    public void setMctsFullName(String mctsFullName) {
        this.mctsFullName = mctsFullName;
    }
    
    @Field
    public String getLangCode() {
        return langCode;
    }
    public void setLangCode(String langCode) {
        this.langCode = langCode;
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
    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    
    @Field
    public String getInstanceID() {
        return instanceID;
    }
    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }
    
    @Field
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
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
