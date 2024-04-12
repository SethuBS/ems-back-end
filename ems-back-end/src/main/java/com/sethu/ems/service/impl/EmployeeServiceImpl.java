package com.sethu.ems.service.impl;

import com.sethu.ems.dto.EmployeeDto;
import com.sethu.ems.entity.Employee;
import com.sethu.ems.exception.ResourceNotFundException;
import com.sethu.ems.mapper.EmployeeMapper;
import com.sethu.ems.repository.EmployeeRepository;
import com.sethu.ems.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new ResourceNotFundException("Employee with given id: "+employeeId+" does not exist"));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }
}
