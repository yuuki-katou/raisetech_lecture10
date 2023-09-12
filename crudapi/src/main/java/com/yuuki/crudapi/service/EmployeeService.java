package com.yuuki.crudapi.service;

import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int id) throws Exception;

    List<Employee> findByDepartment(String department);

    int createEmployee(EmployeeDto employeeDto);

    void updateEmployee(EmployeeDto employeeDto);

    void deleteEmployee(int id);
}


