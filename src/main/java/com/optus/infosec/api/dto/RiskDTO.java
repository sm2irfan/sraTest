package com.optus.infosec.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.optus.infosec.api.util.CustomLocalDateTimeDeserializer;
import com.optus.infosec.api.util.CustomLocalDateTimeSerializer;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author SM
 *
 * Risk Data Transfer Object
 */
@AllArgsConstructor
@NoArgsConstructor
public class RiskDTO {

    private Long engagementId;

    private Long riskId;

    private String riskName;

    private String projectName;

    private String infosecResource;

    private Level likelihood;

    private Level impact;

    private Level riskLevel;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime raisedDatetime;

    private String issuesIdentified;

    private String mitigationAction;

    private Status riskStatus;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime completedDatetime;

    private String riskOwner;

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

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RiskDTO riskDTO = (RiskDTO) o;

        if (getEngagementId() != null ? !getEngagementId().equals(riskDTO.getEngagementId()) : riskDTO.getEngagementId() != null)
            return false;
        if (getRiskId() != null ? !getRiskId().equals(riskDTO.getRiskId()) : riskDTO.getRiskId() != null) return false;
        if (getRiskName() != null ? !getRiskName().equals(riskDTO.getRiskName()) : riskDTO.getRiskName() != null)
            return false;
        if (getProjectName() != null ? !getProjectName().equals(riskDTO.getProjectName()) : riskDTO.getProjectName() != null)
            return false;
        if (getInfosecResource() != null ? !getInfosecResource().equals(riskDTO.getInfosecResource()) : riskDTO.getInfosecResource() != null)
            return false;
        if (getLikelihood() != riskDTO.getLikelihood()) return false;
        if (getImpact() != riskDTO.getImpact()) return false;
        if (getRiskLevel() != riskDTO.getRiskLevel()) return false;
        if (getRaisedDatetime() != null ? !getRaisedDatetime().equals(riskDTO.getRaisedDatetime()) : riskDTO.getRaisedDatetime() != null)
            return false;
        if (getIssuesIdentified() != null ? !getIssuesIdentified().equals(riskDTO.getIssuesIdentified()) : riskDTO.getIssuesIdentified() != null)
            return false;
        if (getMitigationAction() != null ? !getMitigationAction().equals(riskDTO.getMitigationAction()) : riskDTO.getMitigationAction() != null)
            return false;
        if (getRiskStatus() != riskDTO.getRiskStatus()) return false;
        if (getCompletedDatetime() != null ? !getCompletedDatetime().equals(riskDTO.getCompletedDatetime()) : riskDTO.getCompletedDatetime() != null)
            return false;
        return !(getRiskOwner() != null ? !getRiskOwner().equals(riskDTO.getRiskOwner()) : riskDTO.getRiskOwner() != null);

    }

    @Override
    public int hashCode() {
        int result = getRiskId() != null ? getRiskId().hashCode() : 0;
        result = 31 * result + (getRiskName() != null ? getRiskName().hashCode() : 0);
        result = 31 * result + (getProjectName() != null ? getProjectName().hashCode() : 0);
        result = 31 * result + (getInfosecResource() != null ? getInfosecResource().hashCode() : 0);
        result = 31 * result + (getLikelihood() != null ? getLikelihood().hashCode() : 0);
        result = 31 * result + (getImpact() != null ? getImpact().hashCode() : 0);
        result = 31 * result + (getRiskLevel() != null ? getRiskLevel().hashCode() : 0);
        result = 31 * result + (getRaisedDatetime() != null ? getRaisedDatetime().hashCode() : 0);
        result = 31 * result + (getIssuesIdentified() != null ? getIssuesIdentified().hashCode() : 0);
        result = 31 * result + (getMitigationAction() != null ? getMitigationAction().hashCode() : 0);
        result = 31 * result + (getRiskStatus() != null ? getRiskStatus().hashCode() : 0);
        result = 31 * result + (getCompletedDatetime() != null ? getCompletedDatetime().hashCode() : 0);
        result = 31 * result + (getRiskOwner() != null ? getRiskOwner().hashCode() : 0);
        result = 31 * result + (getEngagementId() != null ? getEngagementId().hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "RiskDTO{" +
                "riskId=" + riskId +
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
                ", engagementId='" + engagementId + '\'' +
                '}';
    }
}
