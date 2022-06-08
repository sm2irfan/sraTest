package com.optus.infosec.api.service;

import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.domain.entity.EmployeeEntity;
import com.optus.infosec.domain.entity.InfosecUsersEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Value("#{InfosecUsersService.infosecUsersEntityList}")
    private List<InfosecUsersEntity> infosecUsersEntityList;

    @Value("#{InfosecUsersService.employeeEntityList}")
    private List<EmployeeEntity> employeeEntityList;

    public String getEmailFromEngagementDto(EngagementDTO engagementDTO) {
        return infosecUsersEntityList
                .stream()
                .filter(user -> user.getEmployeeId().equals(engagementDTO.getAssignedTo()))
                .map(InfosecUsersEntity::getEmail)
                .collect(Collectors.toList())
                .get(0);
    }

    public EmployeeEntity getEmployeeEntityFromId(String assignedTo) {
        return employeeEntityList
                .stream()
                .filter(emp -> emp.getEmployeeId().equals(assignedTo))
                .collect(Collectors.toList())
                .get(0);
    }


}
