package org.motechproject.mcts.integration.mds.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * MctsLocationMaster generated by hbm2java
 */
@Entity
public class MctsLocationErrorLog implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1135568505844170766L;
    private String stateid;
    private String state;
    private String dcode;
    private String district;
    private String tcode;
    private String talukaname;
    private String bid;
    private String block;
    private String pid;
    private String phc;
    private String sid;
    private String subcenter;
    private String vcode;
    private String village;
    private String status;
    private String comments;

    public MctsLocationErrorLog() {
    }

    public MctsLocationErrorLog(String stateid, String state, String dcode,
            String district, String tcode, String talukaname, String bid,
            String block, String pid, String phc, String sid, String subcenter,
            String vcode, String village, String status, String comments) {
        this.stateid = stateid;
        this.state = state;
        this.dcode = dcode;
        this.district = district;
        this.tcode = tcode;
        this.talukaname = talukaname;
        this.bid = bid;
        this.block = block;
        this.pid = pid;
        this.phc = phc;
        this.sid = sid;
        this.subcenter = subcenter;
        this.vcode = vcode;
        this.village = village;
        this.status = status;
        this.comments = comments;
    }

   
    @Field
    public String getStateid() {
        return this.stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    @Field
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Field
    public String getDcode() {
        return this.dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    @Field
    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Field
    public String getTcode() {
        return this.tcode;
    }

    public void setTcode(String tcode) {
        this.tcode = tcode;
    }

    @Field
    public String getTalukaname() {
        return this.talukaname;
    }

    public void setTalukaname(String talukaname) {
        this.talukaname = talukaname;
    }

    @Field
    public String getBid() {
        return this.bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Field
    public String getBlock() {
        return this.block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    @Field
    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Field
    public String getPhc() {
        return this.phc;
    }

    public void setPhc(String phc) {
        this.phc = phc;
    }

    @Field
    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Field
    public String getSubcenter() {
        return this.subcenter;
    }

    public void setSubcenter(String subcenter) {
        this.subcenter = subcenter;
    }

    @Field
    public String getVcode() {
        return this.vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    @Field
    public String getVillage() {
        return this.village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    @Field
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Field
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
