package com.optus.infosec.repositories;

import com.optus.infosec.domain.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author SM
 *
 * User Role Repository
 */
public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, String> {

    /**
     * Find User Role by Employee Id
     * @param employeeId
     * @return UserRoleEntity
     */
    UserRoleEntity findByEmployeeId(@Param("employeeId") String employeeId);

}
