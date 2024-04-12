package com.sethu.ems;

import com.sethu.ems.dto.EmployeeDto;
import com.sethu.ems.entity.Employee;
import com.sethu.ems.exception.ResourceAlreadyExistsException;
import com.sethu.ems.mapper.EmployeeMapper;
import com.sethu.ems.repository.EmployeeRepository;
import com.sethu.ems.service.EmployeeService;
import com.sethu.ems.service.impl.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @Before
    public void setup() {
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void testCreateEmployee_EmailExists() {
        // Mock existing email
        String existingEmail = "existing@example.com";
        Employee existingEmployee = new Employee();
        existingEmployee.setEmail(existingEmail);

        // Map employee to dto
        EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(existingEmployee);
        employeeDto.setEmail(existingEmail);

        // Mock repository behavior to return an existing employee
        when(employeeRepository.findByEmail(existingEmail)).thenReturn(existingEmployee);

        // Attempt to create employee with existing email should throw ResourceAlreadyExistsException
        employeeService.createEmployee(employeeDto);

        // Verify that findByEmail was called with the correct email
        verify(employeeRepository, times(1)).findByEmail(existingEmail);
    }
}
