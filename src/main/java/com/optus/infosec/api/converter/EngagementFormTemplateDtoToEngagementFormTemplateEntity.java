package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormTemplateDTO;
import com.optus.infosec.domain.entity.EngagementFormTemplateEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * EngagementFormTemplateDTO To EngagementFormTemplateEntity Converter
 */
public class EngagementFormTemplateDtoToEngagementFormTemplateEntity implements Converter<EngagementFormTemplateDTO, EngagementFormTemplateEntity> {

    @Override
    public EngagementFormTemplateEntity convert(EngagementFormTemplateDTO source) {

        EngagementFormTemplateEntity engagementFormTemplateEntity = new EngagementFormTemplateEntity();

        engagementFormTemplateEntity.setTemplateFormName(source.getTemplateFormName());
        engagementFormTemplateEntity.setTemplateId(source.getTemplateId());
        engagementFormTemplateEntity.setTemplateVersion(source.getTemplateVersion());
        engagementFormTemplateEntity.setTemplate(source.getTemplate()!=null?source.getTemplate().getBytes():null);

        return engagementFormTemplateEntity;
    }
}
