package com.optus.infosec.api.service.repository;

import com.optus.infosec.api.converter.ConversionService;
import com.optus.infosec.api.dto.EngagementFormCommentsDTO;
import com.optus.infosec.api.dto.EngagementFormTrackingDTO;
import com.optus.infosec.repositories.EngagementFormCommentsEntityRepository;
import com.optus.infosec.repositories.EngagementFormTrackingEntityRepository;
import com.optus.infosec.domain.entity.EngagementFormCommentsEntity;
import com.optus.infosec.domain.entity.EngagementFormTrackingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PL
 * <p>
 * Repository Service Class for Comments And Tracking
 */
@Service
public class CommentsTrackingRepositoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentsTrackingRepositoryService.class);

    @Autowired
    private EngagementFormCommentsEntityRepository engagementFormCommentsEntityRepository;

    @Autowired
    private EngagementFormTrackingEntityRepository engagementFormTrackingEntityRepository;

    @Autowired
    private ConversionService conversionService;

    /**
     * Creates a new Engagement Form comment
     *
     * @param engagementFormCommentsDTO
     */
    public void createComment(EngagementFormCommentsDTO engagementFormCommentsDTO) {

        EngagementFormCommentsEntity engagementFormCommentsEntity = conversionService.convert(engagementFormCommentsDTO, EngagementFormCommentsEntity.class);
        LOG.debug("Creating Comment Form Entity : {} ", engagementFormCommentsEntity);
        EngagementFormCommentsEntity createdEngagementFormCommentsEntity = engagementFormCommentsEntityRepository.save(engagementFormCommentsEntity);
        LOG.debug("Engagement Form Entity Created Successfully : {}", createdEngagementFormCommentsEntity);
    }

    /**
     * Get Engagement Form Comments
     *
     * @param engagementId
     * @param engagementFormId
     * @return EngagementFormCommentsDTO
     */
    public List<EngagementFormCommentsDTO> getComments(Long engagementId, Long engagementFormId) {

        LOG.info("Get Engagement Form Comments Entity For Engagement Id : {} And Engagement Form Id: {}", engagementId, engagementFormId);

        List<EngagementFormCommentsEntity> engagementFormCommentsEntityList = engagementFormCommentsEntityRepository.findByEngagementIdAndEngagementFormId(engagementId, engagementFormId);
        LOG.info("Returned Engagement Form Comments Entities: {}", engagementFormCommentsEntityList);
        List<EngagementFormCommentsDTO> engagementFormCommentsDTOList = new ArrayList<>();
        engagementFormCommentsEntityList.forEach(engagementFormCommentsEntity ->
                engagementFormCommentsDTOList.add(conversionService.convert(engagementFormCommentsEntity, EngagementFormCommentsDTO.class)));
        return engagementFormCommentsDTOList;
    }

    /**
     * Update comments viewed
     *
     * @param engagementId
     * @param engagementFormId
     * @param commentItem
     * @param isInfosecUser
     */
    public void updateCommentViewed(Long engagementId, Long engagementFormId, String commentItem, String isInfosecUser) {

        if (isInfosecUser != null && isInfosecUser.equals("Y")) {
            engagementFormCommentsEntityRepository.updateCommentViewedByInfosec(engagementId, engagementFormId, commentItem);
        } else {
            engagementFormCommentsEntityRepository.updateCommentViewedByRequestor(engagementId, engagementFormId, commentItem);
        }
    }

    /**
     * Creates a new Engagement Form tracking
     */
    public void createTracking(List<EngagementFormTrackingDTO> engagementFormTrackingDTOList) {

        List<EngagementFormTrackingEntity> engagementFormTrackingEntityList = new ArrayList<>();
        engagementFormTrackingDTOList.forEach(engagementFormTrackingDTO -> engagementFormTrackingEntityList.add(conversionService.convert(engagementFormTrackingDTO, EngagementFormTrackingEntity.class)));
        LOG.debug("Creating Tracking Form Entity : {} ", engagementFormTrackingEntityList);
        List<EngagementFormTrackingEntity> createdEngagementFormTrackingEntity = engagementFormTrackingEntityRepository.saveAll(engagementFormTrackingEntityList);
        LOG.debug("Engagement Form Entity Created Successfully : {}", createdEngagementFormTrackingEntity);
    }

    /**
     * Get Engagement Form Tracking
     *
     * @param engagementId
     * @param engagementFormId
     * @return EngagementFormTrackingDTO
     */
    public List<EngagementFormTrackingDTO> getTracking(Long engagementId, Long engagementFormId) {

        LOG.debug("Get Engagement Form Tracking Entity For Engagement Id : {} And Engagement Form Id: {}", engagementId, engagementFormId);

        List<EngagementFormTrackingEntity> engagementFormTrackingEntityList = engagementFormTrackingEntityRepository.findByEngagementIdAndEngagementFormId(engagementId, engagementFormId);
        LOG.debug("Returned Engagement Form Tracking Entities: {}", engagementFormTrackingEntityList);
        List<EngagementFormTrackingDTO> engagementFormTrackingDTOList = new ArrayList<>();
        engagementFormTrackingEntityList.forEach(engagementFormTrackingEntity -> engagementFormTrackingDTOList.add(conversionService.convert(engagementFormTrackingEntity, EngagementFormTrackingDTO.class)));
        return engagementFormTrackingDTOList;
    }


}
