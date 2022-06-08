package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.entity.EngagementFormEntity;
import com.optus.infosec.domain.entity.RiskEntity;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author SM
 *
 * Converter Class EngagementDTO to EngagementEntity
 *
 */
public class EngagementDtoToEngagementEntity implements Converter<EngagementDTO, EngagementEntity>{

    @Override
    public EngagementEntity convert(EngagementDTO source) {

        EngagementEntity engagementEntity = new EngagementEntity();

        engagementEntity.setEngagementId(source.getEngagementId());
        engagementEntity.setProjectName(source.getProjectName());
        engagementEntity.setEngagementDate(source.getEngagementDate());
        engagementEntity.setRequestedDatetime(source.getRequestedDatetime());
        engagementEntity.setCompletedDatetime(source.getCompletedDatetime());
        engagementEntity.setRequestedBy(source.getRequestedBy());
        engagementEntity.setAssignedTo(source.getAssignedTo());
        engagementEntity.setEngagementStatus(source.getEngagementStatus());

        // convert engagement form set
        Set<EngagementFormEntity> engagementFormEntitySet = new HashSet<>();
        EngagementFormDtoToEngagementFormEntity converterEngagementForm = new EngagementFormDtoToEngagementFormEntity();
        source.getEngagementFormSet().stream().forEach(engagementForm -> engagementFormEntitySet.add(converterEngagementForm.convert(engagementForm)));
        engagementEntity.setEngagementFormEntitySet(engagementFormEntitySet);


        // convert risk form set
        Set<RiskEntity> riskEntitySet = new HashSet<>();
        RiskDtoToRiskEntity convertRisk = new RiskDtoToRiskEntity();
        source.getRiskSet().stream().forEach(risk -> riskEntitySet.add(convertRisk.convert(risk)));
        engagementEntity.setRiskEntitySet(riskEntitySet);

        return engagementEntity;
    }
}