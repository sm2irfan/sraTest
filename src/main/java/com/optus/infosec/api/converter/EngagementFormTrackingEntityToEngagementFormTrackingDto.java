package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormTrackingDTO;
import com.optus.infosec.domain.entity.EngagementFormTrackingEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author PL
 *
 * Converter Class EngagementFormTrackingEntity to EngagementFormTrackingDto
 *
 */

public class EngagementFormTrackingEntityToEngagementFormTrackingDto implements Converter<EngagementFormTrackingEntity, EngagementFormTrackingDTO> {

	@Override
	public EngagementFormTrackingDTO convert(EngagementFormTrackingEntity source) {
		
		EngagementFormTrackingDTO engagementFormTrackingDto = new EngagementFormTrackingDTO();
		engagementFormTrackingDto.setEngagementId(source.getEngagementId());
		engagementFormTrackingDto.setEngagementFormId(source.getEngagementFormId());
		engagementFormTrackingDto.setTrackingUser(source.getTrackingUser());
		engagementFormTrackingDto.setTrackingItem(source.getTrackingItem());
		engagementFormTrackingDto.setTrackingField(source.getTrackingField());
		engagementFormTrackingDto.setTrackingDate(source.getTrackingDate());
		engagementFormTrackingDto.setOldValue(source.getOldValue());
		engagementFormTrackingDto.setNewValue(source.getNewValue());
		return engagementFormTrackingDto;
	}

}
