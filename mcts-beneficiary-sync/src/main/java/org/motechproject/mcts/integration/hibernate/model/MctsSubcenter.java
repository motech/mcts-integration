package org.motechproject.mcts.integration.hibernate.model;

// Generated May 14, 2014 11:43:34 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MctsSubcenter generated by hbm2java
 */
@Entity
@Table(name = "mcts_subcenter", schema = "report")
public class MctsSubcenter implements java.io.Serializable {

	private int id;
	private MctsPhc mctsPhc;
	private int subcenterId;
	private String name;
	private Set<MctsVillage> mctsVillages = new HashSet<MctsVillage>(0);
	private Set<MctsHealthworker> mctsHealthworkers = new HashSet<MctsHealthworker>(
			0);

	public MctsSubcenter() {
	}

	public MctsSubcenter(int id, MctsPhc mctsPhc, int subcenterId, String name) {
		this.id = id;
		this.mctsPhc = mctsPhc;
		this.subcenterId = subcenterId;
		this.name = name;
	}

	public MctsSubcenter(int id, MctsPhc mctsPhc, int subcenterId, String name,
			Set<MctsVillage> mctsVillages,
			Set<MctsHealthworker> mctsHealthworkers) {
		this.id = id;
		this.mctsPhc = mctsPhc;
		this.subcenterId = subcenterId;
		this.name = name;
		this.mctsVillages = mctsVillages;
		this.mctsHealthworkers = mctsHealthworkers;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phc_id", nullable = false)
	public MctsPhc getMctsPhc() {
		return this.mctsPhc;
	}

	public void setMctsPhc(MctsPhc mctsPhc) {
		this.mctsPhc = mctsPhc;
	}

	@Column(name = "subcenter_id", nullable = false)
	public int getSubcenterId() {
		return this.subcenterId;
	}

	public void setSubcenterId(int subcenterId) {
		this.subcenterId = subcenterId;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsSubcenter")
	public Set<MctsVillage> getMctsVillages() {
		return this.mctsVillages;
	}

	public void setMctsVillages(Set<MctsVillage> mctsVillages) {
		this.mctsVillages = mctsVillages;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mctsSubcenter")
	public Set<MctsHealthworker> getMctsHealthworkers() {
		return this.mctsHealthworkers;
	}

	public void setMctsHealthworkers(Set<MctsHealthworker> mctsHealthworkers) {
		this.mctsHealthworkers = mctsHealthworkers;
	}

}