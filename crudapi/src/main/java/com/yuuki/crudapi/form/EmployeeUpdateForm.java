package com.yuuki.crudapi.form;

import com.yuuki.crudapi.dto.EmployeeDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class EmployeeUpdateForm {
    @NotBlank(message = "名前は必須です。")
    @Size(max = 50, message = "名前は50文字以内で入力してください。")
    private String name;

    @NotNull(message = "生年月日は必須です。")
    @Past(message = "未来の日付は入力できません。")
    private LocalDate birthdate;

    @NotBlank(message = "部署は必須です。")
    @Size(max = 100, message = "部署は100文字以内で入力してください。")
    private String department;

    @NotBlank(message = "役職は必須です。")
    @Size(max = 100, message = "役職は100文字以内で入力してください。")
    private String role;

    @NotBlank(message = "メールアドレスは必須です。")
    @Email(message = "無効なメールアドレスの形式です。")
    @Size(max = 150, message = "メールアドレスは150文字以内で入力してください。")
    private String email;

    @NotBlank(message = "電話番号は必須です。")
    @Pattern(regexp = "^[0-9\\-+()]*$", message = "無効な電話番号の形式です。")
    @Size(max = 15, message = "電話番号は15桁以内で入力してください。")
    private String phone;

    public EmployeeUpdateForm(String name, LocalDate birthdate, String department, String role, String email, String phone) {
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

    public EmployeeDto toDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, EmployeeDto.class);
    }
}
