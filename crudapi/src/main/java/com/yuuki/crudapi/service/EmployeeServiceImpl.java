package com.yuuki.crudapi.service;

import com.yuuki.crudapi.ResourceNotFoundException;
import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;
import com.yuuki.crudapi.mapper.EmployeeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    // 全ての従業員を取得するメソッド
    @Override
    public List<Employee> findAll() {
        return employeeMapper.findAll();
    }

    // 特定のIDの従業員を取得するメソッド
    @Override
    public Employee findById(int id) {
        return this.employeeMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
    }

    // 特定の部署の従業員を取得するメソッド
    @Override
    public List<Employee> findByDepartment(String department) {
        List<Employee> employee = this.employeeMapper.findByDepartment(department);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("resource not found");
        } else {
            return employee;
        }
    }

    //従業員の情報を登録するメソッド
    @Override
    public int createEmployee(EmployeeDto employeeDto) {
        //ModelMapperインスタンスを作成し、EmployeeDtoをEmployeeに変換
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDto, Employee.class);

        // 変換したEmployeeエンティティをデータベースに保存
        employeeMapper.create(employee);

        // 保存したEmployeeのIDを返す。
        return employee.getId();
    }
}

