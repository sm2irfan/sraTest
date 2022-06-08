package com.optus.infosec.domain.entity;

public class InfosecUsersEntity {

    private String employeeId;

    private String firstName;

    private String lastName;

    private String email;

    public InfosecUsersEntity(String employeeId, String firstName, String lastName, String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public InfosecUsersEntity() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "InfosecUsersEntity [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + "]";
    }
    
}
