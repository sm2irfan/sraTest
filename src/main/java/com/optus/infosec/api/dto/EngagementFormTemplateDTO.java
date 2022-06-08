package com.optus.infosec.api.dto;


import com.optus.infosec.domain.enums.EngagementForm;

/**
 * @author SM
 *
 * Data Transfer Class for Engagement Form Template
 */
public class EngagementFormTemplateDTO {

    private Long templateId;

    private String templateVersion;

    private EngagementForm templateFormName;

    private String template;

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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EngagementFormTemplateDTO that = (EngagementFormTemplateDTO) o;

        if (getTemplateId() != null ? !getTemplateId().equals(that.getTemplateId()) : that.getTemplateId() != null)
            return false;
        if (getTemplateVersion() != null ? !getTemplateVersion().equals(that.getTemplateVersion()) : that.getTemplateVersion() != null)
            return false;
        if (getTemplateFormName() != that.getTemplateFormName()) return false;
        return !(getTemplate() != null ? !getTemplate().equals(that.getTemplate()) : that.getTemplate() != null);

    }

    @Override
    public int hashCode() {
        int result = getTemplateId() != null ? getTemplateId().hashCode() : 0;
        result = 31 * result + (getTemplateVersion() != null ? getTemplateVersion().hashCode() : 0);
        result = 31 * result + (getTemplateFormName() != null ? getTemplateFormName().hashCode() : 0);
        result = 31 * result + (getTemplate() != null ? getTemplate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EngagementFormTemplateDTO{" +
                "templateId=" + templateId +
                ", templateVersion='" + templateVersion + '\'' +
                ", templateFormName=" + templateFormName +
                ", template='" + template + '\'' +
                '}';
    }
}
