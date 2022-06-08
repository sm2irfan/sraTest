package com.optus.infosec.api.controller;

import com.optus.infosec.api.dto.EmployeeDTO;
import com.optus.infosec.api.service.TokenDetailService;
import com.optus.infosec.domain.entity.InfosecUsersEntity;
import com.optus.infosec.domain.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WebController {

    private static final Logger LOG = LoggerFactory.getLogger(WebController.class);

    @Value("#{InfosecUsersService.infosecUsersEntityList}")
    private List<InfosecUsersEntity> infosecUsersEntityList;

    private final TokenDetailService tokenDetailService;

    public WebController(
            TokenDetailService tokenDetailService) {
        this.tokenDetailService = tokenDetailService;
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/getSessionUserRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRole> getSessionUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        EmployeeDTO employeeDto = (EmployeeDTO) session.getAttribute("employee_data");
        // TODO: if null redirect to root for login
        return new ResponseEntity<>((UserRole.ADMIN), HttpStatus.OK);
    }

    /**
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getSessionEmpData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDTO> getSessionEmpData(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(tokenDetailService.getEmployeeDtoFromToken(), HttpStatus.OK);
    }

    /**
     * Get Operation for Engagements
     *
     * @return ResponseEntity<EngagementEntity>
     */
    @RequestMapping(value = "/getInfosecUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InfosecUsersEntity>> getInfosecUsers() {
        LOG.debug("Get All InfosecUsers ");
        //need to call keycloak admin api
        return new ResponseEntity<>(infosecUsersEntityList, HttpStatus.OK);
    }
}
