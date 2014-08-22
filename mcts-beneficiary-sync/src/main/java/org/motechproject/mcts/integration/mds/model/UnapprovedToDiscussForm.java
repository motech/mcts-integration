package org.motechproject.mcts.integration.mds.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class UnapprovedToDiscussForm {
    private String reasonDisapproved;
    private String showReasonDisapproved;
    private String badMapping;
    private String ashaCanFix;
    private String ashaNeedsToClose;
    private String anmClose;
    private String confirmAnmClose;
    private String dateModified;
    private boolean close;
    private String namespace;
    private String deviceID;
    private String instanceID;
    private String appVersion;
    private String userID;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;
    
    @Field
    public String getReasonDisapproved() {
        return reasonDisapproved;
    }
    public void setReasonDisapproved(String reasonDisapproved) {
        this.reasonDisapproved = reasonDisapproved;
    }
    
    @Field
    public String getShowReasonDisapproved() {
        return showReasonDisapproved;
    }
    public void setShowReasonDisapproved(String showReasonDisapproved) {
        this.showReasonDisapproved = showReasonDisapproved;
    }
    
    @Field 
    public String getBadMapping() {
        return badMapping;
    }
    public void setBadMapping(String badMapping) {
        this.badMapping = badMapping;
    }
    
    @Field
    public String getAshaCanFix() {
        return ashaCanFix;
    }
    public void setAshaCanFix(String ashaCanFix) {
        this.ashaCanFix = ashaCanFix;
    }
    
    @Field
    public String getAshaNeedsToClose() {
        return ashaNeedsToClose;
    }
    public void setAshaNeedsToClose(String ashaNeedsToClose) {
        this.ashaNeedsToClose = ashaNeedsToClose;
    }
    
    @Field
    public String getAnmClose() {
        return anmClose;
    }
    public void setAnmClose(String anmClose) {
        this.anmClose = anmClose;
    }
    
    @Field
    public String getConfirmAnmClose() {
        return confirmAnmClose;
    }
    public void setConfirmAnmClose(String confirmAnmClose) {
        this.confirmAnmClose = confirmAnmClose;
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
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    
    @Field
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
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
