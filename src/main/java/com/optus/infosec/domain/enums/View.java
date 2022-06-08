package com.optus.infosec.domain.enums;

/**
 * @author SM
 *
 * View Enums
 */
public enum View {

    INDEX("index"),
    ERROR_PAGE("error_page"),
    ENGAGEMENT_DASHBOARD("engagementDashboard"),
    ENGAGEMENT_LIST("engagementList");

    String name;

    View(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
