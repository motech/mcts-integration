package org.motechproject.mcts.integration.mds.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class MapExistingForm {
    private String confirmMapping;
    private String success;
    private String mctsId;
    private String fullMctsId;
    private String dateAuthorized;
    private String dateAuthorizedInt;
    private String dateModified;
    private String ownerId;
    private String deviceID;
    private String instanceID;
    private String nameSpace;
    private String userID;
    private String appVersion;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;
    
    @Field
    public String getConfirmMapping() {
        return confirmMapping;
    }
    public void setConfirmMapping(String confirmMapping) {
        this.confirmMapping = confirmMapping;
    }
    
    @Field
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    
    @Field
    public String getMctsId() {
        return mctsId;
    }
    public void setMctsId(String mctsId) {
        this.mctsId = mctsId;
    }
    
    @Field
    public String getFullMctsId() {
        return fullMctsId;
    }
    public void setFullMctsId(String fullMctsId) {
        this.fullMctsId = fullMctsId;
    }
    
    @Field
    public String getDateAuthorized() {
        return dateAuthorized;
    }
    public void setDateAuthorized(String dateAuthorized) {
        this.dateAuthorized = dateAuthorized;
    }
    
    @Field
    public String getDateAuthorizedInt() {
        return dateAuthorizedInt;
    }
    public void setDateAuthorizedInt(String dateAuthorizedInt) {
        this.dateAuthorizedInt = dateAuthorizedInt;
    }
    
    @Field
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    @Field
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
    public String getNameSpace() {
        return nameSpace;
    }
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
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
