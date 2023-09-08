package com.yuuki.crudapi.mapper;

import com.yuuki.crudapi.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmployeeMapper {
    // MyBatis の Select アノテーションを使って、全ての従業員を取得するSQLクエリを定義
    @Select("SELECT * FROM employees")
    List<Employee> findAll();

    // 特定のIDの従業員を取得するSQLクエリを定義
    @Select("SELECT * FROM employees WHERE id = #{id}")
    Optional<Employee> findById(int id);

    // 特定の部署の従業員を取得するSQLクエリを定義
    @Select("SELECT * FROM employees WHERE department = #{department}")
    List<Employee> findByDepartment(String department);

    //従業員の情報を登録するSQLクエリを定義
    @Insert("INSERT INTO employees (name,birthdate,department,role,email,phone) VALUES (#{name},#{birthdate},#{department},#{role},#{email},#{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Employee employee);
}
