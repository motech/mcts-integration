package org.motechproject.mcts.integration.hibernate.model;

// Generated May 19, 2014 7:55:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MctsPregnantMotherServiceUpdate generated by hbm2java
 */
@Entity
@Table(name = "mcts_pregnant_mother_service_update", schema = "report")
public class MctsPregnantMotherServiceUpdate implements java.io.Serializable {

	private Integer id;
	private MctsPregnantMother mctsPregnantMother;
	private Short serviceType;
	private Date serviceDeliveryDate;
	private Date serviceUpdateTime;

	public MctsPregnantMotherServiceUpdate() {
	}

	public MctsPregnantMotherServiceUpdate(
			MctsPregnantMother mctsPregnantMother, Short serviceType,
			Date serviceDeliveryDate, Date serviceUpdateTime) {
		this.mctsPregnantMother = mctsPregnantMother;
		this.serviceType = serviceType;
		this.serviceDeliveryDate = serviceDeliveryDate;
		this.serviceUpdateTime = serviceUpdateTime;
	}

	@SequenceGenerator(name = "generator", sequenceName = "mcts_pregnant_mother_service_update_id_seq")
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
	@JoinColumn(name = "mcts_id")
	public MctsPregnantMother getMctsPregnantMother() {
		return this.mctsPregnantMother;
	}

	public void setMctsPregnantMother(MctsPregnantMother mctsPregnantMother) {
		this.mctsPregnantMother = mctsPregnantMother;
	}

	@Column(name = "service_type")
	public Short getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "service_delivery_date", length = 13)
	public Date getServiceDeliveryDate() {
		return this.serviceDeliveryDate;
	}

	public void setServiceDeliveryDate(Date serviceDeliveryDate) {
		this.serviceDeliveryDate = serviceDeliveryDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "service_update_time", length = 35)
	public Date getServiceUpdateTime() {
		return this.serviceUpdateTime;
	}

	public void setServiceUpdateTime(Date serviceUpdateTime) {
		this.serviceUpdateTime = serviceUpdateTime;
	}

}
