// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quotations")
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private int totalHours;

    @Temporal(TemporalType.DATE)
    @Column(name = "tentative_start_date")
    private Date tentativeStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "tentative_end_date")
    private Date tentativeEndDate;

    @Column(precision = 10, scale = 2)
    private double assignmentCost;

    @Column(precision = 10, scale = 2)
    private double additionalCosts;

    @Column(precision = 10, scale = 2)
    private double total;

    @Column(name = "created_by")
    private Integer createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationActivity> activities;

    public void calculateTotals() {
        BigDecimal assignmentCost = BigDecimal.ZERO;
        int totalHours = 0;

        if (this.activities != null) {
            for (QuotationActivity activity : this.activities) {
                BigDecimal hourlyCost = activity.getHourlyCost();
                BigDecimal hours = BigDecimal.valueOf(activity.getEstimatedHours());
                assignmentCost = assignmentCost.add(hourlyCost.multiply(hours));
                totalHours += activity.getEstimatedHours();
            }
        }

        this.setAssignmentCost(assignmentCost.doubleValue());
        this.setTotalHours(totalHours);
        this.setTotal(assignmentCost.add(BigDecimal.valueOf(this.getAdditionalCosts())).doubleValue());
    }
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Date getTentativeStartDate() { return tentativeStartDate; }
    public void setTentativeStartDate(Date tentativeStartDate) {
        this.tentativeStartDate = tentativeStartDate;
    }

    public Date getTentativeEndDate() { return tentativeEndDate; }
    public void setTentativeEndDate(Date tentativeEndDate) {
        this.tentativeEndDate = tentativeEndDate;
    }

    public double getAssignmentCost() { return assignmentCost; }
    public void setAssignmentCost(double assignmentCost) {
        this.assignmentCost = assignmentCost;
    }

    public double getAdditionalCosts() { return additionalCosts; }
    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getTotalHours() { return totalHours; }
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public List<QuotationActivity> getActivities() { return activities; }
    public void setActivities(List<QuotationActivity> activities) {
        this.activities = activities;
    }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}