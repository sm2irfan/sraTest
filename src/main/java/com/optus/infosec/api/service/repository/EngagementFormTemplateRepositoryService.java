package com.optus.infosec.api.service.repository;

import com.optus.infosec.api.converter.ConversionService;
import com.optus.infosec.api.dto.EngagementFormTemplateDTO;
import com.optus.infosec.repositories.EngagementFomTemplateEntityRepository;
import com.optus.infosec.domain.entity.EngagementFormTemplateEntity;
import com.optus.infosec.domain.enums.EngagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SM
 *
 * Class to access repository methods of engagement form template entity
 */
@Service
public class EngagementFormTemplateRepositoryService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementFormTemplateRepositoryService.class);

    @Autowired
    private EngagementFomTemplateEntityRepository engagementFomTemplateEntityRepository;

    @Autowired
    private ConversionService conversionService;

    /**
     * Get the lastest version form template for an engagement form
     *
     * @param engagementForm
     * @return EngagementFormTemplateDTO
     */
    public EngagementFormTemplateDTO getLatestEngagementFormTemplate(EngagementForm engagementForm){
        LOG.info("Getting Latest Version Form Template for Engagement Form : {}", engagementForm);
        EngagementFormTemplateEntity engagementFormTemplateEntity =
                engagementFomTemplateEntityRepository.findTop1ByTemplateFormNameOrderByTemplateVersionDesc(engagementForm);
        LOG.info("EngagementFormTemplateEntity for EngagementForm {}: {} ", engagementForm, engagementFormTemplateEntity);
        return conversionService.convert(engagementFormTemplateEntity, EngagementFormTemplateDTO.class);
    }
}
