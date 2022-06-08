package com.optus.infosec.api.service;

import com.optus.infosec.api.dto.EmployeeDTO;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class TokenDetailService {

    private final HttpServletRequest servletRequest;

    public TokenDetailService(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public EmployeeDTO getEmployeeDtoFromToken() {
        var token = (JwtAuthenticationToken) servletRequest.getUserPrincipal();
        Map<String, Object> tokenAttributeMap = token.getTokenAttributes();
        var employeeId = String.valueOf(tokenAttributeMap.get("employeeId"));
        var firstName = String.valueOf(tokenAttributeMap.get("firstName"));
        var lastName = String.valueOf(tokenAttributeMap.get("lastName"));
        var position = String.valueOf(tokenAttributeMap.get("position"));
        var email = String.valueOf(tokenAttributeMap.get("email"));
        var company = String.valueOf(tokenAttributeMap.get("company"));
        var imgUrl = String.valueOf(tokenAttributeMap.get("imgUrl"));
        var userRole = String.valueOf(tokenAttributeMap.get("realm_access"));
        var isInfosecUser = String.valueOf(tokenAttributeMap.get("isInfosecUser"));
        return new EmployeeDTO(employeeId, firstName, lastName, position, email, company, imgUrl, userRole, isInfosecUser);
    }

}
