package com.sethu.ems.service.impl;

import com.sethu.ems.dto.EmployeeDto;
import com.sethu.ems.entity.Employee;
import com.sethu.ems.exception.ResourceAlreadyExistsException;
import com.sethu.ems.exception.ResourceNotFundException;
import com.sethu.ems.mapper.EmployeeMapper;
import com.sethu.ems.repository.EmployeeRepository;
import com.sethu.ems.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        String email = employeeDto.getEmailAddress();

        // Check if the email exists in the repository
        Employee existingEmployee = employeeRepository.findByEmailAddress(email);
        if (existingEmployee != null) {
            throw new ResourceAlreadyExistsException("Employee with email: " + email + " already exists");
        }

        // Map the EmployeeDto to an Employee entity
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Map the saved Employee entity back to a DTO and return it
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        Employee employee = employeeOptional.orElseThrow(() -> {
            if(!employeeOptional.isPresent()) {
                return new ResourceNotFundException("Employee with given id: " + employeeId + " does not exist");
            }
            return null;
        });
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {

        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());

    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFundException("Employee with given id: " + employeeId + " does not exist"));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmailAddress(updatedEmployee.getEmailAddress());
        employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFundException("Employee with given id: " + employeeId + " does not exist"));
        employeeRepository.deleteById(employeeId);
    }
}
