package com.optus.infosec.domain.enums;

/**
 * @author SM
 *
 * Engagement Form Enum
 */

public enum EngagementForm {

    OVERVIEW("Overview"),
    SUMMARY("Summary"),
    SOLUTION_REVIEW("Solution Review"),
    DATA_PRIVACY("Data Privacy"),
    EA_REPO_MEGA("A Repo (MEGA)"),
    // OWG("OWG"),
    TSSR("TSSR"),
    PLATFORM_SECURITY("Platform Security"),
    CLOUD_THIRD_PARTY_HOSTING("Cloud/Third Party Hosting"),
    CUSTOMER_FACING_APPLICATION("Customer Facing Application"),
    SECURITY_TOOLS("Security Tools"),
    NETWORK_MANAGED_SYSTEMS("Network Managed Systems"),
    INTERFACE_CONTRACTS("Interface Contracts"),
    SOLUTION_ARCHITECTURE("Solution Architecture"),
    SECURITY_TESTING("Security Testing"),
    RISK_PRE_APPROVAL_FORM("Risk Pre-Approval Form"),
    RISK_FORM("Risk Form"),
    ENGAGEMENT_FORM_SEND("Engagement Form Send");

    private String label;

    EngagementForm(String label){
        this.label=label;
    }

    public String getLabel(){
        return label;
    }
}