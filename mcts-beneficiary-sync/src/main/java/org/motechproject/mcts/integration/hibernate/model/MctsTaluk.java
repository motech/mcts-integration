package org.motechproject.mcts.integration.hibernate.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * MctsTaluk generated by hbm2java
 */
@Entity
@Table(name = "mcts_taluk", schema = "report", uniqueConstraints = @UniqueConstraint(columnNames = {
        "taluk_id", "district_id" }))
public class MctsTaluk implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -226240310604906915L;
    private Integer id;
    private MctsDistrict mctsDistrict;
    private int talukId;
    private String name;
    private boolean status;
    private Set<MctsHealthblock> mctsHealthblocks = new HashSet<MctsHealthblock>(
            0);
    private Set<MctsVillage> mctsVillages = new HashSet<MctsVillage>(0);

    public MctsTaluk() {
    }

    public MctsTaluk(MctsDistrict mctsDistrict, int talukId, String name) {
        this.mctsDistrict = mctsDistrict;
        this.talukId = talukId;
        this.name = name;
    }

    public MctsTaluk(MctsDistrict mctsDistrict, int talukId, String name,
            Set<MctsHealthblock> mctsHealthblocks,
            Set<MctsVillage> mctsVillages, boolean status) {
        this.mctsDistrict = mctsDistrict;
        this.talukId = talukId;
        this.name = name;
        this.mctsHealthblocks = mctsHealthblocks;
        this.mctsVillages = mctsVillages;
        this.status = status;
    }

    @SequenceGenerator(name = "generator", sequenceName = "mcts_taluk_id_seq")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    public MctsDistrict getMctsDistrict() {
        return this.mctsDistrict;
    }

    public void setMctsDistrict(MctsDistrict mctsDistrict) {
        this.mctsDistrict = mctsDistrict;
    }

    @Column(name = "taluk_id", nullable = false)
    public int getTalukId() {
        return this.talukId;
    }

    public void setTalukId(int talukId) {
        this.talukId = talukId;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status")
    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsTaluk")
    public Set<MctsHealthblock> getMctsHealthblocks() {
        return this.mctsHealthblocks;
    }

    public void setMctsHealthblocks(Set<MctsHealthblock> mctsHealthblocks) {
        this.mctsHealthblocks = mctsHealthblocks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsTaluk")
    public Set<MctsVillage> getMctsVillages() {
        return this.mctsVillages;
    }

    public void setMctsVillages(Set<MctsVillage> mctsVillages) {
        this.mctsVillages = mctsVillages;
    }

}
