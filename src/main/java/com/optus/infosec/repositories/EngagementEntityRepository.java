package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author SM
 * <p>
 * Engagement Entity Repository
 */
public interface EngagementEntityRepository extends JpaRepository<EngagementEntity, Long> {


    /**
     * Find Engagement Entity by Engagement Id
     *
     * @param engagementId
     * @return EngagementEntity
     */
    EngagementEntity findByEngagementId(@Param("engagementId") Long engagementId);

    /**
     * Find all Engagements
     *
     * @return EngagementEntity
     */
    @Override
    List<EngagementEntity> findAll();

    /**
     * Find Engagement Entity by Engagement Id
     *
     * @param requestedBy
     * @return List<EngagementEntity>
     */
    @Query("SELECT eng FROM EngagementEntity eng " +
            "WHERE (eng.requestedBy LIKE %:requestedBy% OR :requestedBy is null)" +
            "AND (eng.engagementId = :engagementId OR :engagementId is null)" +
            "AND (eng.projectName LIKE %:projectName% OR :projectName is null)" +
            "AND (eng.assignedTo LIKE %:assignedTo% OR :assignedTo is null)" +
            "AND (eng.engagementStatus = :engagementStatus OR :engagementStatus is null)"
    )
    List<EngagementEntity> findByRequestedByAndEngagementIdAndProjectNameAndInfosecResourceAndEngagementStatus(
            @Param("requestedBy") String requestedBy,
            @Param("engagementId") Long engagementId,
            @Param("projectName") String projectName,
            @Param("assignedTo") String assignedTo,
            @Param("engagementStatus") Status engagementStatus
    );

    /**
     * @param emailId
     * @return
     */
    @Query("SELECT re.engagementEntity FROM RiskEntity re WHERE re.riskOwner LIKE %:riskOwner%")
    List<EngagementEntity> findByRiskOwnerEmailId(@Param("riskOwner") String emailId);

    /**
     * Gets proxy object
     *
     * @param engagementId
     * @return
     */
    @Override
    EngagementEntity getOne(Long engagementId);

    /**
     * Saves the Engagement  Entity
     *
     * @param engagementEntity
     * @return EngagementEntity persisted entity
     */
    @Override
    EngagementEntity save(EngagementEntity engagementEntity);


    /*
    search engagement list
   */
    @Query("select eng from EngagementEntity eng " +
            "WHERE (eng.engagementId = :engagementId OR :engagementId is null ) " +
            "AND (eng.projectName LIKE lower (concat('%', :projectName,'%')) OR :projectName is null ) " +
            "AND (eng.assignedToName LIKE lower (concat('%', :assignedToName,'%')) OR :assignedToName is null ) " +
            "AND (eng.requestedByName LIKE lower(concat('%', :requestedByName,'%')) OR :requestedByName is null ) " +
            "AND (eng.engagementStatus =:engagementStatus OR :engagementStatus is null )" +
            "AND ((eng.requestedDatetime BETWEEN :requestedDatetimeStart AND :requestedDatetimeEnd)  OR :requestedDatetimeStart is null) " +
            "AND ((eng.completedDatetime BETWEEN :completedDatetimeStart AND :completedDatetimeEnd) OR :completedDatetimeStart is null) ")
    List<EngagementSearchListProjection> search(
            @Param("engagementId") Long engagementId,
            @Param("projectName") String projectName,
            @Param("requestedByName") String requestedByName,
            @Param("engagementStatus") Status engagementStatus,
            @Param("requestedDatetimeStart") LocalDateTime requestedDatetimeStart,
            @Param("requestedDatetimeEnd") LocalDateTime requestedDatetimeEnd,
            @Param("completedDatetimeStart") LocalDateTime completedDatetimeStart,
            @Param("completedDatetimeEnd") LocalDateTime completedDatetimeEnd,
            @Param("assignedToName") String assignedToName,
            Pageable pageable
    );

    /*
search engagement list
*/
    @Query("select count (eng) from EngagementEntity eng " +
            "WHERE (eng.engagementId = :engagementId OR :engagementId is null ) " +
            "AND (eng.projectName LIKE lower (concat('%', :projectName,'%')) OR :projectName is null ) " +
            "AND (eng.assignedToName LIKE lower (concat('%', :assignedToName,'%')) OR :assignedToName is null ) " +
            "AND (eng.requestedByName LIKE lower(concat('%', :requestedByName,'%')) OR :requestedByName is null ) " +
            "AND (eng.engagementStatus =:engagementStatus OR :engagementStatus is null )" +
            "AND ((eng.requestedDatetime BETWEEN :requestedDatetimeStart AND :requestedDatetimeEnd)  OR :requestedDatetimeStart is null) " +
            "AND ((eng.completedDatetime BETWEEN :completedDatetimeStart AND :completedDatetimeEnd) OR :completedDatetimeStart is null) ")
    Integer countSearch(
            @Param("engagementId") Long engagementId,
            @Param("projectName") String projectName,
            @Param("requestedByName") String requestedByName,
            @Param("engagementStatus") Status engagementStatus,
            @Param("requestedDatetimeStart") LocalDateTime requestedDatetimeStart,
            @Param("requestedDatetimeEnd") LocalDateTime requestedDatetimeEnd,
            @Param("completedDatetimeStart") LocalDateTime completedDatetimeStart,
            @Param("completedDatetimeEnd") LocalDateTime completedDatetimeEnd,
            @Param("assignedToName") String assignedToName
    );

    @Query(value = "select t.engagementStatus from EngagementEntity t")
    List<String> getAll();

}
















