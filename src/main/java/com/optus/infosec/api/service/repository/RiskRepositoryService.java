package com.optus.infosec.api.service.repository;

import com.optus.infosec.api.converter.ConversionService;
import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.api.dto.RiskDtoWrapper;
import com.optus.infosec.repositories.RiskEntityRepository;
import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.entity.RiskEntity;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SM
 *
 * Risk Controller
 */
@Service
public class RiskRepositoryService {

    private static final Logger LOG = LoggerFactory.getLogger(RiskRepositoryService.class);

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private RiskEntityRepository riskEntityRepository;

    @Autowired
    private EngagementRepositoryService engagementRepositoryService;

    /**
     * Create a new Risk
     *
     * @param engagementId
     * @param riskDTO
     * @return
     */
    public RiskDTO createRisk(Long engagementId, RiskDTO riskDTO){

        RiskEntity riskEntity = conversionService.convert(riskDTO, RiskEntity.class);
        LOG.debug("Creating Risk Entity : {}", riskEntity);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        // set engagement entity in engagement entity form
        riskEntity.setEngagementEntity(engagementEntity);
        // set created and modified date time
        riskEntity.setCreatedDatetime(LocalDateTime.now());
        riskEntity.setModifiedDatetime(LocalDateTime.now());
        RiskEntity createdRiskEntity = riskEntityRepository.save(riskEntity);

        LOG.debug("Risk Entity Created Successfully : {}", createdRiskEntity);

        return conversionService.convert(createdRiskEntity, RiskDTO.class);
    }

    /**
     * Create a Risk List
     *
     * @param engagementId
     * @param riskDTOList
     */
    public List<RiskDTO> createRiskList(Long engagementId, List<RiskDTO> riskDTOList) {

        List<RiskEntity> riskEntityList = conversionService.convertList(riskDTOList, RiskEntity.class);
        LOG.debug("Creating Risk Entity List : {}", riskEntityList);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        riskEntityList.forEach(riskEntity -> {
            // set engagement entity in engagement entity form
            riskEntity.setEngagementEntity(engagementEntity);
            // set created and modified date time
            riskEntity.setCreatedDatetime(LocalDateTime.now());
            riskEntity.setModifiedDatetime(LocalDateTime.now());
        });

        List<RiskEntity> createdRiskEntityList = riskEntityRepository.saveAll(riskEntityList);

        LOG.debug("Risk Entity List Created Successfully : {}", createdRiskEntityList);

        return conversionService.convertList(createdRiskEntityList, RiskDTO.class);
    }

    /**
     * Update a Risk
     *
     * @param engagementId
     * @param riskId
     * @param riskDTO
     * @return
     */
    public RiskDTO updateRisk(Long engagementId, Long riskId, RiskDTO riskDTO){

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        // Get proxy object for retrieved engagement form entity
        RiskEntity retrievedRiskEntity = riskEntityRepository.findByRiskId(riskId);
        LOG.debug("Retrieved Risk Entity :{} For Engagement Id : {} And Risk Id: {}", retrievedRiskEntity, engagementId, riskId);

        // New Engagement Entity
        RiskEntity newRiskEntity = conversionService.convert(riskDTO, RiskEntity.class);
        LOG.debug("New Risk Entity: {}", newRiskEntity);

        // set modified date time
        retrievedRiskEntity.setModifiedDatetime(LocalDateTime.now());
        retrievedRiskEntity = copyData(newRiskEntity, retrievedRiskEntity);

        return conversionService.convert(riskEntityRepository.save(retrievedRiskEntity), RiskDTO.class);
    }

    /**
     * Update Risks for an Engagement
     *
     * @param engagementId
     * @param riskDTOList
     * @return
     */
    public List<RiskDTO> updateRiskList(Long engagementId, List<RiskDTO> riskDTOList){

        List<RiskEntity> riskEntityList = conversionService.convertList(riskDTOList, RiskEntity.class);
        List<RiskEntity> saveRiskEntityList = new ArrayList<>();
        riskEntityList.forEach(riskEntity -> {

                    RiskEntity retrievedRiskEntityProxy = riskEntityRepository.getOne(riskEntity.getRiskId());

                    // set modified date time
                    retrievedRiskEntityProxy.setModifiedDatetime(LocalDateTime.now());
                    retrievedRiskEntityProxy = copyData(riskEntity, retrievedRiskEntityProxy);

                    // add to List
                    saveRiskEntityList.add(retrievedRiskEntityProxy);
                }
        );
        // update existing risks
        List<RiskEntity> updatedRiskDtoList = riskEntityRepository.saveAll(saveRiskEntityList);

        return conversionService.convertList(updatedRiskDtoList, RiskDTO.class);
    }

    /**
     * Get Risk
     *
     * @param engagementId
     * @param riskId
     */
    public RiskDTO getRisk(Long engagementId, Long riskId) {

        LOG.debug("Get Risk Entity For Risk Id : {} and Engagement Id", riskId, engagementId);
        RiskEntity riskEntity = riskEntityRepository.findByRiskId(riskId);
        LOG.debug("Returned Risk Entity: {} For Risk Id : {}", riskEntity, engagementId);
        RiskDTO riskDTO = conversionService.convert(riskEntity, RiskDTO.class);
        return riskDTO;
    }

    /**
     *
     * Get All Risks
     *
     * @param engagementId
     */
    public List<RiskDTO> getRisks(Long engagementId) {

        LOG.debug("Get Risk Entities For Engagement Id : {}", engagementId);

        EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
        EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

        List<RiskEntity> riskEntityList = riskEntityRepository.findByEngagementEntity(engagementEntity);

        return conversionService.convertList(riskEntityList, RiskDTO.class);
    }

    /**
     * Get All Risks
     *
     * @return List<RiskDTO>
     */
    public List<RiskDTO> getRisks() {

        LOG.debug("Get All Risk Entities");
        List<RiskEntity> riskEntityList = riskEntityRepository.findAll();
        return conversionService.convertList(riskEntityList, RiskDTO.class);
    }


    /**
     * Get All Risks matching engagement Id, project name, business owner, infosec resource, engagement status
     *
     * @return List<RiskDTO>
     */
    public List<RiskDTO> getRisks(Long engagementId, Long riskId, String projectName, String businessOwner, String infosecResource, Status riskStatus) {

        LOG.debug("Get Risk Entities For Engagement Id : {}, Project Name: {}, Business Owner: {}, Infosec Resource: {}, Risk Status: {}", engagementId, projectName, businessOwner, infosecResource, riskStatus);
        EngagementEntity engagementEntity = null;
        if(null != engagementId) {
            EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
            engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);
        }
        return conversionService.convertList(riskEntityRepository.findByParams(engagementEntity, riskId, businessOwner, projectName, infosecResource, riskStatus), RiskDTO.class);
    }

    /**
     *
     * @param source
     * @return
     */
    private RiskEntity copyData(RiskEntity source, RiskEntity destination) {

        if(source==null){
            return new RiskEntity();
        }

        if(destination==null){
            destination = new RiskEntity();
        }

        destination.setRaisedDatetime(source.getRaisedDatetime());
        destination.setCompletedDatetime(source.getCompletedDatetime());
        destination.setImpact(source.getImpact());
        destination.setInfosecResource(source.getInfosecResource());
        destination.setIssuesIdentified(source.getIssuesIdentified());
        destination.setLikelihood(source.getLikelihood());
        destination.setMitigationAction(source.getMitigationAction());
        destination.setProjectName(source.getProjectName());
        destination.setRiskName(source.getRiskName());
        destination.setRiskOwner(source.getRiskOwner());
        destination.setRiskStatus(source.getRiskStatus());
        destination.setRiskLevel(source.getRiskLevel());

        return destination;
    }
    
    
    public RiskDtoWrapper searchRisks(Long engagementId, 
			Level riskLevel, 
			String projectName, 
			Long riskId, 
			String riskName,
			String riskOwner, 
			Status riskStatus, 
			LocalDate raisedDatetime, 
			LocalDate completedDatetime, 
			Integer page,
			Integer size) {
		
		  /*
        From the front end user sends a single LocalDate as requestedDatetime and completedDatetime,
        hence we get data for the 24 hours for that particular date as LocalDateTime
         */
        LocalDateTime raisedDatetimeStart = raisedDatetime == null ? null : LocalDateTime.of(raisedDatetime, LocalTime.of(0, 0, 0));
        LocalDateTime raisedDatetimeEnd = raisedDatetime == null ? null : LocalDateTime.of(raisedDatetime, LocalTime.of(23, 59, 59));

        LocalDateTime completedDatetimeStart = completedDatetime == null ? null : LocalDateTime.of(completedDatetime, LocalTime.of(0, 0, 0));
        LocalDateTime completedDatetimeEnd = completedDatetime == null ? null : LocalDateTime.of(completedDatetime, LocalTime.of(23, 59, 59));
        
        List<RiskEntity> riskEntityList = this.riskEntityRepository.search(
                engagementId,
                riskLevel,
                projectName,
                riskId,
                riskName,
                riskOwner,
                riskStatus,
                raisedDatetimeStart,
                raisedDatetimeEnd,
                completedDatetimeStart,
                completedDatetimeEnd,
                // mage 1 st page and number of records to 0 and 10 by default if user sends nothing
                PageRequest.of((page == null ? 0 : page), (size == null ? 10 : size)));
        
        Integer count = this.riskEntityRepository.countSearch(
                engagementId,
                riskLevel,
                projectName,
                riskId,
                riskName,
                riskOwner,
                riskStatus,
                raisedDatetimeStart,
                raisedDatetimeEnd,
                completedDatetimeStart,
                completedDatetimeEnd);
        
        List<RiskEntity> engagementListFilteredFromNumberOfRisks = riskEntityList;
        

        List<RiskDTO> engagementDTOList = new ArrayList<>();

        // convert entity to DTO
        engagementListFilteredFromNumberOfRisks.forEach(engagementEntity -> engagementDTOList.add(conversionService.convert(engagementEntity, RiskDTO.class)));

        return new RiskDtoWrapper(count, engagementDTOList);

	}
    
}
