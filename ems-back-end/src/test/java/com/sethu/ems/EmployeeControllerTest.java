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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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

    @Test
    public void testGetAllEmployees() {
        // Create some dummy employee data
        List<EmployeeDto> dummyEmployees = Arrays.asList(
                new EmployeeDto(1L, "John", "Doe", "john@example.com"),
                new EmployeeDto(2L, "Jane", "Doe", "jane@example.com")
        );

        // Set up the behavior of the mocked service method
        when(employeeService.getAllEmployees()).thenReturn(dummyEmployees);

        // Create an instance of the controller and inject the mocked service
        EmployeeController controller = new EmployeeController(employeeService);

        // Call the controller method
        ResponseEntity<List<EmployeeDto>> responseEntity = controller.getAllEmployees();

        // Verify that the service method was called
        verify(employeeService, times(1)).getAllEmployees();

        // Assert the response status
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert the response body
        assertEquals(dummyEmployees, responseEntity.getBody());
    }
    @Test
    public void testUpdateEmployee() {
        // Given
        Long employeeId = 1L;
        EmployeeDto updatedEmployeeDto = new EmployeeDto(1L, "John", "Doe", "john@example.com");
        EmployeeDto expectedEmployeeDto = new EmployeeDto(1L, "John", "Doe", "john@example.com");

        // Mock the service method
        when(employeeService.updateEmployee(eq(employeeId), any(EmployeeDto.class)))
                .thenReturn(expectedEmployeeDto);

        // When
        ResponseEntity<EmployeeDto> responseEntity = employeeController.updateEmployee(employeeId, updatedEmployeeDto);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEmployeeDto, responseEntity.getBody());
    }

    @Test
    public void testDeleteEmployee() {
        // Given
        Long employeeId = 1L;

        // When
        ResponseEntity<String> responseEntity = employeeController.deleteEmployee(employeeId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Employee deleted successful", responseEntity.getBody());
        verify(employeeService).deleteEmployee(employeeId);
    }
}
