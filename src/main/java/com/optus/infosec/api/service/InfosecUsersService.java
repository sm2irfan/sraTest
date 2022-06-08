package com.optus.infosec.api.service;

import com.optus.infosec.api.service.wrapper.RealmTokenWrapper;
import com.optus.infosec.domain.entity.EmployeeEntity;
import com.optus.infosec.domain.entity.InfosecUsersEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("InfosecUsersService")
public class InfosecUsersService {

    private static final Logger LOG = LoggerFactory.getLogger(InfosecUsersService.class);

    @Autowired
    private RestTemplate restTemplate;

    public static final List<InfosecUsersEntity> infosecUsersEntityList = new ArrayList<>();

    public static final List<EmployeeEntity> employeeEntityList = new ArrayList<>();

    public void getUsersFromKeycloak() {

        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Getting infosec users from keycloak.....");

        RealmTokenWrapper realmTokenWrapper = getAccessTokenForSraApp();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + realmTokenWrapper.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String keycloakServerBaseUrl = System.getProperty("keycloak.base.url");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(
                new StringBuilder(keycloakServerBaseUrl)
                        .append("auth/admin/realms/")
                        .append(realmTokenWrapper.getRealm())
                        .append("/users")
                        .toString(),
                HttpMethod.GET, entity,
                Object.class);

        ArrayList userList = Optional.ofNullable((ArrayList) responseEntity.getBody()).orElse(null);

        JSONArray jsArray = new JSONArray(userList);

        for (Object item : jsArray) {
            try {
                JSONObject jsonObject = new JSONObject(item.toString());

                String attributes = String.valueOf(jsonObject.has("attributes") ? jsonObject.getJSONObject("attributes") : "");

                if (!attributes.isBlank() && new JSONObject(attributes).has("employeeId")) {

                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> attributes, {}", attributes);

                    String firstName = String.valueOf(jsonObject.has("firstName") ? jsonObject.get("firstName") : "");
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> firstName, {}", firstName);

                    String email = String.valueOf(jsonObject.has("email") ? jsonObject.get("email") : "");
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> email, {}", email);

                    String lastName = String.valueOf(jsonObject.has("lastName") ? jsonObject.get("lastName") : "");
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> lastName, {}", lastName);

                    String employeeId = String.valueOf(new JSONObject(attributes).getJSONArray("employeeId").get(0));
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> employee id, {}", employeeId);

                    String position = String.valueOf(new JSONObject(attributes).getJSONArray("position").get(0));
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> position, {}", position);

                    String company = String.valueOf(new JSONObject(attributes).getJSONArray("company").get(0));
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> company, {}", company);

                    String imgUrl = String.valueOf(new JSONObject(attributes).getJSONArray("company").get(0));
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> imgUrl, {}", imgUrl);

                    EmployeeEntity employeeEntity = new EmployeeEntity(employeeId, firstName, lastName, position, email, company, imgUrl);
                    LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> employeeEntity, {}", employeeEntity);

                    employeeEntityList.add(employeeEntity);

                    if (String.valueOf(new JSONObject(attributes).getJSONArray("isInfosecUser").get(0)).equalsIgnoreCase("Y")) {
                        InfosecUsersEntity infosecUsersEntity = new InfosecUsersEntity(employeeId, firstName, lastName, email);
                        infosecUsersEntityList.add(infosecUsersEntity);
                        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> infosecUsersEntity, {}", infosecUsersEntity);
                    }

                }
            } catch (Exception e) {
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error retrieving infosec users from keycloak, {}", Arrays.toString(e.getStackTrace()));
            }
        }

        LOG.debug(">>>>>>>>>>>>>>>> Employee Entity List from keycloak : {}", employeeEntityList);
        LOG.debug(">>>>>>>>>>>>>>>> Infosec Entity List from keycloak : {}", infosecUsersEntityList);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Retrieved, {} users from keycloak", infosecUsersEntityList.size());
    }

    private RealmTokenWrapper getAccessTokenForSraApp() {

        LOG.debug("Getting access token for the SRA app....");

        String keycloakServerBaseUrl = System.getProperty("keycloak.base.url");
        String realmName = System.getProperty("sra.realm.name");
        String grantType = System.getProperty("sra.backend.grantType");
        String clientId = System.getProperty("sra.backend.clientId");
        String username = System.getProperty("sra.backend.username");
        String password = System.getProperty("sra.backend.password");
        String clientSecret = System.getProperty("sra.backend.clientSecret");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("username", username);
        map.add("password", password);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<LinkedHashMap> responseEntity = restTemplate.postForEntity(
                new StringBuilder(keycloakServerBaseUrl)
                        .append("auth/realms/")
                        .append(realmName)
                        .append("/protocol/openid-connect/token")
                        .toString(),
                entity, LinkedHashMap.class);


        Optional<LinkedHashMap> body = Optional.ofNullable(responseEntity.getBody());
        if (body.isPresent()) {
            LOG.debug("Got the access token for SRA Server successfully !");
            return new RealmTokenWrapper(realmName, String.valueOf(body.get().get("access_token")));
        } else {
            LOG.debug("Failed to retrieve access token from keycloak for SRA Server !");
            return null;
        }
    }


}
