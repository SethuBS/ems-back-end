package com.sethu.ems;


import com.sethu.ems.controller.EmployeeController;
import com.sethu.ems.dto.EmployeeDto;
import com.sethu.ems.exception.ResourceNotFundException;
import com.sethu.ems.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testCreateEmployee() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto(1L, "John", "Doe", "john@example.com");
        when(employeeService.createEmployee(employeeDto)).thenReturn(employeeDto);

        // When
        ResponseEntity<EmployeeDto> responseEntity = employeeController.createEmployee(employeeDto);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(employeeDto, responseEntity.getBody());

        // Verify that the service method was called with the correct argument
        verify(employeeService, times(1)).createEmployee(employeeDto);
    }

    @Test
    public void testGetEmployeeById() {
        // Mock data
        Long employeeId = 1L;
        EmployeeDto expectedEmployeeDto = new EmployeeDto(employeeId, "John", "Doe", "john.doe@example.com");
        when(employeeService.getEmployeeById(employeeId)).thenReturn(expectedEmployeeDto);

        // Call the controller method
        ResponseEntity<EmployeeDto> responseEntity = employeeController.getEmployeeById(employeeId);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEmployeeDto, responseEntity.getBody());

        // Verify that the service method was called with the correct ID
        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test(expected = ResourceNotFundException.class)
    public void testGetEmployeeById_NotFound() {
        // Mock data
        Long employeeId = 1L;
        when(employeeService.getEmployeeById(employeeId)).thenThrow(ResourceNotFundException.class);

        // Call the controller method
        employeeController.getEmployeeById(employeeId);
    }

}
