package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormCommentsDTO;
import com.optus.infosec.domain.entity.EngagementFormCommentsEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author PL
 *
 * Converter Class EngagementFormCommentsDto to EngagementFormCommentsEntity
 *
 */

public class EngagementFormCommentsDtoToEngagementFormCommentsEntity implements Converter<EngagementFormCommentsDTO, EngagementFormCommentsEntity> {

	@Override
	public EngagementFormCommentsEntity convert(EngagementFormCommentsDTO source) {
		
		EngagementFormCommentsEntity engagementFormCommentsEntity = new EngagementFormCommentsEntity();
		engagementFormCommentsEntity.setEngagementId(source.getEngagementId());
		engagementFormCommentsEntity.setEngagementFormId(source.getEngagementFormId());
		engagementFormCommentsEntity.setCommentBy(source.getCommentBy());
		engagementFormCommentsEntity.setCommentItem(source.getCommentItem());
		engagementFormCommentsEntity.setComment(source.getComment());
		engagementFormCommentsEntity.setCommentDate(source.getCommentDate());
		engagementFormCommentsEntity.setInfosecViewed(source.getInfosecViewed());
		engagementFormCommentsEntity.setRequestorViewed(source.getRequestorViewed());
		return engagementFormCommentsEntity;
	}

}
