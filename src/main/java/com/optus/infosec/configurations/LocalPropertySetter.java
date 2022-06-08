package com.optus.infosec.configurations;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("local")
@Component
public class LocalPropertySetter {

    @PostConstruct
    void init() {
        System.setProperty("keycloak.base.url", "https://keycloak-oss-cloud.awshost.io/");
        System.setProperty("sra.realm.name", "cossmos");
        System.setProperty("sra.file.save.location", "/home/mihan/Desktop/");
        System.setProperty("sra.backend.grantType", "password");
        System.setProperty("sra.backend.clientId", "sraClient");
        System.setProperty("sra.backend.username", "sra_user");
        System.setProperty("sra.backend.password", "123");
        System.setProperty("sra.keycloak.jwk.token.store", "https://keycloak-oss-cloud.awshost.io/auth/realms/cossmos/protocol/openid-connect/certs");
    }

}
