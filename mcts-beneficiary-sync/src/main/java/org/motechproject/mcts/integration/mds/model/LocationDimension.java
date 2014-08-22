package org.motechproject.mcts.integration.mds.model;

import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
@Unique(members = {
        "state", "district", "block" })
public class LocationDimension implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6060839638633366766L;
    private String state;
    private String district;
    private String block;

    public LocationDimension() {

    }

    public LocationDimension(String state, String district, String block) {
        this.state = state;
        this.district = district;
        this.block = block;
    }

    @Field
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Field
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Field
    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
