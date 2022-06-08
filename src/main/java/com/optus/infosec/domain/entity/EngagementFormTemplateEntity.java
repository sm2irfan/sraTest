package com.optus.infosec.domain.entity;


import com.optus.infosec.domain.enums.EngagementForm;

import javax.persistence.*;
import java.util.Arrays;

/**
 * @author SM
 *
 * Entity to have default templates for engagement forms
 *
 */
@Entity
@Table(name = "engagement_form_template")
public class EngagementFormTemplateEntity {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long templateId;

    private String templateVersion;

    @Enumerated(EnumType.STRING)
    private EngagementForm templateFormName;

    @Lob
    private byte[] template;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    public EngagementForm getTemplateFormName() {
        return templateFormName;
    }

    public void setTemplateFormName(EngagementForm templateFormName) {
        this.templateFormName = templateFormName;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "EngagementFormTemplateEntity{" +
                "templateId=" + templateId +
                ", templateVersion='" + templateVersion + '\'' +
                ", templateFormName=" + templateFormName +
                ", template=" + Arrays.toString(template) +
                '}';
    }
}
