package org.motechproject.mcts.integration.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "mcts_pregnant_mother_service_update")
public class MCTSPregnantMotherServiceUpdate {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mcts_id")
    private MCTSPregnantMother mctsPregnantMother;

    @Column(name = "service_type")
    private Integer serviceType;

    @Column(name = "service_delivery_date")
    private Date serviceDeliveryDate;

    @Column(name = "service_update_time")
    private Timestamp serviceUpdateTime;

    public MCTSPregnantMotherServiceUpdate() {
    }

    public MCTSPregnantMotherServiceUpdate(MCTSPregnantMother mctsPregnantMother, Integer serviceType, Date serviceDeliveryDate, Timestamp serviceUpdateTime) {
        this.mctsPregnantMother = mctsPregnantMother;
        this.serviceType = serviceType;
        this.serviceDeliveryDate = serviceDeliveryDate;
        this.serviceUpdateTime = serviceUpdateTime;
    }

    public MCTSPregnantMother getMctsPregnantMother() {
        return mctsPregnantMother;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public Date getServiceDeliveryDate() {
        return serviceDeliveryDate;
    }
}
