package com.optus.infosec.api.dto;


import com.optus.infosec.domain.enums.EngagementForm;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author SM
 *
 * Engagement Form Transfer Object
 */
@AllArgsConstructor
@NoArgsConstructor
public class EngagementFormDTO {

    private Long engagementFormId;

    private EngagementForm engagementFormName;

    private String formTemplate;

    private String formData;


    public Long getEngagementFormId() {
        return engagementFormId;
    }

    public void setEngagementFormId(Long engagementFormId) {
        this.engagementFormId = engagementFormId;
    }

    public EngagementForm getEngagementFormName() {
        return engagementFormName;
    }

    public void setEngagementFormName(EngagementForm engagementFormName) {
        this.engagementFormName = engagementFormName;
    }

    public String getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(String formTemplate) {
        this.formTemplate = formTemplate;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EngagementFormDTO that = (EngagementFormDTO) o;

        if (getEngagementFormId() != null ? !getEngagementFormId().equals(that.getEngagementFormId()) : that.getEngagementFormId() != null)
            return false;
        if (getEngagementFormName() != that.getEngagementFormName()) return false;
        if (getFormTemplate() != null ? !getFormTemplate().equals(that.getFormTemplate()) : that.getFormTemplate() != null)
            return false;
        return !(getFormData() != null ? !getFormData().equals(that.getFormData()) : that.getFormData() != null);

    }

    @Override
    public int hashCode() {
        int result = getEngagementFormId() != null ? getEngagementFormId().hashCode() : 0;
        result = 31 * result + (getEngagementFormName() != null ? getEngagementFormName().hashCode() : 0);
        result = 31 * result + (getFormTemplate() != null ? getFormTemplate().hashCode() : 0);
        result = 31 * result + (getFormData() != null ? getFormData().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EngagementFormDTO{" +
                "engagementFormId=" + engagementFormId +
                ", engagementFormName=" + engagementFormName +
                ", formTemplate='" + formTemplate + '\'' +
                ", formData='" + formData + '\'' +
                '}';
    }
}
