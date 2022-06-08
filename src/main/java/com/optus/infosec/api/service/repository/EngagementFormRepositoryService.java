package com.optus.infosec.api.service.repository;

import com.optus.infosec.api.converter.ConversionService;
import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.api.dto.EngagementFormTemplateDTO;
import com.optus.infosec.repositories.EngagementFormEntityRepository;
import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.entity.EngagementFormEntity;
import com.optus.infosec.domain.enums.EngagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SM
 * <p>
 * Repository Service Class for Engagement Forms
 */
@Service
public class EngagementFormRepositoryService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementFormRepositoryService.class);

    @Autowired
    private EngagementFormEntityRepository engagementFormEntityRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private EngagementRepositoryService engagementRepositoryService;

    @Autowired
    private EngagementFormTemplateRepositoryService engagementFormTemplateRepositoryService;

    /**
     * Creates a new engagement form
     *
     * @param engagementFormDTO
     */
    public void createEngagementForm(Long engagementId, EngagementFormDTO engagementFormDTO) {

        EngagementFormEntity engagementFormEntity = conversionService.convert(engagementFormDTO, EngagementFormEntity.class);
        LOG.debug("Creating Engagement Form Entity : {} For Engagement Id : {}", engagementFormEntity, engagementId);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        // set engagement entity in engagement entity form
        engagementFormEntity.setEngagementEntity(engagementEntity);
        // set created and modified date time
        engagementFormEntity.setCreatedDatetime(LocalDateTime.now());
        engagementFormEntity.setModifiedDatetime(LocalDateTime.now());
        EngagementFormEntity createdEngagementFormEntity = engagementFormEntityRepository.save(engagementFormEntity);

        LOG.debug("Engagement Form Entity Created Successfully : {}", createdEngagementFormEntity);
    }

    /**
     * Create Engagement Form List
     *
     * @param engagementId
     * @param engagementFormDTOList
     */
    public void createEngagementFormList(Long engagementId, List<EngagementFormDTO> engagementFormDTOList) {

        List<EngagementFormEntity> engagementFormEntityList = conversionService.convertList(engagementFormDTOList, EngagementFormEntity.class);
        LOG.debug("Creating Engagement Form Entity List : {} For Engagement Id : {}", engagementFormEntityList, engagementId);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);


        engagementFormEntityList.forEach(engagementFormEntity -> {
            // set engagement entity in engagement entity form
            engagementFormEntity.setEngagementEntity(engagementEntity);
            // set created and modified date time
            engagementFormEntity.setCreatedDatetime(LocalDateTime.now());
            engagementFormEntity.setModifiedDatetime(LocalDateTime.now());
        });

        List<EngagementFormEntity> createdEngagementFormEntityList = engagementFormEntityRepository.saveAll(engagementFormEntityList);

        LOG.debug("Engagement Form Entity List Created Successfully : {}", createdEngagementFormEntityList);
    }

    /**
     * Get Engagement Form
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public EngagementFormDTO getEngagementForm(Long engagementId, EngagementForm engagementFormType) {

        LOG.debug("Get Engagement Form Entity For Engagement Id : {} And Engagement Form Type: {}", engagementId, engagementFormType);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        EngagementFormEntity engagementFormEntity = engagementFormEntityRepository.findByEngagementEntityAndEngagementFormName(engagementEntity, engagementFormType);
        LOG.debug("Returned Engagement Form Entity: {} For Entity Form Type : {}", engagementFormEntity, engagementFormType);
        EngagementFormDTO engagementFormDTO = conversionService.convert(engagementFormEntity, EngagementFormDTO.class);
        return engagementFormDTO;
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public void updateEngagementForm(Long engagementId, EngagementForm engagementFormType, EngagementFormDTO engagementFormDTO) {

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        // Get proxy object for retrieved engagement form entity
        EngagementFormEntity retrievedEngagementFormEntity = engagementFormEntityRepository.findByEngagementEntityAndEngagementFormName(engagementEntity, engagementFormType);
        LOG.debug("Retrieved Engagement Form Entity For Engagement Id : {} And Engagement Form Type: {}", retrievedEngagementFormEntity, engagementId, engagementFormType);

        // New Engagement Entity
        EngagementFormEntity newEngagementFormEntity = conversionService.convert(engagementFormDTO, EngagementFormEntity.class);
        LOG.debug("New Engagement Form Entity: {}", newEngagementFormEntity);

        // set modified date time
        retrievedEngagementFormEntity.setModifiedDatetime(LocalDateTime.now());

        // copy contents to be updated
        //retrievedEngagementFormEntity.setFormTemplate(newEngagementFormEntity.getFormTemplate());
        retrievedEngagementFormEntity.setFormData(newEngagementFormEntity.getFormData());
        //retrievedEngagementFormEntity.setEngagementFormName(newEngagementFormEntity.getEngagementFormName());

        engagementFormEntityRepository.save(retrievedEngagementFormEntity);
    }


    /**
     * Create engagement forms with default template -
     * gets the default template from engagement form template table and creates new engagement form
     *
     * @param engagementId
     * @param engagementFormList
     */
    public void createDefaultEngagementForms(Long engagementId, List<EngagementForm> engagementFormList) {

        LOG.info("Creating default engagement forms for Engagement Id: {} and EngagementFormList: {}", engagementId, engagementFormList);
        // get all the default form templates
        // TODO: get all in one db transaction
        List<EngagementFormDTO> engagementFormDTOList = new ArrayList<>();
        engagementFormList.forEach(engagementForm -> {

            EngagementFormTemplateDTO engagementFormTemplateDTO = engagementFormTemplateRepositoryService.getLatestEngagementFormTemplate(engagementForm);
            if (engagementFormTemplateDTO != null) {
                EngagementFormDTO engagementFormDTO = new EngagementFormDTO();
                engagementFormDTO.setEngagementFormName(engagementFormTemplateDTO.getTemplateFormName());
                engagementFormDTO.setFormTemplate(engagementFormTemplateDTO.getTemplate());
                engagementFormDTOList.add(engagementFormDTO);
            }
        });

        // create
        createEngagementFormList(engagementId, engagementFormDTOList);
    }
}
