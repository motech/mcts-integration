package org.motechproject.mcts.integration.hibernate.model;

// Generated May 15, 2014 10:59:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * MctsDistrict generated by hbm2java
 */
@Entity
@Table(name = "mcts_district", schema = "report")
public class MctsDistrict implements java.io.Serializable {

	private Integer id;
	private MctsState mctsState;
	private int disctrictId;
	private String name;
	private Set<MctsTaluk> mctsTaluks = new HashSet<MctsTaluk>(0);

	public MctsDistrict() {
	}

	public MctsDistrict(MctsState mctsState, int disctrictId, String name) {
		this.mctsState = mctsState;
		this.disctrictId = disctrictId;
		this.name = name;
	}

	public MctsDistrict(MctsState mctsState, int disctrictId, String name,
			Set<MctsTaluk> mctsTaluks) {
		this.mctsState = mctsState;
		this.disctrictId = disctrictId;
		this.name = name;
		this.mctsTaluks = mctsTaluks;
	}

	@SequenceGenerator(name = "generator", sequenceName = "mcts_district_id_seq")
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
	@JoinColumn(name = "state_id", nullable = false)
	public MctsState getMctsState() {
		return this.mctsState;
	}

	public void setMctsState(MctsState mctsState) {
		this.mctsState = mctsState;
	}

	@Column(name = "disctrict_id", nullable = false)
	public int getDisctrictId() {
		return this.disctrictId;
	}

	public void setDisctrictId(int disctrictId) {
		this.disctrictId = disctrictId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsDistrict")
	public Set<MctsTaluk> getMctsTaluks() {
		return this.mctsTaluks;
	}

	public void setMctsTaluks(Set<MctsTaluk> mctsTaluks) {
		this.mctsTaluks = mctsTaluks;
	}

}
