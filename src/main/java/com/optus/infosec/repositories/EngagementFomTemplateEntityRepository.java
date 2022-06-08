package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.EngagementFormTemplateEntity;
import com.optus.infosec.domain.enums.EngagementForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author SM
 * <p>
 * Engagement Form Template Repository
 */
public interface EngagementFomTemplateEntityRepository extends JpaRepository<EngagementFormTemplateEntity, Long> {


    /**
     * Gets the last version assuming versions are like - 1, 2, 3 . . . etc
     *
     * @param templateFormName
     * @return EngagementFormTypeEntity
     */
     EngagementFormTemplateEntity findTop1ByTemplateFormNameOrderByTemplateVersionDesc(@Param("templateFormName") EngagementForm templateFormName);
}
