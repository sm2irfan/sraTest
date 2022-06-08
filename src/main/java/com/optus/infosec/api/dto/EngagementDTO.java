package com.optus.infosec.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.optus.infosec.api.util.CustomLocalDateTimeDeserializer;
import com.optus.infosec.api.util.CustomLocalDateTimeSerializer;
import com.optus.infosec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author SM
 * <p>
 * Engagement Transfer Object
 */
@NoArgsConstructor
@AllArgsConstructor
public class EngagementDTO {

    private Long engagementId;

    private String projectName;

    //    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private Date engagementDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime requestedDatetime;

//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime completedDatetime;

    private String requestedBy;

    private String assignedTo;

    private Status engagementStatus;

    private Set<EngagementFormDTO> engagementFormSet = new HashSet<>();

    private Set<RiskDTO> riskSet = new HashSet<>();

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

    public Set<EngagementFormDTO> getEngagementFormSet() {
        return engagementFormSet;
    }

    public void setEngagementFormSet(Set<EngagementFormDTO> engagementFormSet) {
        this.engagementFormSet = engagementFormSet;
    }

    public Set<RiskDTO> getRiskSet() {
        return riskSet;
    }

    public void setRiskSet(Set<RiskDTO> riskSet) {
        this.riskSet = riskSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EngagementDTO that = (EngagementDTO) o;

        if (getEngagementId() != null ? !getEngagementId().equals(that.getEngagementId()) : that.getEngagementId() != null)
            return false;
        if (getProjectName() != null ? !getProjectName().equals(that.getProjectName()) : that.getProjectName() != null)
            return false;
        if (getEngagementDate() != null ? !getEngagementDate().equals(that.getEngagementDate()) : that.getEngagementDate() != null)
            return false;
        if (getRequestedDatetime() != null ? !getRequestedDatetime().equals(that.getRequestedDatetime()) : that.getRequestedDatetime() != null)
            return false;
        if (getCompletedDatetime() != null ? !getCompletedDatetime().equals(that.getCompletedDatetime()) : that.getCompletedDatetime() != null)
            return false;
        if (getRequestedBy() != null ? !getRequestedBy().equals(that.getRequestedBy()) : that.getRequestedBy() != null)
            return false;
        if (getAssignedTo() != null ? !getAssignedTo().equals(that.getAssignedTo()) : that.getAssignedTo() != null)
            return false;
        if (getEngagementStatus() != null ? !getEngagementStatus().equals(that.getEngagementStatus()) : that.getEngagementStatus() != null)
            return false;
        if (getEngagementFormSet() != null ? !getEngagementFormSet().equals(that.getEngagementFormSet()) : that.getEngagementFormSet() != null)
            return false;
        return !(getRiskSet() != null ? !getRiskSet().equals(that.getRiskSet()) : that.getRiskSet() != null);

    }

    @Override
    public int hashCode() {
        int result = getEngagementId() != null ? getEngagementId().hashCode() : 0;
        result = 31 * result + (getProjectName() != null ? getProjectName().hashCode() : 0);
        result = 31 * result + (getEngagementDate() != null ? getEngagementDate().hashCode() : 0);
        result = 31 * result + (getRequestedDatetime() != null ? getRequestedDatetime().hashCode() : 0);
        result = 31 * result + (getCompletedDatetime() != null ? getCompletedDatetime().hashCode() : 0);
        result = 31 * result + (getRequestedBy() != null ? getRequestedBy().hashCode() : 0);
        result = 31 * result + (getAssignedTo() != null ? getAssignedTo().hashCode() : 0);
        result = 31 * result + (getEngagementStatus() != null ? getEngagementStatus().hashCode() : 0);
        result = 31 * result + (getEngagementFormSet() != null ? getEngagementFormSet().hashCode() : 0);
        result = 31 * result + (getRiskSet() != null ? getRiskSet().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EngagementDTO{" +
                "engagementId=" + engagementId +
                ", projectName='" + projectName + '\'' +
                ", engagementDate='" + engagementDate + '\'' +
                ", requestedDatetime=" + requestedDatetime +
                ", completedDatetime=" + completedDatetime +
                ", requestedBy='" + requestedBy + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", engagementStatus=" + engagementStatus +
                ", engagementFormSet=" + engagementFormSet +
                ", riskSet=" + riskSet +
                '}';
    }
}
