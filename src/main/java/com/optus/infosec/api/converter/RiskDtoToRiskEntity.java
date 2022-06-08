package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.domain.entity.RiskEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * Converter Class RiskDto to RiskEntity
 *
 */
public class RiskDtoToRiskEntity implements Converter<RiskDTO, RiskEntity> {


    @Override
    public RiskEntity convert(RiskDTO source) {

        RiskEntity riskEntity = new RiskEntity();

        riskEntity.setRiskId(source.getRiskId());
        riskEntity.setRiskName(source.getRiskName());
        riskEntity.setRiskOwner(source.getRiskOwner());
        riskEntity.setRiskLevel(source.getRiskLevel());
        riskEntity.setRiskStatus(source.getRiskStatus());
        riskEntity.setRaisedDatetime(source.getRaisedDatetime());
        riskEntity.setProjectName(source.getProjectName());
        riskEntity.setCompletedDatetime(source.getCompletedDatetime());
        riskEntity.setImpact(source.getImpact());
        riskEntity.setInfosecResource(source.getInfosecResource());
        riskEntity.setIssuesIdentified(source.getIssuesIdentified());
        riskEntity.setLikelihood(source.getLikelihood());
        riskEntity.setMitigationAction(source.getMitigationAction());

        return riskEntity;
    }
}
