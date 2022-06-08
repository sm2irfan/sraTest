package com.optus.infosec.repositories;

import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.Set;

public interface EngagementSearchListProjection {

    Long getEngagementId();

    String getProjectName();

    LocalDateTime getRequestedDatetime();

    LocalDateTime getCompletedDatetime();

    String getRequestedBy();

    String getAssignedTo();

    String getAssignedToName();

    Status getEngagementStatus();

    Set<RiskProjection> getRiskEntitySet();

}


