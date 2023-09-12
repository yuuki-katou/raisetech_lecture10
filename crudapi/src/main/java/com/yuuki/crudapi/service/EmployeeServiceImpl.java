package com.yuuki.crudapi.service;

import com.yuuki.crudapi.ResourceNotFoundException;
import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;
import com.yuuki.crudapi.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }


    // すべての従業員情報をデータベースから取得する
    @Override
    public List<Employee> findAll() {
        return employeeMapper.findAll();
    }


    // 指定されたIDを持つ従業員をデータベースから取得する
    // 存在しない場合はResourceNotFoundExceptionを投げる
    @Override
    public Employee findById(int id) {
        return this.employeeMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
    }


    // 指定された部署の従業員をデータベースから取得する
    // 結果が空の場合はResourceNotFoundExceptionを投げる
    @Override
    public List<Employee> findByDepartment(String department) {
        List<Employee> employee = this.employeeMapper.findByDepartment(department);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return employee;
    }


    // 従業員の情報をデータベースに登録する
    // EmployeeDtoオブジェクトをEmployeeエンティティにマッピングし、その後データベースに保存する
    @Override
    public int createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeDto.toEmployee();
        employeeMapper.create(employee);
        return employee.getId();
    }


    // 従業員の情報をデータベースで更新する
    // 更新前に従業員が存在するか確認し、存在しない場合はResourceNotFoundExceptionを投げる
    @Override
    public void updateEmployee(EmployeeDto employeeDto) {
        employeeMapper.findById(employeeDto.getId()).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        Employee employee = employeeDto.toEmployee();
        employeeMapper.updateEmployee(employee);
    }


    // 指定されたIDを持つ従業員をデータベースから削除する
    // 削除前に従業員が存在するか確認し、存在しない場合はResourceNotFoundExceptionを投げる
    @Override
    public void deleteEmployee(int id) {
        employeeMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        employeeMapper.deleteById(id);
    }
}