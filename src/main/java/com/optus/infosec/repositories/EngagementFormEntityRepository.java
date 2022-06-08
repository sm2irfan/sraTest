package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.entity.EngagementFormEntity;
import com.optus.infosec.domain.enums.EngagementForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author SM
 *
 * Engagement Form Entity Repository
 */
public interface EngagementFormEntityRepository extends JpaRepository<EngagementFormEntity, Long> {


    /**
     * Saves the Engagement Form Entity
     *
     * @param engagementFormEntity
     * @return EngagementFormEntity persisted entity
     */
   /* @Override
    EngagementFormEntity save(EngagementFormEntity engagementFormEntity);*/

    /**
     *
     * @param engagementEntity
     * @param engagementFormName
     * @return
     */
    EngagementFormEntity findByEngagementEntityAndEngagementFormName(@Param("engagementEntity") EngagementEntity engagementEntity, @Param("engagementFormName") EngagementForm engagementFormName);
    /**
     *
     * @param engagementFormId
     * @return
     */
    @Override
    EngagementFormEntity getOne(Long engagementFormId);

    /**
     * Find Engagement Form Entity by Engagement Form Id
     *
     * @param engagementFormId
     * @return EngagementEntity
     */
    EngagementFormEntity findByEngagementFormId(@Param("engagementFormId") Long engagementFormId);
}
