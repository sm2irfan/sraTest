package com.optus.infosec.api.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RiskDtoWrapper {
    private Integer totalCount;
    private List<RiskDTO> riskData = new ArrayList<>();
}
