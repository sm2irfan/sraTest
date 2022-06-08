package com.optus.infosec.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RisksByTemporal {
    String temporalType;
    Integer deviationCount;
    Integer mitigatedCount;
    Integer openCount;
    Integer closedAmount;
}
