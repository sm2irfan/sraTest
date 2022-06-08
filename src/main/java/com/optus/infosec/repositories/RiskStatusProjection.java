package com.optus.infosec.repositories;

import com.optus.infosec.domain.enums.Status;

public interface RiskStatusProjection {
    Status getRiskStatus();
}
