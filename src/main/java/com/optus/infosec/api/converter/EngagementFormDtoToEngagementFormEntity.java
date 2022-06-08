package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.domain.entity.EngagementFormEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author SM
 *
 * Converter Class EngagementFormDto to EngagementFormEntity
 *
 */
public class EngagementFormDtoToEngagementFormEntity implements Converter<EngagementFormDTO, EngagementFormEntity> {

    @Override
    public EngagementFormEntity convert(EngagementFormDTO source) {

        EngagementFormEntity engagementFormEntity = new EngagementFormEntity();

        engagementFormEntity.setEngagementFormId(source.getEngagementFormId());
        engagementFormEntity.setEngagementFormName(source.getEngagementFormName());
        engagementFormEntity.setFormData(source.getFormData()!=null?source.getFormData().getBytes():null);
        engagementFormEntity.setFormTemplate(source.getFormTemplate()!=null?source.getFormTemplate().getBytes():null);

        return engagementFormEntity;
    }
}