package com.optus.infosec.domain.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.optus.infosec.domain.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * @author SM
 * <p>
 * Engagement Entity
 */
@Entity
@Table(name = "engagement")
public class EngagementEntity {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long engagementId;

    private String projectName;

    private Date engagementDate;

    private LocalDateTime requestedDatetime;

    private LocalDateTime completedDatetime;

    private String requestedBy;

    private String assignedTo;

    @Column(name = "assigned_to_name")
    private String assignedToName;

    @Enumerated(EnumType.STRING)
    private Status engagementStatus;

    private LocalDateTime createdDatetime;

    private LocalDateTime modifiedDatetime;

    private String requestedByName;

    //@OneToMany(mappedBy="engagementEntity", cascade = CascadeType.PERSIST)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "engagementId")
    private Set<EngagementFormEntity> engagementFormEntitySet;

    @OneToMany(mappedBy = "engagementEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private Set<RiskEntity> riskEntitySet;


    public EngagementEntity() {
    }

    public EngagementEntity(Long engagementId, String projectName, Date engagementDate, LocalDateTime requestedDatetime, LocalDateTime completedDatetime, String requestedBy,
                            String assignedTo, Status engagementStatus, LocalDateTime createdDatetime, LocalDateTime modifiedDatetime,
                            Set<EngagementFormEntity> engagementFormEntitySet, Set<RiskEntity> riskEntitySet) {
        this.engagementId = engagementId;
        this.projectName = projectName;
        this.engagementDate = engagementDate;
        this.requestedDatetime = requestedDatetime;
        this.completedDatetime = completedDatetime;
        this.requestedBy = requestedBy;
        this.assignedTo = assignedTo;
        this.engagementStatus = engagementStatus;
        this.createdDatetime = createdDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.engagementFormEntitySet = engagementFormEntitySet;
        this.riskEntitySet = riskEntitySet;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getEngagementDate() {
        return engagementDate;
    }

    public void setEngagementDate(Date engagementDate) {
        this.engagementDate = engagementDate;
    }

    public LocalDateTime getRequestedDatetime() {
        return requestedDatetime;
    }

    public void setRequestedDatetime(LocalDateTime requestedDatetime) {
        this.requestedDatetime = requestedDatetime;
    }

    public LocalDateTime getCompletedDatetime() {
        return completedDatetime;
    }

    public void setCompletedDatetime(LocalDateTime completedDatetime) {
        this.completedDatetime = completedDatetime;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Status getEngagementStatus() {
        return engagementStatus;
    }

    public void setEngagementStatus(Status engagementStatus) {
        this.engagementStatus = engagementStatus;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public LocalDateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(LocalDateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public Set<EngagementFormEntity> getEngagementFormEntitySet() {
        return engagementFormEntitySet;
    }

    public void setEngagementFormEntitySet(Set<EngagementFormEntity> engagementFormEntitySet) {
        this.engagementFormEntitySet = engagementFormEntitySet;
    }

    public Set<RiskEntity> getRiskEntitySet() {
        return riskEntitySet;
    }

    public void setRiskEntitySet(Set<RiskEntity> riskEntitySet) {
        this.riskEntitySet = riskEntitySet;
    }

    @Override
    public String toString() {
        return "EngagementEntity{" +
                "engagementId='" + engagementId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", engagementDate='" + engagementDate + '\'' +
                ", requestedDatetime=" + requestedDatetime +
                ", completedDatetime=" + completedDatetime +
                ", requestedBy='" + requestedBy + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", engagementStatus='" + engagementStatus + '\'' +
                ", createdDatetime=" + createdDatetime +
                ", modifiedDatetime=" + modifiedDatetime +
                // ", engagementFormEntitySet=" + engagementFormEntitySet +
                ", riskEntitySet=" + riskEntitySet +
                '}';
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }
}
