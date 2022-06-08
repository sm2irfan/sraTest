package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormTemplateDTO;
import com.optus.infosec.domain.entity.EngagementFormTemplateEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * EngagementFormTemplateEntity To EngagementFormTemplateDTO Converter
 */
public class EngagementFormTemplateEntityToEngagementFormTemplateDto  implements Converter<EngagementFormTemplateEntity, EngagementFormTemplateDTO> {

    @Override
    public EngagementFormTemplateDTO convert(EngagementFormTemplateEntity source) {

        EngagementFormTemplateDTO engagementFormTemplateDTO = new EngagementFormTemplateDTO();

        engagementFormTemplateDTO.setTemplateFormName(source.getTemplateFormName());
        engagementFormTemplateDTO.setTemplateId(source.getTemplateId());
        engagementFormTemplateDTO.setTemplateVersion(source.getTemplateVersion());
        engagementFormTemplateDTO.setTemplate(source.getTemplate()!=null?new String(source.getTemplate()):null);

        return engagementFormTemplateDTO;
    }
}
