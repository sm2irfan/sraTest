package com.optus.infosec.api.dto;

import java.util.Date;

public class EngagementFormTrackingDTO {
	
    private Long trackingId;

    private Long engagementId;
    
    private Long engagementFormId;
    
    private String trackingItem;
    
    private String trackingUser;

    private Date trackingDate;

    private String trackingField;
    
    private String oldValue;
    
    private String newValue;

	public Long getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
	}
	
	public Long getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(Long engagementId) {
		this.engagementId = engagementId;
	}

	public Long getEngagementFormId() {
		return engagementFormId;
	}

	public void setEngagementFormId(Long engagementFormId) {
		this.engagementFormId = engagementFormId;
	}

	public String getTrackingItem() {
		return trackingItem;
	}

	public void setTrackingItem(String trackingItem) {
		this.trackingItem = trackingItem;
	}

	public String getTrackingUser() {
		return trackingUser;
	}

	public void setTrackingUser(String trackingUser) {
		this.trackingUser = trackingUser;
	}

	public Date getTrackingDate() {
		return trackingDate;
	}

	public void setTrackingDate(Date trackingDate) {
		this.trackingDate = trackingDate;
	}

	public String getTrackingField() {
		return trackingField;
	}

	public void setTrackingField(String trackingField) {
		this.trackingField = trackingField;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public String toString() {
		return "EngagementFormTrackingDTO [trackingId=" + trackingId + ", engagementId=" + engagementId
				+ ", engagementFormId=" + engagementFormId + ", trackingItem=" + trackingItem + ", trackingUser="
				+ trackingUser + ", trackingDate=" + trackingDate + ", trackingField=" + trackingField + ", oldValue="
				+ oldValue + ", newValue=" + newValue + "]";
	}
    
}
