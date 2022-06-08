package com.optus.infosec.api.service;


import com.optus.infosec.api.dto.EngagementFormCommentsDTO;
import com.optus.infosec.api.dto.EngagementFormTrackingDTO;
import com.optus.infosec.api.service.repository.CommentsTrackingRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PL
 *
 * Service Class for Engagement Comments And Tracking
 *
 */
@Service
public class CommentsTrackingService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentsTrackingService.class);

    @Autowired
    private CommentsTrackingRepositoryService commentsTrackingRepositoryService;

    /**
     * Creates a new comment 
     *
     * @param engagementFormCommentDTO
     */
    public void createComment(EngagementFormCommentsDTO engagementFormCommentsDTO){
    	commentsTrackingRepositoryService.createComment(engagementFormCommentsDTO);
    }

    /**
     * Get Engagement Form
     *
     * @param engagementId
     * @return EngagementFormCommentsEntity
     */
    public List<EngagementFormCommentsDTO> getComments(Long engagementId, Long engagementFormId){
        return commentsTrackingRepositoryService.getComments(engagementId, engagementFormId);
    }
    
    /**
     * update comment viewed
     *
     * @param engagementId
     * @param engagementFormId
     * @param commentItem
     * @param isInfosecUser
     */
    public void updateCommentViewed(Long engagementId, Long engagementFormId, String commentItem, String isInfosecUser){
    	commentsTrackingRepositoryService.updateCommentViewed(engagementId, engagementFormId, commentItem, isInfosecUser);
    }
    
    /**
     * Creates a new tracking 
     *
     * @param engagementFormTrackingDTO
     */
    public void createTracking(List<EngagementFormTrackingDTO> engagementFormTrackingDTOList){
    	commentsTrackingRepositoryService.createTracking(engagementFormTrackingDTOList);
    }

    /**
     * Get Engagement Form
     *
     * @param engagementId
     * @return EngagementFormTrackingEntity
     */
    public List<EngagementFormTrackingDTO> getTracking(Long engagementId, Long engagementFormId){
        return commentsTrackingRepositoryService.getTracking(engagementId, engagementFormId);
    }

}
