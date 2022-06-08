//package com.optus.infosec.domain.entity;
//
//import javax.persistence.*;
//
//
///**
// * @author SM
// * <p>
// * Status Entity
// */
//
//@Entity
//@Table(name = "status_type")
//public class Status {
//
//    @Id
//    @Column(nullable = false, unique = true)
//    private Long statusTypeId;
//
//    @Enumerated(EnumType.STRING)
//    private com.optus.infosec.domain.enums.Status status;
//
//    public Long getStatusTypeId() {
//        return statusTypeId;
//    }
//
//    public void setStatusTypeId(Long statusTypeId) {
//        this.statusTypeId = statusTypeId;
//    }
//
//    public com.optus.infosec.domain.enums.Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(com.optus.infosec.domain.enums.Status status) {
//        this.status = status;
//    }
//
//    @Override
//    public String toString() {
//        return "Status{" +
//                "statusTypeId=" + statusTypeId +
//                ", status=" + status +
//                '}';
//    }
//}
//
