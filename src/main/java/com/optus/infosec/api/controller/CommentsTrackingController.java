package com.optus.infosec.api.controller;

import com.optus.infosec.api.dto.EngagementFormCommentsDTO;
import com.optus.infosec.api.dto.EngagementFormTrackingDTO;
import com.optus.infosec.api.service.CommentsTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author PL
 * <p>
 * Controller for Comments and Tracking
 */
@RestController
@RequestMapping("/commentstracking")
public class CommentsTrackingController {

    private static final Logger LOG = LoggerFactory.getLogger(CommentsTrackingController.class);

    @Autowired
    private CommentsTrackingService commentsTrackingService;


    /**
     * Get Operation for Comments
     *
     * @return ResponseEntity<EngagementFormCommentsEntity>
     */
    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EngagementFormCommentsDTO>> getComments(@RequestParam() Long engagementId,
                                                                       @RequestParam() Long engagementFormId) {

        LOG.info("Get All Comments: engagementId : {}, engagementFormId: {} ", engagementId, engagementFormId);
        List<EngagementFormCommentsDTO> engagementFormCommentsDTOList = commentsTrackingService.getComments(engagementId, engagementFormId);
        LOG.info("Number of comments: {}", engagementFormCommentsDTOList.size());
        return new ResponseEntity<>(engagementFormCommentsDTOList, HttpStatus.OK);
    }

    /**
     * Post Operation for Comments
     *
     * @param engagementFormCommentsDTO
     * @return ResponseEntity<Void>
     */
    @PostMapping(value = "/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EngagementFormCommentsDTO>> createComments(@RequestBody EngagementFormCommentsDTO engagementFormCommentsDTO) {

        LOG.debug("engagementFormCommentst DTO : {}", engagementFormCommentsDTO);
        commentsTrackingService.createComment(engagementFormCommentsDTO);
        List<EngagementFormCommentsDTO> engagementFormCommentsDTOList = commentsTrackingService.getComments(engagementFormCommentsDTO.getEngagementId(),
                engagementFormCommentsDTO.getEngagementFormId());
        return new ResponseEntity<>(engagementFormCommentsDTOList, HttpStatus.OK);
    }

    /**
     * Put Operation for Comments
     *
     * @param engagementId
     * @param engagementFormId
     * @param commentItem
     * @param isInfosecUser
     */
    @PutMapping(value = "/updateCommentViewed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCommentViewed(@RequestParam(required = true) Long engagementId,
                                                      @RequestParam(required = true) Long engagementFormId,
                                                      @RequestParam(required = true) String commentItem,
                                                      @RequestParam(required = true) String isInfosecUser) {

        LOG.debug("commentViewed engagementId: {}, engagementFormId {}, commentItem {}, isInfosecUser {}", engagementId, engagementFormId, commentItem, isInfosecUser);
        commentsTrackingService.updateCommentViewed(engagementId, engagementFormId, commentItem, isInfosecUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Get Operation for Tracking
     *
     * @return ResponseEntity<EngagementFormTrackingEntity>
     */
    @GetMapping(value = "/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EngagementFormTrackingDTO>> getTracking(@RequestParam(required = true) Long engagementId,
                                                                       @RequestParam(required = true) Long engagementFormId) {

        LOG.debug("Get All Tracking: engagementId : {}, engagementFormId: {} ", engagementId, engagementFormId);
        List<EngagementFormTrackingDTO> engagementFormTrackingDTOList = commentsTrackingService.getTracking(engagementId, engagementFormId);
        return new ResponseEntity<>(engagementFormTrackingDTOList, HttpStatus.OK);
    }

    /**
     * Post Operation for Tracking
     *
     * @param engagementFormTrackingDTOList
     * @return ResponseEntity<Void>
     */
    @PostMapping(value = "/tracking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EngagementFormTrackingDTO>> createTracking(@RequestBody List<EngagementFormTrackingDTO> engagementFormTrackingDTOList) {

        LOG.debug("engagementFormTrackingt DTO : {}", engagementFormTrackingDTOList);
        if (engagementFormTrackingDTOList != null && engagementFormTrackingDTOList.size() > 0) {
            commentsTrackingService.createTracking(engagementFormTrackingDTOList);
            List<EngagementFormTrackingDTO> engagementFormTrackingDTOListAll = commentsTrackingService.getTracking(engagementFormTrackingDTOList.get(0).getEngagementId(), engagementFormTrackingDTOList.get(0).getEngagementFormId());
            return new ResponseEntity<>(engagementFormTrackingDTOListAll, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
