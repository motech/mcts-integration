package org.motechproject.mcts.integration.hibernate.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "mcts_pregnant_mother", uniqueConstraints = {@UniqueConstraint(columnNames = {"mcts_id"}), @UniqueConstraint(columnNames = {"case_id"})})
public class MCTSPregnantMother {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mcts_id")
    private String mctsId;

    @OneToOne
    @JoinColumn(name = "case_id")
    private MotherCase motherCase;

    public MCTSPregnantMother() {
    }

    public MCTSPregnantMother(String mctsId, MotherCase motherCase) {
        this.mctsId = mctsId;
        this.motherCase = motherCase;
    }

    public Integer getId() {
        return id;
    }

    public String getMctsId() {
        return mctsId;
    }

    public MotherCase getMotherCase() {
        return motherCase;
    }

    public void updateMctsId(String mctsId) {
        this.mctsId = mctsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCTSPregnantMother that = (MCTSPregnantMother) o;

        EqualsBuilder equalsBuilder = new EqualsBuilder()
                .append(mctsId, that.mctsId);

        if (motherCase == null && that.motherCase == null) return equalsBuilder.isEquals();
        if (motherCase == null || that.motherCase == null) return false;

        return equalsBuilder
                .append(motherCase.getCaseId(), that.getMotherCase().getCaseId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(mctsId)
                .append(motherCase != null ? motherCase.getCaseId() : 0)
                .hashCode();
    }
    
    @Override
    public String toString()
    {
    	return String.format("id:%s\tmcts_id:%s\tcase_id:%s", this.id, this.mctsId, this.motherCase);
    }
}
