package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;

import java.time.LocalDateTime;

public interface RiskProjection {
    public Long getRiskId();

    public String getRiskName();

    public String getProjectName();

    public String getInfosecResource();

    public Level getLikelihood();

    public Level getImpact();

    public Level getRiskLevel();

    public LocalDateTime getRaisedDatetime();

    public String getIssuesIdentified();

    public String getMitigationAction();

    public Status getRiskStatus();

    public LocalDateTime getCompletedDatetime();

    public String getRiskOwner();

    public EngagementEntity getEngagementEntity();
}
