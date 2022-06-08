package com.optus.infosec.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EngagementsByTemporal {
    private String temporalType;
    private Integer newCount;
    private Integer openedCount;
    private Integer closeCount;
    private Integer canceledCount;
    private Integer onHoldCount;
}
