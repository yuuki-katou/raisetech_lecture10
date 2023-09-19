package com.yuuki.crudapi.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.yuuki.crudapi.entity.Employee;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeMapperTest {

    @Autowired
    EmployeeMapper employeeMapper;

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void すべてのユーザーが取得できること() {
        List<Employee> employeeList = employeeMapper.findAll();
        assertThat(employeeList)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(
                        new Employee(1, "Taro Yamada", LocalDate.of(1990, 1, 1),
                                "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890"),
                        new Employee(2, "Hanako Tanaka", LocalDate.of(1995, 2, 2),
                                "Sales", "Staff", "hanako.tanaka@example.com", "023-4567-8901")
                );

    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 部署名で該当する従業員の情報を取得できること() {
        List<Employee> result = employeeMapper.findByDepartment("Sales");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Taro Yamada");
        assertThat(result.get(1).getName()).isEqualTo("Hanako Tanaka");
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 部署名で該当する従業員の情報がないとき空のリストが返される() {
        List<Employee> result = employeeMapper.findByDepartment("HR");
        assertThat(result).hasSize(0);
        assertThat(result).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void IDで指定した従業員の情報を取得できること() {

        Optional<Employee> employee = employeeMapper.findById(1);
        Employee expectedEmployee = new Employee(1, "Taro Yamada", LocalDate.of(1990, 1, 1),
                "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890");

        assertThat(employee)
                .isPresent()
                .get().usingRecursiveComparison()
                .isEqualTo(expectedEmployee);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 指定したIDが存在しない場合に返ってくるデータが空であること() {
        int targetId = 100;
        Optional<Employee> employee = employeeMapper.findById(targetId);
        assertThat(employee).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterInsert.yml", ignoreCols = "id")
    @Transactional
    void 新しい従業員情報が登録されること() {
        Employee employee = new Employee(3, "Ichiro Sato", LocalDate.of(1990, 3, 3),
                "Information Systems", "System Engineer", "ichiro.sato@example.com", "012-3456-7890");
        employeeMapper.create(employee);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterUpdate.yml")
    @Transactional
    void 従業員の情報が更新できること() {
        Employee employee = new Employee(2, "Hanako Tanaka", LocalDate.of(1995, 2, 2), "Information Systems",
                "System Engineer", "hanako.tanaka@example.com", "023-4567-8901"
        );
        employeeMapper.updateEmployee(employee);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterDelete.yml")
    @Transactional
    void 従業員の情報を削除できること() {
        int targetId = 2;
        employeeMapper.deleteById(targetId);
    }
}

