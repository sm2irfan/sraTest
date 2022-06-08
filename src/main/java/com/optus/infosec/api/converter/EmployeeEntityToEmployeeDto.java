package com.optus.infosec.api.converter;

import com.optus.infosec.api.dto.EmployeeDTO;
import com.optus.infosec.domain.entity.EmployeeEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * @author PL
 *
 * Converter Class EmployeeEntity to EmployeeDTO
 *
 */
public class EmployeeEntityToEmployeeDto implements Converter<EmployeeEntity, EmployeeDTO>{

    @Override
    public EmployeeDTO convert(EmployeeEntity source) {

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setEmployeeId(source.getEmployeeId());
        employeeDTO.setFirstName(source.getFirstName());
        employeeDTO.setLastName(source.getLastName());
        employeeDTO.setPosition(source.getPosition());
        employeeDTO.setEmail(source.getEmail());
        employeeDTO.setCompany(source.getCompany());
        employeeDTO.setImgUrl(source.getImgUrl());

        return employeeDTO;
    }
}