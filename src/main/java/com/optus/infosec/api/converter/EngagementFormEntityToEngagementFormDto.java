package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.domain.entity.EngagementFormEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * Converter Class EngagementFormEntity to EngagementFormDto
 *
 */
public class EngagementFormEntityToEngagementFormDto implements Converter<EngagementFormEntity, EngagementFormDTO> {

    @Override
    public EngagementFormDTO convert(EngagementFormEntity source) {

        EngagementFormDTO engagementFormDTO = new EngagementFormDTO();

        engagementFormDTO.setEngagementFormId(source.getEngagementFormId());
        engagementFormDTO.setEngagementFormName(source.getEngagementFormName());
        engagementFormDTO.setFormData(source.getFormData() != null ? new String(source.getFormData()):null);
        engagementFormDTO.setFormTemplate(source.getFormTemplate()!=null?new String(source.getFormTemplate()):null);

        return engagementFormDTO;
    }
}