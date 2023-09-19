package com.yuuki.crudapi.form;

import com.yuuki.crudapi.dto.EmployeeDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class EmployeeCreateForm {

    private String name;
    private LocalDate birthdate;
    private String department;
    private String role;
    private String email;
    private String phone;

    public EmployeeCreateForm(String name, LocalDate birthdate, String department, String role, String email, String phone) {
        this.name = name;
        this.birthdate = birthdate;
        this.department = department;
        this.role = role;
        this.email = email;
        this.phone = phone;
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
    

    public EmployeeDto toDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, EmployeeDto.class);
    }
}

