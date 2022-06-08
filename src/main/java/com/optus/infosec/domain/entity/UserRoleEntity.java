package com.optus.infosec.domain.entity;

import javax.persistence.*;

/**
 * @author SM
 *
 * User Role Entity
 */
@Entity
@Table(name = "user_role") 
public class UserRoleEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String employeeId;

    @ManyToOne
    @JoinColumn(name = "userRoleTypeId")
    private UserRoleTypeEntity userRoleTypeEntity;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public UserRoleTypeEntity getUserRoleTypeEntity() {
        return userRoleTypeEntity;
    }

    public void setUserRoleTypeEntity(UserRoleTypeEntity userRoleTypeEntity) {
        this.userRoleTypeEntity = userRoleTypeEntity;
    }

    @Override
    public String toString() {
        return "UserRoleEntity{" +
                "employeeId='" + employeeId + '\'' +
                ", userRoleTypeEntity=" + userRoleTypeEntity +
                '}';
    }
}