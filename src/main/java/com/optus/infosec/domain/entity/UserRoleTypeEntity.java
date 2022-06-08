package com.optus.infosec.domain.entity;


import com.optus.infosec.domain.enums.UserRole;

import javax.persistence.*;

/**
 * @author SM
 *
 * User Role Type - ADMIN, BUINESS OWNER etc
 */
@Entity
@Table(name = "user_role_type")
public class UserRoleTypeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private Long userRoleTypeId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public Long getUserRoleTypeId() {
        return userRoleTypeId;
    }

    public void setUserRoleTypeId(Long userRoleTypeId) {
        this.userRoleTypeId = userRoleTypeId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "UserRoleTypeEntity{" +
                "userRoleTypeId='" + userRoleTypeId + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}