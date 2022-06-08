package com.optus.infosec.repositories;


import com.optus.infosec.domain.entity.EngagementFormTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author PL
 *
 * Engagement Form Tracking Entity Repository
 */
public interface EngagementFormTrackingEntityRepository extends JpaRepository<EngagementFormTrackingEntity, Long> {

	/**
     * Find Engagement Form Tracking
     *
     * @param engagementId
     * @param engagementFormId
     * @return List<EngagementFormTrackingEntity>
     */
    @Query("SELECT trck FROM EngagementFormTrackingEntity trck WHERE (trck.engagementId = :engagementId)"+
           "AND (trck.engagementFormId = :engagementFormId)"+
           "ORDER BY trck.trackingDate"
    )
    List<EngagementFormTrackingEntity> findByEngagementIdAndEngagementFormId(
            @Param("engagementId") Long engagementId,
            @Param("engagementFormId") Long engagementFormId
    );
}
