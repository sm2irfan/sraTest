package com.optus.infosec.repositories;

import com.optus.infosec.domain.enums.Status;

import java.time.LocalDateTime;

public interface RiskStatusDateProjection {
    Status getRiskStatus();
    LocalDateTime getCompletedDatetime();
}
