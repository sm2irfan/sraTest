package com.optus.infosec.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.optus.infosec.domain.enums.EngagementForm;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author SM
 *
 * Engagement Form Entity - To store json of engagement forms
 */
@Entity
@Table(name = "engagement_form")
public class EngagementFormEntity {

    @ManyToOne
    @JoinColumn(name = "engagementId")
    private EngagementEntity engagementEntity;

    @Enumerated(EnumType.STRING)
    private EngagementForm engagementFormName;

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long engagementFormId;

    @Lob
    private byte[] formTemplate;

    @Lob
    private byte[] formData;

    private LocalDateTime createdDatetime;

    private LocalDateTime modifiedDatetime;

    public EngagementFormEntity() {
    }

    public EngagementFormEntity(EngagementEntity engagementEntity, EngagementForm engagementFormName, Long engagementFormId, byte[] formTemplate, byte[] formData,
                                LocalDateTime createdDatetime, LocalDateTime modifiedDatetime) {
        this.engagementEntity = engagementEntity;
        this.engagementFormName = engagementFormName;
        this.engagementFormId = engagementFormId;
        this.formTemplate = formTemplate;
        this.formData = formData;
        this.createdDatetime = createdDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public EngagementEntity getEngagementEntity() {
        return engagementEntity;
    }

    public void setEngagementEntity(EngagementEntity engagementEntity) {
        this.engagementEntity = engagementEntity;
    }

    public EngagementForm getEngagementFormName() {
        return engagementFormName;
    }

    public void setEngagementFormName(EngagementForm engagementFormName) {
        this.engagementFormName = engagementFormName;
    }

    public Long getEngagementFormId() {
        return engagementFormId;
    }

    public void setEngagementFormId(Long engagementFormId) {
        this.engagementFormId = engagementFormId;
    }

    public byte[] getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(byte[] formTemplate) {
        this.formTemplate = formTemplate;
    }

    public byte[] getFormData() {
        return formData;
    }

    public void setFormData(byte[] formData) {
        this.formData = formData;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public LocalDateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(LocalDateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    @Override
    public String toString() {
        return "EngagementFormEntity{" +
                "engagementEntity=" + engagementEntity +
                ", engagementFormName=" + engagementFormName +
                ", engagementFormId=" + engagementFormId +
                ", formTemplate=" + Arrays.toString(formTemplate) +
                ", formData=" + Arrays.toString(formData) +
                ", createdDatetime=" + createdDatetime +
                ", modifiedDatetime=" + modifiedDatetime +
                '}';
    }
}
