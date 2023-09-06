package com.yuuki.crudapi.form;

import java.util.Date;

public class EmployeeCreateForm {
    private int id;
    private String name;
    private Date birthdate;
    private String department;
    private String role;
    private String email;
    private String phone;

    public EmployeeCreateForm(int id, String name, Date birthdate, String department, String role, String email, String phone) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.department = department;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthdate() {
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
}

