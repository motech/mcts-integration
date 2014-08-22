package org.motechproject.mcts.integration.mds.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class MappingToApproveForm {
    private String confirmMappingApproval;
    private String approved;
    private String dateModified;
    private boolean close;
    private String dateAuthorized;
    private String dateAuthorizedInt;
    private String namespace;
    private String deviceId;
    private String instanceId;
    private String appVersion;
    private String userId;
    private MotherCase motherCase;
    private MctsPregnantMother mctsPregnantMother;

    @Field
    public String getConfirmMappingApproval() {
        return confirmMappingApproval;
    }

    public void setConfirmMappingApproval(String confirmMappingApproval) {
        this.confirmMappingApproval = confirmMappingApproval;
    }

    @Field
    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    @Field
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    @Field
    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
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
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Field
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Field
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Field
    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Field
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
