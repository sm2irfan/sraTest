package com.optus.infosec.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author SM
 * <p>
 * Risk Entity
 */
@Entity
@Table(name = "risk")
public class RiskEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engagementId")
//    @JsonManagedReference
    private EngagementEntity engagementEntity;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskId;

    private String riskName;

    private String projectName;

    private String infosecResource;

    @Enumerated(EnumType.STRING)
    private Level likelihood;

    @Enumerated(EnumType.STRING)
    private Level impact;

    @Enumerated(EnumType.STRING)
    private Level riskLevel;

    private LocalDateTime raisedDatetime;

    private String issuesIdentified;

    private String mitigationAction;

    @Enumerated(EnumType.STRING)
    private Status riskStatus;

    private LocalDateTime completedDatetime;

    private String riskOwner;

    private LocalDateTime createdDatetime;

    private LocalDateTime modifiedDatetime;

    public EngagementEntity getEngagementEntity() {
        return engagementEntity;
    }

    public void setEngagementEntity(EngagementEntity engagementEntity) {
        this.engagementEntity = engagementEntity;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getInfosecResource() {
        return infosecResource;
    }

    public void setInfosecResource(String infosecResource) {
        this.infosecResource = infosecResource;
    }

    public Level getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(Level likelihood) {
        this.likelihood = likelihood;
    }

    public Level getImpact() {
        return impact;
    }

    public void setImpact(Level impact) {
        this.impact = impact;
    }

    public Level getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Level riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateTime getRaisedDatetime() {
        return raisedDatetime;
    }

    public void setRaisedDatetime(LocalDateTime raisedDatetime) {
        this.raisedDatetime = raisedDatetime;
    }

    public String getIssuesIdentified() {
        return issuesIdentified;
    }

    public void setIssuesIdentified(String issuesIdentified) {
        this.issuesIdentified = issuesIdentified;
    }

    public String getMitigationAction() {
        return mitigationAction;
    }

    public void setMitigationAction(String mitigationAction) {
        this.mitigationAction = mitigationAction;
    }

    public Status getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Status riskStatus) {
        this.riskStatus = riskStatus;
    }

    public LocalDateTime getCompletedDatetime() {
        return completedDatetime;
    }

    public void setCompletedDatetime(LocalDateTime completedDatetime) {
        this.completedDatetime = completedDatetime;
    }

    public String getRiskOwner() {
        return riskOwner;
    }

    public void setRiskOwner(String riskOwner) {
        this.riskOwner = riskOwner;
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

    @Override
    public String toString() {
        return "RiskEntity{" +
                ", riskId=" + riskId +
                ", riskName='" + riskName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", infosecResource='" + infosecResource + '\'' +
                ", likelihood=" + likelihood +
                ", impact=" + impact +
                ", riskLevel=" + riskLevel +
                ", raisedDatetime=" + raisedDatetime +
                ", issuesIdentified='" + issuesIdentified + '\'' +
                ", mitigationAction='" + mitigationAction + '\'' +
                ", riskStatus=" + riskStatus +
                ", completedDatetime=" + completedDatetime +
                ", riskOwner='" + riskOwner + '\'' +
                ", createdDatetime=" + createdDatetime +
                ", modifiedDatetime=" + modifiedDatetime +
                '}';
    }
}
