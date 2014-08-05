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
@Table(name = "mapping_to_approve_form")
public class MappingToApproveForm {
    private int id;
    private String confirmMappingApproval;
    private String approved;
    private String confirmNewCaseApproval;
    private String disapproved;
    private String reasonDisapproved;
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

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "confirm_mapping_approval")
    public String getConfirmMappingApproval() {
        return confirmMappingApproval;
    }

    public void setConfirmMappingApproval(String confirmMappingApproval) {
        this.confirmMappingApproval = confirmMappingApproval;
    }

    @Column(name = "approved")
    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
    
    @Column(name = "confirm_new_case_approval")
    public String getConfirmNewCaseApproval() {
        return confirmNewCaseApproval;
    }

    public void setConfirmNewCaseApproval(String confirmNewCaseApproval) {
        this.confirmNewCaseApproval = confirmNewCaseApproval;
    }
    
    @Column(name = "disapproved")
    public String getDisapproved() {
        return disapproved;
    }

    public void setDisapproved(String disapproved) {
        this.disapproved = disapproved;
    }

    @Column(name = "reason_disapproved")
    public String getReasonDisapproved() {
        return reasonDisapproved;
    }

    public void setReasonDisapproved(String reasonDisapproved) {
        this.reasonDisapproved = reasonDisapproved;
    }


    @Column(name = "date_modified")
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    @Column(name = "close")
    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }



    @Column(name = "date_authorized")
    public String getDateAuthorized() {
        return dateAuthorized;
    }

    public void setDateAuthorized(String dateAuthorized) {
        this.dateAuthorized = dateAuthorized;
    }

    @Column(name = "date_authorized_int")
    public String getDateAuthorizedInt() {
        return dateAuthorizedInt;
    }

    public void setDateAuthorizedInt(String dateAuthorizedInt) {
        this.dateAuthorizedInt = dateAuthorizedInt;
    }

    @Column(name = "namespace")
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "instance_id")
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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
