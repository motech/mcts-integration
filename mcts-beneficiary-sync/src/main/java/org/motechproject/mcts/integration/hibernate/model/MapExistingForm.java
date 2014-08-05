package org.motechproject.mcts.integration.hibernate.model;

import java.util.Date;

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
@Table(name = "map_existing_form")
public class MapExistingForm {
    private int id;
    private String confirmMapping;
    private String success;
    private String notMapped;
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
    
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name = "confirm_mapping")
    public String getConfirmMapping() {
        return confirmMapping;
    }
    public void setConfirmMapping(String confirmMapping) {
        this.confirmMapping = confirmMapping;
    }
    
    @Column(name = "success")
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    
    @Column(name = "not_mapped")
    public String getNotMapped() {
        return notMapped;
    }
    public void setNotMapped(String notMapped) {
        this.notMapped = notMapped;
    }
    
    @Column(name = "mcts_id")
    public String getMctsId() {
        return mctsId;
    }
    public void setMctsId(String mctsId) {
        this.mctsId = mctsId;
    }
    
    @Column(name = "full_mcts_id")
    public String getFullMctsId() {
        return fullMctsId;
    }
    public void setFullMctsId(String fullMctsId) {
        this.fullMctsId = fullMctsId;
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
    
    @Column(name = "date_modified")
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
    
    @Column(name = "owner_id")
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
    
    @Column(name = "namespace")
    public String getNameSpace() {
        return nameSpace;
    }
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
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
