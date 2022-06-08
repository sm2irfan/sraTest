package com.optus.infosec.domain.entity;

public class EmployeeEntity {


    private String employeeId;

    private String firstName;

    private String lastName;

    private String position;

    private String email;

    private String company;

    private String imgUrl;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String employeeId, String firstName, String lastName, String position, String email, String company, String imgUrl) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.email = email;
        this.company = company;
        this.imgUrl = imgUrl;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "EmployeeEntity [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", position=" + position + ", email=" + email + ", company=" + company + ", imgUrl=" + imgUrl + "]";
    }

}
