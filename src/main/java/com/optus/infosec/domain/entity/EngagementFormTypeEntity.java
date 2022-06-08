package com.optus.infosec.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author SM
 *
 * Entity for EngagementFormType - Summary, OWG, Platform Security etc.
 */
@Entity
@Table(name = "engagement_form_type")
public class EngagementFormTypeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private Long engagementFormTypeId;

    private String engagementFormName;

    public Long getEngagementFormTypeId() {
        return engagementFormTypeId;
    }

    public void setEngagementFormTypeId(Long engagementFormTypeId) {
        this.engagementFormTypeId = engagementFormTypeId;
    }

    public String getEngagementFormName() {
        return engagementFormName;
    }

    public void setEngagementFormName(String engagementFormName) {
        this.engagementFormName = engagementFormName;
    }

    @Override
    public String toString() {
        return "EngagementFormTypeEntity{" +
                "engagementFormTypeId='" + engagementFormTypeId + '\'' +
                ", engagementFormName='" + engagementFormName + '\'' +
                '}';
    }
}