package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.domain.entity.RiskEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * Converts Risk Entity to Risk DTO
 */
public class RiskEntityToRiskDto  implements Converter<RiskEntity, RiskDTO> {

    @Override
    public RiskDTO convert(RiskEntity source) {

        RiskDTO riskDTO = new RiskDTO();

        riskDTO.setEngagementId(source.getEngagementEntity().getEngagementId());
        riskDTO.setRiskId(source.getRiskId());
        riskDTO.setRiskName(source.getRiskName());
        riskDTO.setRiskOwner(source.getRiskOwner());
        riskDTO.setRiskLevel(source.getRiskLevel());
        riskDTO.setRiskStatus(source.getRiskStatus());
        riskDTO.setRaisedDatetime(source.getRaisedDatetime());
        riskDTO.setProjectName(source.getProjectName());
        riskDTO.setCompletedDatetime(source.getCompletedDatetime());
        riskDTO.setImpact(source.getImpact());
        riskDTO.setInfosecResource(source.getInfosecResource());
        riskDTO.setIssuesIdentified(source.getIssuesIdentified());
        riskDTO.setLikelihood(source.getLikelihood());
        riskDTO.setMitigationAction(source.getMitigationAction());

        return riskDTO;

    }
}