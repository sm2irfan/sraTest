package com.optus.infosec.api.dto;

import com.optus.infosec.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class EngagementSearchListDto {

    private Long engagementId;

    private String projectName;

    private LocalDateTime requestedDatetime;
    private LocalDateTime completedDatetime;

    private String requestedBy;

    private String assignedTo;

    private Status engagementStatus;

    private Set<RiskDTO> riskSet = new HashSet<>();

    public EngagementSearchListDto(Long engagementId, String projectName, LocalDateTime requestedDatetime, LocalDateTime completedDatetime, String requestedBy,
                                   String assignedTo, Status engagementStatus, Set<RiskDTO> riskSet) {
        this.engagementId = engagementId;
        this.projectName = projectName;
        this.requestedDatetime = requestedDatetime;
        this.completedDatetime = completedDatetime;
        this.requestedBy = requestedBy;
        this.assignedTo = assignedTo;
        this.engagementStatus = engagementStatus;
        this.riskSet = riskSet;
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

    public Set<RiskDTO> getRiskEntitySet() {
        return riskSet;
    }

    public void setRiskSet(Set<RiskDTO> riskSet) {
        this.riskSet = riskSet;
    }

    @Override
    public String toString() {
        return "EngagementSearchListDto{" +
                "engagementId=" + engagementId +
                ", projectName='" + projectName + '\'' +
                ", requestedDatetime=" + requestedDatetime +
                ", completedDatetime=" + completedDatetime +
                ", requestedBy='" + requestedBy + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", engagementStatus=" + engagementStatus +
                ", riskSet=" + riskSet +
                '}';
    }
}
