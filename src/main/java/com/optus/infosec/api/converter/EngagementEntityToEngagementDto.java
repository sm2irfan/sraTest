package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.domain.entity.EngagementEntity;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author SM
 * <p>
 * Converter Class EngagementEntity to EngagementDTO
 */
public class EngagementEntityToEngagementDto implements Converter<EngagementEntity, EngagementDTO> {

    @Override
    public EngagementDTO convert(EngagementEntity engagementEntity) {

        EngagementDTO engagementDTO = new EngagementDTO();

        engagementDTO.setEngagementId(engagementEntity.getEngagementId());
        engagementDTO.setProjectName(engagementEntity.getProjectName());
        engagementDTO.setEngagementDate(engagementEntity.getEngagementDate());
        engagementDTO.setRequestedDatetime(engagementEntity.getRequestedDatetime());
        engagementDTO.setCompletedDatetime(engagementEntity.getCompletedDatetime());
        engagementDTO.setRequestedBy(engagementEntity.getRequestedBy());
        engagementDTO.setAssignedTo(engagementEntity.getAssignedTo());
        engagementDTO.setEngagementStatus(engagementEntity.getEngagementStatus());

        /*EmployeeEntity employeeEntity = employeeService.findByEmployeeId(source.getRequestedBy());
        if(null!=employeeEntity) {
            engagementDTO.setRequestedBy(employeeEntity.getFirstName() + " " + employeeEntity.getLastName());
        }

        InfosecUsersEntity infosecUsersEntity = infosecUsersService.findByEmployeeId(source.getAssignedTo());
        if(null!=infosecUsersEntity) {
            engagementDTO.setAssignedTo(infosecUsersEntity.getFirstName() + " " + infosecUsersEntity.getLastName());
        }*/

        // convert engagement form set
        Set<EngagementFormDTO> engagementFormSet = new HashSet<>();

        EngagementFormEntityToEngagementFormDto converter = new EngagementFormEntityToEngagementFormDto();

        engagementEntity.getEngagementFormEntitySet().forEach(engagementForm ->
                {
                    engagementFormSet.add(converter.convert(engagementForm));
                }
        );

        engagementDTO.setEngagementFormSet(engagementFormSet);

        // convert risk form set
        Set<RiskDTO> riskSet = new HashSet<>();

        RiskEntityToRiskDto convertRisk = new RiskEntityToRiskDto();
        engagementEntity.getRiskEntitySet().forEach(risk ->
                {
                    riskSet.add(convertRisk.convert(risk));
                }
        );
        engagementDTO.setRiskSet(riskSet);

        return engagementDTO;
    }
}
