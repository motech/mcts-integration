package org.motechproject.mcts.integration.mds.model;

import java.util.Arrays;
import java.util.Date;

import javax.jdo.annotations.Unique;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Unique(members = "case_id")
public class MotherCase implements
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7629916276936204480L;

    private static final Logger logger = LoggerFactory
            .getLogger("MotherCase");

    private Flw flw;
    private FlwGroup flwGroup;
    @Unique
    private String caseId;
    private String caseName;
    private String caseType;
    private Date dateModified;
    private Date serverDateModified;
    private Integer familyNumber;
    private Integer hhNumber;
    private String husbandName;
    private String lastVisitType;
    private String motherAlive;
    private Date motherDob;
    private String motherName;
    private Date closedOn;
    private Date add;
    private Integer age;
    private String birthPlace;
    private String complications;
    private Date dateNextBp;
    private Date dateNextCf;
    private Date dateNextEb;
    private Date dateNextPnc;
    private String eatsMeat;
    private Date edd;
    private String enrolledInKilkari;
    private String familyPlanningType;
    private Integer howManyChildren;
    private String interestInKilkari;
    private String lastPregTt;
    private Date lmp;
    private String mobileNumber;
    private Integer numBoys;
    private Date dateCf1;
    private Date dateCf2;
    private Date dateCf3;
    private Date dateCf4;
    private Date dateCf5;
    private Date dateCf6;
    private Date dateEb1;
    private Date dateEb2;
    private Date dateEb3;
    private Date dateEb4;
    private Date dateEb5;
    private Date dateEb6;
    private String allPncOnTime;
    private Date datePnc1;
    private Date datePnc2;
    private Date datePnc3;
    private String firstPncTime;
    private Integer pnc1DaysLate;
    private Integer pnc2DaysLate;
    private Integer pnc3DaysLate;
    private Date ttBoosterDate;
    private String sba;
    private String sbaPhone;
    private String accompany;
    private Date anc1Date;
    private Date anc2Date;
    private Date anc3Date;
    private Date anc4Date;
    private String cleanCloth;
    private String coupleInterested;
    private Date dateBp1;
    private Date dateBp2;
    private Date dateBp3;
    private Date dateLastVisit;
    private String deliveryType;
    private Integer ifaTablets;
    private Date ifaTablets100;
    private String materials;
    private String maternalEmergency;
    private String maternalEmergencyNumber;
    private String phoneVehicle;
    private String savingMoney;
    private Date tt1Date;
    private Date tt2Date;
    private String vehicle;
    private String birthStatus;
    private Date migrateOutDate;
    private String migratedStatus;
    private String status;
    private String term;
    private Date dateCf7;
    private Date dateDelFu;
    private Date dateNextReg;
    private String institutional;
    private Date dob;
    private Boolean closed;
    private Date creationTime;
    private Date lastModifiedTime;
    private Flw closedBy;
    private String mobileNumberWhose;
    private Integer bpVisitNum;
    private Integer wardNumber;
    private Integer ebVisitNum;
    private Integer pncVisitNum;
    private Integer cfVisitNum;

    public MotherCase() {
        Date date = new Date();
        creationTime = date;
        lastModifiedTime = date;
    }

   

    @Field
    @Cascade(persist = true, update = true, delete = true)
    public Flw getFlw() {
        return this.flw;
    }

    public void setFlw(Flw flw) {
        this.flw = flw;
    }

   
    @Field
    @Cascade(persist = true, update = true, delete = true)
    public FlwGroup getFlwGroup() {
        return this.flwGroup;
    }

    public void setFlwGroup(FlwGroup flwGroup) {
        this.flwGroup = flwGroup;
    }

    @Field
    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @Field
    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    @Field
    public String getCaseType() {
        return this.caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    @Field
    public Date getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Field
    public Date getServerDateModified() {
        return this.serverDateModified;
    }

    public void setServerDateModified(Date serverDateModified) {
        this.serverDateModified = serverDateModified;
    }

    @Field
    public Integer getFamilyNumber() {
        return this.familyNumber;
    }

    public void setFamilyNumber(Integer familyNumber) {
        this.familyNumber = familyNumber;
    }

    @Field
    public Integer getHhNumber() {
        return this.hhNumber;
    }

    public void setHhNumber(Integer hhNumber) {
        this.hhNumber = hhNumber;
    }

    @Field
    public String getHusbandName() {
        return this.husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    @Field
    public String getLastVisitType() {
        return lastVisitType;
    }

    public void setLastVisitType(String lastVisitType) {
        this.lastVisitType = lastVisitType;
    }

    @Field
    public String getMotherAlive() {
        return this.motherAlive;
    }

    public void setMotherAlive(String motherAlive) {
        this.motherAlive = motherAlive;
    }

    @Field
    public Date getMotherDob() {
        return this.motherDob;
    }

    public void setMotherDob(Date motherDob) {
        this.motherDob = motherDob;
    }

    @Field
    public String getMotherName() {
        return this.motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    @Field
    public Date getClosedOn() {
        return this.closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    @Field
    public Date getAdd() {
        return this.add;
    }

    public void setAdd(Date add) {
        this.add = add;
    }

    @Field
    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Field
    public String getBirthPlace() {
        return this.birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Field
    public String getComplications() {
        return this.complications;
    }

    public void setComplications(String complications) {
        this.complications = complications;
    }

    @Field
    public Date getDateNextBp() {
        return this.dateNextBp;
    }

    public void setDateNextBp(Date dateNextBp) {
        this.dateNextBp = dateNextBp;
    }

    

    @Field
    public Date getDateNextEb() {
        return this.dateNextEb;
    }

    public void setDateNextEb(Date dateNextEb) {
        this.dateNextEb = dateNextEb;
    }
    
    @Field
    public Date getDateNextCf() {
        return this.dateNextCf;
    }

    public void setDateNextCf(Date dateNextCf) {
        this.dateNextCf = dateNextCf;
    }

    @Field
    public Date getDateNextPnc() {
        return this.dateNextPnc;
    }

    public void setDateNextPnc(Date dateNextPnc) {
        this.dateNextPnc = dateNextPnc;
    }

   

    @Field
    public Date getEdd() {
        return this.edd;
    }

    public void setEdd(Date edd) {
        this.edd = edd;
    }

    @Field
    public String getEnrolledInKilkari() {
        return this.enrolledInKilkari;
    }

    public void setEnrolledInKilkari(String enrolledInKilkari) {
        this.enrolledInKilkari = enrolledInKilkari;
    }

    @Field
    public String getFamilyPlanningType() {
        return this.familyPlanningType;
    }

    public void setFamilyPlanningType(String familyPlanningType) {
        this.familyPlanningType = familyPlanningType;
    }

    @Field
    public Integer getHowManyChildren() {
        return this.howManyChildren;
    }

    public void setHowManyChildren(Integer howManyChildren) {
        this.howManyChildren = howManyChildren;
    }

    @Field
    public String getInterestInKilkari() {
        return this.interestInKilkari;
    }

    public void setInterestInKilkari(String interestInKilkari) {
        this.interestInKilkari = interestInKilkari;
    }

    @Field
    public String getLastPregTt() {
        return this.lastPregTt;
    }

    public void setLastPregTt(String lastPregTt) {
        this.lastPregTt = lastPregTt;
    }

    @Field
    public Date getLmp() {
        return this.lmp;
    }
    
    @Field
    public String getEatsMeat() {
        return this.eatsMeat;
    }

    public void setEatsMeat(String eatsMeat) {
        this.eatsMeat = eatsMeat;
    }

    public void setLmp(Date lmp) {
        this.lmp = lmp;
    }

    @Field
    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Field
    public Integer getNumBoys() {
        return this.numBoys;
    }

    public void setNumBoys(Integer numBoys) {
        this.numBoys = numBoys;
    }

    @Field
    public Date getDateCf1() {
        return this.dateCf1;
    }

    public void setDateCf1(Date dateCf1) {
        this.dateCf1 = dateCf1;
    }

    @Field
    public Date getDateCf2() {
        return this.dateCf2;
    }

    public void setDateCf2(Date dateCf2) {
        this.dateCf2 = dateCf2;
    }

    @Field
    public Date getDateCf3() {
        return this.dateCf3;
    }

    public void setDateCf3(Date dateCf3) {
        this.dateCf3 = dateCf3;
    }

    @Field
    public Date getDateCf4() {
        return this.dateCf4;
    }

    public void setDateCf4(Date dateCf4) {
        this.dateCf4 = dateCf4;
    }

    @Field
    public Date getDateCf5() {
        return this.dateCf5;
    }

    public void setDateCf5(Date dateCf5) {
        this.dateCf5 = dateCf5;
    }

    @Field
    public Date getDateCf6() {
        return this.dateCf6;
    }

    public void setDateCf6(Date dateCf6) {
        this.dateCf6 = dateCf6;
    }

    @Field
    public Date getDateEb1() {
        return this.dateEb1;
    }

    public void setDateEb1(Date dateEb1) {
        this.dateEb1 = dateEb1;
    }

    @Field
    public Date getDateEb2() {
        return this.dateEb2;
    }

    public void setDateEb2(Date dateEb2) {
        this.dateEb2 = dateEb2;
    }

    @Field
    public Date getDateEb3() {
        return this.dateEb3;
    }

    public void setDateEb3(Date dateEb3) {
        this.dateEb3 = dateEb3;
    }

    @Field
    public Date getDateEb4() {
        return this.dateEb4;
    }

    public void setDateEb4(Date dateEb4) {
        this.dateEb4 = dateEb4;
    }

    @Field
    public Date getDateEb5() {
        return this.dateEb5;
    }

    public void setDateEb5(Date dateEb5) {
        this.dateEb5 = dateEb5;
    }

    @Field
    public Date getDateEb6() {
        return this.dateEb6;
    }

    public void setDateEb6(Date dateEb6) {
        this.dateEb6 = dateEb6;
    }

    @Field
    public String getAllPncOnTime() {
        return this.allPncOnTime;
    }

    public void setAllPncOnTime(String allPncOnTime) {
        this.allPncOnTime = allPncOnTime;
    }

    @Field
    public Date getDatePnc1() {
        return this.datePnc1;
    }

    public void setDatePnc1(Date datePnc1) {
        this.datePnc1 = datePnc1;
    }

    @Field
    public Date getDatePnc2() {
        return this.datePnc2;
    }

    public void setDatePnc2(Date datePnc2) {
        this.datePnc2 = datePnc2;
    }

    @Field
    public Date getDatePnc3() {
        return this.datePnc3;
    }

    public void setDatePnc3(Date datePnc3) {
        this.datePnc3 = datePnc3;
    }

    @Field
    public String getFirstPncTime() {
        return this.firstPncTime;
    }

    public void setFirstPncTime(String firstPncTime) {
        this.firstPncTime = firstPncTime;
    }

    @Field 
    public Integer getPnc1DaysLate() {
        return this.pnc1DaysLate;
    }

    public void setPnc1DaysLate(Integer pnc1DaysLate) {
        this.pnc1DaysLate = pnc1DaysLate;
    }

    @Field 
    public Integer getPnc2DaysLate() {
        return this.pnc2DaysLate;
    }

    public void setPnc2DaysLate(Integer pnc2DaysLate) {
        this.pnc2DaysLate = pnc2DaysLate;
    }

    @Field
    public Integer getPnc3DaysLate() {
        return this.pnc3DaysLate;
    }

    public void setPnc3DaysLate(Integer pnc3DaysLate) {
        this.pnc3DaysLate = pnc3DaysLate;
    }

    @Field
    public Date getTtBoosterDate() {
        return this.ttBoosterDate;
    }

    public void setTtBoosterDate(Date ttBoosterDate) {
        this.ttBoosterDate = ttBoosterDate;
    }

    @Field
    public String getSba() {
        return this.sba;
    }

    public void setSba(String sba) {
        this.sba = sba;
    }

    @Field
    public String getSbaPhone() {
        return this.sbaPhone;
    }

    public void setSbaPhone(String sbaPhone) {
        this.sbaPhone = sbaPhone;
    }

    @Field
    public String getAccompany() {
        return this.accompany;
    }

    public void setAccompany(String accompany) {
        this.accompany = accompany;
    }

    @Field
    public Date getAnc1Date() {
        return this.anc1Date;
    }

    public void setAnc1Date(Date anc1Date) {
        this.anc1Date = anc1Date;
    }

    @Field
    public Date getAnc2Date() {
        return this.anc2Date;
    }

    public void setAnc2Date(Date anc2Date) {
        this.anc2Date = anc2Date;
    }

    @Field 
    public Date getAnc3Date() {
        return this.anc3Date;
    }

    public void setAnc3Date(Date anc3Date) {
        this.anc3Date = anc3Date;
    }

    @Field
    public Date getAnc4Date() {
        return this.anc4Date;
    }

    public void setAnc4Date(Date anc4Date) {
        this.anc4Date = anc4Date;
    }

    @Field
    public String getCleanCloth() {
        return this.cleanCloth;
    }

    public void setCleanCloth(String cleanCloth) {
        this.cleanCloth = cleanCloth;
    }

    @Field
    public String getCoupleInterested() {
        return this.coupleInterested;
    }

    public void setCoupleInterested(String coupleInterested) {
        this.coupleInterested = coupleInterested;
    }

    @Field
    public Date getDateBp1() {
        return this.dateBp1;
    }

    public void setDateBp1(Date dateBp1) {
        this.dateBp1 = dateBp1;
    }

    @Field
    public Date getDateBp2() {
        return this.dateBp2;
    }

    public void setDateBp2(Date dateBp2) {
        this.dateBp2 = dateBp2;
    }

    @Field
    public Date getDateBp3() {
        return this.dateBp3;
    }

    public void setDateBp3(Date dateBp3) {
        this.dateBp3 = dateBp3;
    }

    @Field
    public Date getDateLastVisit() {
        return this.dateLastVisit;
    }

    public void setDateLastVisit(Date dateLastVisit) {
        this.dateLastVisit = dateLastVisit;
    }

    @Field 
    public String getDeliveryType() {
        return this.deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Field 
    public Integer getIfaTablets() {
        return this.ifaTablets;
    }

    public void setIfaTablets(Integer ifaTablets) {
        this.ifaTablets = ifaTablets;
    }

    @Field
    public Date getIfaTablets100() {
        return this.ifaTablets100;
    }

    public void setIfaTablets100(Date ifaTablets100) {
        this.ifaTablets100 = ifaTablets100;
    }

    @Field
    public String getMaterials() {
        return this.materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    @Field
    public String getMaternalEmergency() {
        return this.maternalEmergency;
    }

    public void setMaternalEmergency(String maternalEmergency) {
        this.maternalEmergency = maternalEmergency;
    }

    @Field
    public String getMaternalEmergencyNumber() {
        return this.maternalEmergencyNumber;
    }

    public void setMaternalEmergencyNumber(String maternalEmergencyNumber) {
        this.maternalEmergencyNumber = maternalEmergencyNumber;
    }

    @Field
    public String getPhoneVehicle() {
        return this.phoneVehicle;
    }

    public void setPhoneVehicle(String phoneVehicle) {
        this.phoneVehicle = phoneVehicle;
    }

    @Field
    public String getSavingMoney() {
        return this.savingMoney;
    }

    public void setSavingMoney(String savingMoney) {
        this.savingMoney = savingMoney;
    }

    @Field
    public Date getTt1Date() {
        return this.tt1Date;
    }

    public void setTt1Date(Date tt1Date) {
        this.tt1Date = tt1Date;
    }

    @Field
    public Date getTt2Date() {
        return this.tt2Date;
    }

    public void setTt2Date(Date tt2Date) {
        this.tt2Date = tt2Date;
    }

    @Field
    public String getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Field
    public String getBirthStatus() {
        return this.birthStatus;
    }

    public void setBirthStatus(String birthStatus) {
        this.birthStatus = birthStatus;
    }

    @Field 
    public Date getMigrateOutDate() {
        return this.migrateOutDate;
    }

    public void setMigrateOutDate(Date migrateOutDate) {
        this.migrateOutDate = migrateOutDate;
    }

    @Field
    public String getMigratedStatus() {
        return this.migratedStatus;
    }

    public void setMigratedStatus(String migratedStatus) {
        this.migratedStatus = migratedStatus;
    }

    @Field 
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Field
    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Field
    public Date getDateCf7() {
        return this.dateCf7;
    }

    public void setDateCf7(Date dateCf7) {
        this.dateCf7 = dateCf7;
    }

    @Field
    public Date getDateDelFu() {
        return this.dateDelFu;
    }

    public void setDateDelFu(Date dateDelFu) {
        this.dateDelFu = dateDelFu;
    }

    @Field
    public Date getDateNextReg() {
        return this.dateNextReg;
    }

    public void setDateNextReg(Date dateNextReg) {
        this.dateNextReg = dateNextReg;
    }

    @Field 
    public String getInstitutional() {
        return this.institutional;
    }

    public void setInstitutional(String institutional) {
        this.institutional = institutional;
    }

    @Field
    public Date getDob() {
        return this.dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Field 
    public Boolean getClosed() {
        return this.closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Field
    public String getMobileNumberWhose() {
        return this.mobileNumberWhose;
    }

    public void setMobileNumberWhose(String mobileNumberWhose) {
        this.mobileNumberWhose = mobileNumberWhose;
    }

    @Field
    public Integer getWardNumber() {
        return this.wardNumber;
    }

    public void setWardNumber(Integer wardNumber) {
        this.wardNumber = wardNumber;
    }

    @Field
    public Integer getBpVisitNum() {
        return this.bpVisitNum;
    }

    public void setBpVisitNum(Integer bpVisitNum) {
        this.bpVisitNum = bpVisitNum;
    }

    @Field 
    public Integer getEbVisitNum() {
        return this.ebVisitNum;
    }

    public void setEbVisitNum(Integer ebVisitNum) {
        this.ebVisitNum = ebVisitNum;
    }

    @Field
    public Integer getPncVisitNum() {
        return this.pncVisitNum;
    }

    public void setPncVisitNum(Integer pncVisitNum) {
        this.pncVisitNum = pncVisitNum;
    }

    @Field 
    public Integer getCfVisitNum() {
        return this.cfVisitNum;
    }

    public void setCfVisitNum(Integer cfVisitNum) {
        this.cfVisitNum = cfVisitNum;
    }

    @Field
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Field
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Field
    @Cascade(persist = true, update = true, delete = true)
    public Flw getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Flw closedBy) {
        this.closedBy = closedBy;
    }

    private boolean isLatest(MotherCase updatedObject) {
        if (this.serverDateModified == null)
            return true;
        else if (updatedObject.serverDateModified == null)
            return false;
        return this.serverDateModified
                .compareTo(updatedObject.serverDateModified) <= 0;
    }
}
