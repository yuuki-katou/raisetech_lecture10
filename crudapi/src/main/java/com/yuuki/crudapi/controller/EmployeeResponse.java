package com.yuuki.crudapi.controller;

import com.yuuki.crudapi.entity.Employee;

public class EmployeeResponse {

    private String name;
    private String department;
    private String role;
    private String email;
    private String phone;

    public EmployeeResponse(Employee employee) {
        this.name = employee.getName();
        this.department = employee.getDepartment();
        this.role = employee.getRole();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
