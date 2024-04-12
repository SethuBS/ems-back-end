package com.sethu.ems.repository;

import com.sethu.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository  extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
}
