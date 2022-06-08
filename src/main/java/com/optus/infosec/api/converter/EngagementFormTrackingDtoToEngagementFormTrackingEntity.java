package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormTrackingDTO;
import com.optus.infosec.domain.entity.EngagementFormTrackingEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author PL
 *
 * Converter Class EngagementFormTrackingDto to EngagementFormTrackingEntity
 *
 */

public class EngagementFormTrackingDtoToEngagementFormTrackingEntity implements Converter<EngagementFormTrackingDTO, EngagementFormTrackingEntity> {
	
	@Override
	public EngagementFormTrackingEntity convert(EngagementFormTrackingDTO source) {
		
		EngagementFormTrackingEntity engagementFormTrackingEntity = new EngagementFormTrackingEntity();
		engagementFormTrackingEntity.setEngagementId(source.getEngagementId());
		engagementFormTrackingEntity.setEngagementFormId(source.getEngagementFormId());
		engagementFormTrackingEntity.setTrackingUser(source.getTrackingUser());
		engagementFormTrackingEntity.setTrackingItem(source.getTrackingItem());
		engagementFormTrackingEntity.setTrackingField(source.getTrackingField());
		engagementFormTrackingEntity.setTrackingDate(source.getTrackingDate());
		engagementFormTrackingEntity.setOldValue(source.getOldValue());
		engagementFormTrackingEntity.setNewValue(source.getNewValue());
		return engagementFormTrackingEntity;
	}

}
