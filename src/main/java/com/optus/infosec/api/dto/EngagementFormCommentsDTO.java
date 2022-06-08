package com.optus.infosec.api.dto;

import java.util.Date;

public class EngagementFormCommentsDTO {
	
    private Long commentId;
    
    private Long engagementId;
    
    private Long engagementFormId;
    
    private String commentItem;

    private String commentBy;
    
    private Date commentDate;

    private String comment;
    
    private String infosecViewed;
    
    private String requestorViewed;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public String getCommentBy() {
		return commentBy;
	}

	public void setCommentBy(String commentBy) {
		this.commentBy = commentBy;
	}

	public String getCommentItem() {
		return commentItem;
	}

	public void setCommentItem(String commentItem) {
		this.commentItem = commentItem;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInfosecViewed() {
		return infosecViewed;
	}

	public void setInfosecViewed(String infosecViewed) {
		this.infosecViewed = infosecViewed;
	}

	public String getRequestorViewed() {
		return requestorViewed;
	}

	public void setRequestorViewed(String requestorViewed) {
		this.requestorViewed = requestorViewed;
	}

	@Override
	public String toString() {
		return "EngagementFormCommentsDTO [commentId=" + commentId + ", engagementId=" + engagementId
				+ ", engagementFormId=" + engagementFormId + ", commentBy=" + commentBy + ", commentItem=" + commentItem
				+ ", commentDate=" + commentDate + ", comment=" + comment + ", infosecViewed=" + infosecViewed
				+ ", requestorViewed=" + requestorViewed + "]";
	}
    
}
