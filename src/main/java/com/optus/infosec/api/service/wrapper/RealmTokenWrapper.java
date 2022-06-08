package com.optus.infosec.api.service.wrapper;

public class RealmTokenWrapper {

    private String realm;
    private String accessToken;

    public RealmTokenWrapper(String realm, String accessToken) {
        this.realm = realm;
        this.accessToken = accessToken;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
