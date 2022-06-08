package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.entity.RiskEntity;
import com.optus.infosec.domain.enums.Level;
import com.optus.infosec.domain.enums.Status;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author SM
 * <p>
 * Risk Entity Repository
 */
public interface RiskEntityRepository extends JpaRepository<RiskEntity, Long> {

    /**
     * Saves Risk Entity
     *
     * @param riskEntity
     * @return
     */
    @Override
    RiskEntity save(RiskEntity riskEntity);

    /**
     * Gets Risk Entity
     *
     * @param riskId
     * @return
     */
    @Override
    RiskEntity getOne(Long riskId);


    /**
     * Find all risks for a given engagement
     *
     * @param engagementEntity
     * @return
     */
    List<RiskEntity> findByEngagementEntity(@Param("engagementEntity") EngagementEntity engagementEntity);


    /**
     * Get by Risk id
     *
     * @param riskId
     * @return
     */
    RiskEntity findByRiskId(@Param("riskId") Long riskId);


    /**
     * Find Risk Entity
     *
     * @param riskOwner
     * @param engagementEntity
     * @param projectName
     * @param infosecResource
     * @param riskStatus
     * @return List<RiskEntity>
     */
    @Query("SELECT re FROM RiskEntity re WHERE (re.riskOwner LIKE %:riskOwner% OR :riskOwner is null)" +
            "AND (re.engagementEntity = :engagementEntity OR :engagementEntity is null)" +
            "AND (re.projectName LIKE %:projectName% OR :projectName is null)" +
            "AND (re.infosecResource LIKE %:infosecResource% OR :infosecResource is null)" +
            "AND (re.riskStatus = :riskStatus OR :riskStatus is null)" +
            "AND (re.riskId = :riskId OR :riskId is null)"
    )
    List<RiskEntity> findByParams(
            @Param("engagementEntity") EngagementEntity engagementEntity,
            @Param("riskId") Long riskId,
            @Param("riskOwner") String riskOwner,
            @Param("projectName") String projectName,
            @Param("infosecResource") String infosecResource,
            @Param("riskStatus") Status riskStatus
    );


//    engagementId,
//    riskLevel,
//    projectName,
//    riskId,
//    riskName,
//    riskOwner,
//    riskStatus,
//    raisedDatetimeStart,
//    raisedDatetimeEnd,
//    completedDatetimeStart,
//    completedDatetimeEnd,


    /*
    search engagement list
   */
    @Query("select re FROM RiskEntity re " +
            "WHERE (re.engagementEntity.engagementId = :engagementId OR :engagementId is null ) " +
            "AND (re.riskLevel = :riskLevel OR :riskLevel is null ) " +
            "AND (re.projectName LIKE %:projectName% OR :projectName is null ) " +
            "AND (re.riskId = :riskId OR :riskId is null ) " +
            "AND (re.riskName LIKE %:riskName OR :riskName is null ) " +
            "AND (re.riskOwner LIKE %:riskOwner OR :riskOwner is null ) " +
            "AND (re.riskStatus =:riskStatus OR :riskStatus is null )" +
            "AND ((re.raisedDatetime BETWEEN :raisedDatetimeStart AND :raisedDatetimeEnd)  OR :raisedDatetimeStart is null) " +
            "AND ((re.completedDatetime BETWEEN :completedDatetimeStart AND :completedDatetimeEnd) OR :completedDatetimeStart is null) ")
    List<RiskEntity> search(
            @Param("engagementId") Long engagementId,
            @Param("riskLevel") Level riskLevel,
            @Param("projectName") String projectName,
            @Param("riskId") Long riskId,
            @Param("riskName") String riskName,
            @Param("riskOwner") String riskOwner,
            @Param("riskStatus") Status riskStatus,
            @Param("raisedDatetimeStart") LocalDateTime raisedDatetimeStart,
            @Param("raisedDatetimeEnd") LocalDateTime raisedDatetimeEnd,
            @Param("completedDatetimeStart") LocalDateTime completedDatetimeStart,
            @Param("completedDatetimeEnd") LocalDateTime completedDatetimeEnd,
            Pageable pageable
    );


    /*
    search engagement list
    */
    @Query("select count (re) from RiskEntity re " +
            "WHERE (re.engagementEntity.engagementId = :engagementId OR :engagementId is null ) " +
            "AND (re.riskLevel = :riskLevel OR :riskLevel is null ) " +
            "AND (re.projectName LIKE lower (concat('%', :projectName,'%')) OR :projectName is null ) " +
            "AND (re.riskId = :riskId OR :riskId is null ) " +
            "AND (re.riskName LIKE lower (concat('%', :riskName,'%')) OR :riskName is null ) " +
            "AND (re.riskOwner LIKE lower (concat('%', :riskOwner,'%')) OR :riskOwner is null ) " +
            "AND (re.riskStatus =:riskStatus OR :riskStatus is null )" +
            "AND ((re.raisedDatetime BETWEEN :raisedDatetimeStart AND :raisedDatetimeEnd)  OR :raisedDatetimeStart is null) " +
            "AND ((re.completedDatetime BETWEEN :completedDatetimeStart AND :completedDatetimeEnd) OR :completedDatetimeStart is null) ")
    Integer countSearch(
            @Param("engagementId") Long engagementId,
            @Param("riskLevel") Level riskLevel,
            @Param("projectName") String projectName,
            @Param("riskId") Long riskId,
            @Param("riskName") String riskName,
            @Param("riskOwner") String riskOwner,
            @Param("riskStatus") Status riskStatus,
            @Param("raisedDatetimeStart") LocalDateTime raisedDatetimeStart,
            @Param("raisedDatetimeEnd") LocalDateTime raisedDatetimeEnd,
            @Param("completedDatetimeStart") LocalDateTime completedDatetimeStart,
            @Param("completedDatetimeEnd") LocalDateTime completedDatetimeEnd
    );

    @Query(value = "select t.riskStatus from RiskEntity t")
    List<String> getAll();

    @Query(value = "select t.riskStatus,t.completedDatetime from RiskEntity t")
    List<Object> getRiskTypeAndCompletedDateTime();

}
