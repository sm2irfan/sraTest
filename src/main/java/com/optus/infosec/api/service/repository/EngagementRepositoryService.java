package com.optus.infosec.api.service.repository;

import com.optus.infosec.api.converter.ConversionService;
import com.optus.infosec.api.dto.*;
import com.optus.infosec.api.service.EmployeeService;
import com.optus.infosec.domain.entity.*;
import com.optus.infosec.domain.enums.Status;
import com.optus.infosec.repositories.EngagementEntityRepository;
import com.optus.infosec.repositories.EngagementSearchListProjection;
import com.optus.infosec.repositories.RiskProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SM
 * <p>
 * Repository Service Class for Engagements
 */
@Service
public class EngagementRepositoryService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementRepositoryService.class);

    private final EngagementEntityRepository engagementEntityRepository;

    private final ConversionService conversionService;

    private final EmployeeService employeeService;

    @Value("#{InfosecUsersService.infosecUsersEntityList}")
    private List<InfosecUsersEntity> infosecUsersEntityList;

    public EngagementRepositoryService(EngagementEntityRepository engagementEntityRepository,
                                       ConversionService conversionService,
                                       EmployeeService employeeService) {
        this.engagementEntityRepository = engagementEntityRepository;
        this.conversionService = conversionService;
        this.employeeService = employeeService;
    }

    /**
     * Creates a new engagement
     *
     * @param engagementDTO
     */
    public EngagementDTO createEngagement(EngagementDTO engagementDTO) {

        try {

            String requestedBy = engagementDTO.getRequestedBy();

            EmployeeEntity employeeEntityFromId = employeeService.getEmployeeEntityFromId(requestedBy);


            EngagementEntity engagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

            if (engagementEntity != null) {
                engagementEntity.setRequestedByName(employeeEntityFromId.getFirstName() + " " + employeeEntityFromId.getLastName());
            }

            LOG.debug("Creating Engagement Entity : {}", engagementEntity);

            // set created and modified date time
            engagementEntity.setCreatedDatetime(LocalDateTime.now());
            engagementEntity.setModifiedDatetime(LocalDateTime.now());

            //set created and modified date time for engagement forms
            engagementEntity.getEngagementFormEntitySet().forEach(engagementFormEntity -> {
                engagementFormEntity.setCreatedDatetime(LocalDateTime.now());
                engagementFormEntity.setModifiedDatetime(LocalDateTime.now());
            });

            EngagementEntity createdEngagementEntity = engagementEntityRepository.save(engagementEntity);
            LOG.debug("Engagement Entity Created Successfully : {}", createdEngagementEntity);
            return conversionService.convert(createdEngagementEntity, EngagementDTO.class);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public EngagementDTO getEngagement(Long engagementId) {

        LOG.info("Get Engagement Entity For Engagement Id : {}", engagementId);
        Optional<EngagementEntity> engagementEntity = engagementEntityRepository.findById(engagementId);
        LOG.info("Returned Engagement Entity: {} For Entity Id : {}", engagementEntity, engagementId);
        if (engagementEntity.isPresent()) {
            return this.entityToDTO(engagementEntity.get());
        } else {
            return null;
        }

    }

    /**
     * Get Engagements
     *
     * @return EngagementEntity
     */
    public List<EngagementDTO> getEngagements() {

        LOG.debug("Get All Engagements");
        List<EngagementEntity> engagementEntityList = engagementEntityRepository.findAll();
        List<EngagementDTO> engagementDTOList = new ArrayList<>();
        engagementEntityList.forEach(engagementEntity -> engagementDTOList.add(conversionService.convert(engagementEntity, EngagementDTO.class)));
        return engagementDTOList;
    }

    /**
     * Get Engagements
     *
     * @return EngagementEntity
     */
    public List<EngagementDTO> getEngagementsWithParams(String requestedBy, Long engagementId, String projectName, String assignedTo, Status engagementStatus) {

        LOG.debug("Get All Engagements Requested By : {}", requestedBy);
        List<EngagementEntity> engagementEntityList = engagementEntityRepository.findByRequestedByAndEngagementIdAndProjectNameAndInfosecResourceAndEngagementStatus(requestedBy,
                engagementId,
                projectName,
                assignedTo,
                engagementStatus
        );
        LOG.debug("Returned Engagement Entities: {}", engagementEntityList);
        List<EngagementDTO> engagementDTOList = new ArrayList<>();
        engagementEntityList.forEach(engagementEntity -> engagementDTOList.add(conversionService.convert(engagementEntity, EngagementDTO.class)));
        return engagementDTOList;
    }

    /**
     * Get Engagements
     *
     * @return EngagementEntity
     */
    public List<EngagementDTO> getEngagementsWithRiskOwnerEmailId(String emailId) {

        LOG.debug("Get All Engagements For Risk Owner Email Id : {}", emailId);
        List<EngagementEntity> engagementEntityList = engagementEntityRepository.findByRiskOwnerEmailId(emailId);
        LOG.debug("Returned Engagement Entities: {}", engagementEntityList);
        List<EngagementDTO> engagementDTOList = new ArrayList<>();
        engagementEntityList.forEach(engagementEntity -> engagementDTOList.add(conversionService.convert(engagementEntity, EngagementDTO.class)));
        return engagementDTOList;
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public void updateEngagement(Long engagementId, EngagementDTO engagementDTO) {

        try {
            // Get proxy object for retrieved engagement entity
            EngagementEntity retrievedEngagementEntity = engagementEntityRepository.getOne(engagementId);
            LOG.debug("Retrieved Engagement Entity,{} For Engagement Id : {}", retrievedEngagementEntity, engagementId);
            // New Engagement Entity
            EngagementEntity newEngagementEntity = conversionService.convert(engagementDTO, EngagementEntity.class);

            LOG.debug("New Engagement Entity: {}", newEngagementEntity);
            // set modified date time
            retrievedEngagementEntity.setModifiedDatetime(LocalDateTime.now());

            //copy fields from new to retrieved proxy object
            retrievedEngagementEntity.setEngagementStatus(newEngagementEntity.getEngagementStatus());
            retrievedEngagementEntity.setAssignedTo(newEngagementEntity.getAssignedTo());
            retrievedEngagementEntity.setRequestedBy(newEngagementEntity.getRequestedBy());
            retrievedEngagementEntity.setCompletedDatetime(newEngagementEntity.getCompletedDatetime());
            retrievedEngagementEntity.setRequestedDatetime(newEngagementEntity.getRequestedDatetime());
            retrievedEngagementEntity.setProjectName(newEngagementEntity.getProjectName());
            retrievedEngagementEntity.setEngagementDate(newEngagementEntity.getEngagementDate());

            String assigendToName = infosecUsersEntityList.stream()
                    .filter(infosecUsersEntity -> infosecUsersEntity.getEmployeeId().toLowerCase().trim().equalsIgnoreCase(newEngagementEntity.getAssignedTo().toLowerCase().trim()))
                    .map(infosecUsersEntity -> {
                        return infosecUsersEntity.getFirstName() + " " + infosecUsersEntity.getLastName();
                    }).collect(Collectors.toList()).get(0);
            retrievedEngagementEntity.setAssignedToName(assigendToName);
            engagementEntityRepository.save(retrievedEngagementEntity);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public EngagementDtoWrapper searchEngagements(Long engagementId,
                                                  String projectName,
                                                  String requestedBy,
                                                  Status engagementStatus,
                                                  LocalDate requestedDatetime,
                                                  LocalDate completedDatetime,
                                                  String assignedTo,
                                                  Integer numberOfRisks,
                                                  Integer page,
                                                  Integer size) {

        /*
        From the front end user sends a single LocalDate as requestedDatetime and completedDatetime,
        hence we get data for the 24 hours for that particular date as LocalDateTime
         */
        LocalDateTime requestedDatetimeStart = requestedDatetime == null ? null : LocalDateTime.of(requestedDatetime, LocalTime.of(0, 0, 0));
        LocalDateTime requestedDatetimeEnd = requestedDatetime == null ? null : LocalDateTime.of(requestedDatetime, LocalTime.of(23, 59, 59));

        LocalDateTime completedDatetimeStart = completedDatetime == null ? null : LocalDateTime.of(completedDatetime, LocalTime.of(0, 0, 0));
        LocalDateTime completedDatetimeEnd = completedDatetime == null ? null : LocalDateTime.of(completedDatetime, LocalTime.of(23, 59, 59));

        LOG.debug("assignedTo>>>>>>>>>>>>>>>>: {}", assignedTo);

        List<EngagementSearchListProjection> engagementEntityList = this.engagementEntityRepository.search(
                engagementId,
                projectName,
                requestedBy,
                engagementStatus,
                requestedDatetimeStart,
                requestedDatetimeEnd,
                completedDatetimeStart,
                completedDatetimeEnd,
                assignedTo,
                // mage 1 st page and number of records to 0 and 10 by default if user sends nothing
                PageRequest.of((page == null ? 0 : page), (size == null ? 10 : size)));

        LOG.debug("engagementEntityList >>>>> {}", engagementEntityList.size());

        Integer count = this.engagementEntityRepository.countSearch(
                engagementId,
                projectName,
                requestedBy,
                engagementStatus,
                requestedDatetimeStart,
                requestedDatetimeEnd,
                completedDatetimeStart,
                completedDatetimeEnd,
                assignedTo);

        List<EngagementSearchListProjection> engagementListFilteredFromNumberOfRisks = engagementEntityList;

        if (numberOfRisks != null) {
            // filter from number of risks
            engagementListFilteredFromNumberOfRisks = engagementEntityList
                    .stream()
                    .filter(engagementEntity -> engagementEntity.getRiskEntitySet().size() == numberOfRisks)
                    .collect(Collectors.toList());
        }
        LOG.debug("engagementListFilteredFromNumberOfRisks size >>>>> {}", engagementListFilteredFromNumberOfRisks.size());

        List<EngagementSearchListDto> engagementDTOList = new ArrayList<>();

        // convert entity to DTO
        engagementListFilteredFromNumberOfRisks.forEach(projection ->
                engagementDTOList.add(new EngagementSearchListDto(
                        projection.getEngagementId(),
                        projection.getProjectName(),
                        projection.getRequestedDatetime(),
                        projection.getCompletedDatetime(),
                        projection.getRequestedBy(),
                        // send assigned to name instead of assigned to code
                        projection.getAssignedToName(),
                        projection.getEngagementStatus(),
                        convertToRiskSet(projection.getRiskEntitySet())
                )));

        LOG.debug("engagementDTOList >>>>> {}", engagementDTOList.toString());

        return new EngagementDtoWrapper(count, engagementDTOList);
    }

    Set<RiskDTO> convertToRiskSet(Set<RiskProjection> riskEntitySet) {
        Set<RiskDTO> risks = new HashSet<>();
        riskEntitySet.forEach(riskProjection ->
                risks.add(new RiskDTO(
                        riskProjection.getEngagementEntity().getEngagementId(),
                        riskProjection.getRiskId(),
                        riskProjection.getRiskName(),
                        riskProjection.getProjectName(),
                        riskProjection.getInfosecResource(),
                        riskProjection.getLikelihood(),
                        riskProjection.getImpact(),
                        riskProjection.getRiskLevel(),
                        riskProjection.getRaisedDatetime(),
                        riskProjection.getIssuesIdentified(),
                        riskProjection.getMitigationAction(),
                        riskProjection.getRiskStatus(),
                        riskProjection.getCompletedDatetime(),
                        riskProjection.getRiskOwner()
                )));
        return risks;
    }

    EngagementDTO entityToDTO(EngagementEntity engagementEntity) {
        return new EngagementDTO(
                engagementEntity.getEngagementId(),
                engagementEntity.getProjectName(),
                engagementEntity.getEngagementDate(),
                engagementEntity.getRequestedDatetime(),
                engagementEntity.getCompletedDatetime(),
                engagementEntity.getRequestedBy(),
                engagementEntity.getAssignedTo(),
                engagementEntity.getEngagementStatus(),
                generateEngagementFormDTOSet(engagementEntity.getEngagementFormEntitySet()),
                generateRiskDTOSet(engagementEntity.getRiskEntitySet())
        );
    }

    Set<EngagementFormDTO> generateEngagementFormDTOSet(Set<EngagementFormEntity> engagementFormEntitySet) {
        Set<EngagementFormDTO> engagementFormDTOSet = new HashSet<>();
        engagementFormEntitySet.forEach(engagementFormEntity -> engagementFormDTOSet.add(new EngagementFormDTO(
                engagementFormEntity.getEngagementFormId(),
                engagementFormEntity.getEngagementFormName(),
                Arrays.toString(engagementFormEntity.getFormTemplate()),
                Arrays.toString(engagementFormEntity.getFormData())
        )));
        return engagementFormDTOSet;
    }

    Set<RiskDTO> generateRiskDTOSet(Set<RiskEntity> riskEntitySet) {
        Set<RiskDTO> riskDTOSet = new HashSet<>();
        riskEntitySet.forEach(riskEntity -> riskDTOSet.add(new RiskDTO(
                riskEntity.getEngagementEntity().getEngagementId(),
                riskEntity.getRiskId(),
                riskEntity.getRiskName(),
                riskEntity.getProjectName(),
                riskEntity.getInfosecResource(),
                riskEntity.getLikelihood(),
                riskEntity.getImpact(),
                riskEntity.getRiskLevel(),
                riskEntity.getRaisedDatetime(),
                riskEntity.getIssuesIdentified(),
                riskEntity.getMitigationAction(),
                riskEntity.getRiskStatus(),
                riskEntity.getCompletedDatetime(),
                riskEntity.getRiskOwner()
        )));
        return riskDTOSet;
    }
}
