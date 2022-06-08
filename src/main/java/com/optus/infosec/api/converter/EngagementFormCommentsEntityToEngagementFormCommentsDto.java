package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EngagementFormCommentsDTO;
import com.optus.infosec.domain.entity.EngagementFormCommentsEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author PL
 *
 * Converter Class EngagementFormCommentsEntity to EngagementFormCommentsDto
 *
 */

public class EngagementFormCommentsEntityToEngagementFormCommentsDto implements Converter<EngagementFormCommentsEntity, EngagementFormCommentsDTO> {

	@Override
	public EngagementFormCommentsDTO convert(EngagementFormCommentsEntity source) {

		EngagementFormCommentsDTO engagementFormCommentsDto = new EngagementFormCommentsDTO();
		engagementFormCommentsDto.setCommentId(source.getCommentId());
		engagementFormCommentsDto.setEngagementId(source.getEngagementId());
		engagementFormCommentsDto.setEngagementFormId(source.getEngagementFormId());
		engagementFormCommentsDto.setCommentBy(source.getCommentBy());
		engagementFormCommentsDto.setCommentItem(source.getCommentItem());
		engagementFormCommentsDto.setComment(source.getComment());
		engagementFormCommentsDto.setCommentDate(source.getCommentDate());
		engagementFormCommentsDto.setInfosecViewed(source.getInfosecViewed());
		engagementFormCommentsDto.setRequestorViewed(source.getRequestorViewed());
		return engagementFormCommentsDto;
	}

}
