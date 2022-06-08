package com.optus.infosec.repositories;


import com.optus.infosec.domain.entity.EngagementFormCommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author PL
 *
 * Engagement Form Comments Entity Repository
 */
public interface EngagementFormCommentsEntityRepository extends JpaRepository<EngagementFormCommentsEntity, Long> {

    /**
     * Find Engagement Form Comments
     *
     * @param engagementId
     * @param engagementFormId
     * @return List<EngagementFormCommentsEntity>
     */
    @Query("SELECT cmnts FROM EngagementFormCommentsEntity cmnts WHERE (cmnts.engagementId = :engagementId)"+
           "AND (cmnts.engagementFormId = :engagementFormId)"+
           "ORDER BY cmnts.commentDate"
    )
    List<EngagementFormCommentsEntity> findByEngagementIdAndEngagementFormId(
            @Param("engagementId") Long engagementId,
            @Param("engagementFormId") Long engagementFormId
    );

    /**
     * Saves the Engagement Form Comments Entity
     *
     * @param engagementFormCommentsEntity
     * @return EngagementFormCommentsEntity persisted entity
     */
    @Override
    EngagementFormCommentsEntity save(EngagementFormCommentsEntity engagementFormCommentsEntity);
    
    
    /**
     * Update Comment viewed by Infosec User
     *
     * @param engagementId
     * @param engagementFormId
     * @param commentId
     */
    @Transactional
    @Modifying
    @Query("UPDATE EngagementFormCommentsEntity cmnts SET cmnts.infosecViewed='Y' WHERE (cmnts.engagementId = :engagementId) "+
            "AND (cmnts.engagementFormId = :engagementFormId) "+
            "AND (cmnts.commentItem = :commentItem)"
     )
    int updateCommentViewedByInfosec(@Param("engagementId") Long engagementId,
            @Param("engagementFormId") Long engagementFormId,
            @Param("commentItem") String commentItem);
    
    
    /**
     * Update Comment viewed by Requestor
     *
     * @param engagementId
     * @param engagementFormId
     * @param commentId
     */
    @Transactional
    @Modifying
    @Query("UPDATE EngagementFormCommentsEntity cmnts SET cmnts.requestorViewed='Y' WHERE (cmnts.engagementId = :engagementId) "+
            "AND (cmnts.engagementFormId = :engagementFormId) "+
            "AND (cmnts.commentItem = :commentItem)"
     )
    int updateCommentViewedByRequestor(@Param("engagementId") Long engagementId,
            @Param("engagementFormId") Long engagementFormId,
            @Param("commentItem") String commentItem);
}
