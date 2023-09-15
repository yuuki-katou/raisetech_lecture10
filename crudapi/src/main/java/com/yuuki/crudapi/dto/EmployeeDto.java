package com.yuuki.crudapi.dto;

import com.yuuki.crudapi.entity.Employee;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class EmployeeDto {
    private int id;
    private String name;
    private LocalDate birthdate;
    private String department;
    private String role;
    private String email;
    private String phone;

    public EmployeeDto(int id, String name, LocalDate birthdate, String department, String role, String email, String phone) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.department = department;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public EmployeeDto() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Employee toEmployee() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Employee.class);
    }
}
