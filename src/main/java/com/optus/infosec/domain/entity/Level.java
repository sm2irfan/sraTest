//package com.optus.infosec.domain.entity;
//
//
//import javax.persistence.*;
//
//
///**
// * @author SM
// *
// * Entity for level - LOW, , MEDIUM, HIGH
// */
//
//@Entity
//@Table(name = "level_type")
//public class Level {
//
//    @Id
//    @Column(nullable = false, unique = true)
//    private Long levelTypeId;
//
//    @Enumerated(EnumType.STRING)
//    private com.optus.infosec.domain.enums.Level level;
//
//    public Long getLevelTypeId() {
//        return levelTypeId;
//    }
//
//    public void setLevelTypeId(Long levelTypeId) {
//        this.levelTypeId = levelTypeId;
//    }
//
//    public com.optus.infosec.domain.enums.Level getLevel() {
//        return level;
//    }
//
//    public void setLevel(com.optus.infosec.domain.enums.Level level) {
//        this.level = level;
//    }
//
//    @Override
//    public String toString() {
//        return "Level{" +
//                "levelTypeId=" + levelTypeId +
//                ", level=" + level +
//                '}';
//    }
//}
//
