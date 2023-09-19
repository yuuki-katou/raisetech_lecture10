package com.yuuki.crudapi.service;

import com.yuuki.crudapi.ResourceNotFoundException;
import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;
import com.yuuki.crudapi.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    EmployeeServiceImpl employeeServiceImpl;

    @Mock
    EmployeeMapper employeeMapper;

    @Test
    public void 全ての従業員情報が記載されたリストが正常に返されること() {
        //テストデータの用意
        List<Employee> expectedEmployees = Arrays.asList(
                new Employee(1, "Taro Yamada", LocalDate.of(1990, 1, 1), "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890"),
                new Employee(2, "Hanako Tanaka", LocalDate.of(1995, 2, 2), "HR", "Staff", "hanako.tanaka@example.com", "023-4567-8901")
        );

        //モックの動作を定義
        doReturn(expectedEmployees).when(employeeMapper).findAll();

        //メソッドを実行
        List<Employee> actual = employeeServiceImpl.findAll();
        
        //結果の検証
        assertThat(actual).isEqualTo(expectedEmployees);
    }

    @Test
    public void 存在するIDを指定した場合に従業員情報が正常に返されること() {
        //テストデータの用意
        Employee expectedEmployee = new Employee(1, "Taro Yamada", LocalDate.of(1990, 1, 1),
                "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890");

        // モックの動作を定義
        doReturn(Optional.of(expectedEmployee)).when(employeeMapper).findById(1);

        // メソッドを実行
        Employee actual = employeeServiceImpl.findById(1);

        // 結果を検証
        assertThat(actual).isEqualTo(expectedEmployee);
    }

    @Test
    public void 存在しないIDを指定した場合にResourceNotFoundExceptionが投げられること() {
        //モックの動作を定義
        doReturn(Optional.empty()).when(employeeMapper).findById(999);

        //メソッドの実行＆結果の検証
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.findById(999));
    }

    @Test
    public void 存在する部署名を指定した場合に正常に従業員リストが返されること() {
        //モックデータの用意
        String testDepartment = "Sales";
        List<Employee> expectedEmployees = Arrays.asList(
                new Employee(1, "Taro Yamaha", LocalDate.of(1990, 1, 1),
                        "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890"));

        //モックの動作を定義
        doReturn(expectedEmployees).when(employeeMapper).findByDepartment(testDepartment);

        //メソッドの実行
        List<Employee> actual = employeeServiceImpl.findByDepartment(testDepartment);

        //結果を検証
        assertThat(actual).isEqualTo(expectedEmployees);
    }

    @Test
    public void 存在しない部署名を指定したときにResourceNotFoundが投げられること() {
        //テストデータの用意
        String testDepartment = "NonExistentDepartment";
        //モックの用意
        doReturn(Collections.emptyList()).when(employeeMapper).findByDepartment(testDepartment);

        //メソッドの実行＆結果の検証
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.findByDepartment(testDepartment));
    }


    @Test
    public void 従業員情報の更新時に正確な属性でデータベースが更新されること() {
        // 既存データをモック
        doReturn(Optional.of(new Employee(1, "Taro", LocalDate.of(1990, 1, 1), "Sales", "Manager", "taro.yamada@example.com", "012-3456-7890")))
                .when(employeeMapper).findById(1);

        // 更新データを設定
        EmployeeDto updateDto = new EmployeeDto(1, "Taro Yamaha", LocalDate.of(2000, 1, 1), "Engineering", "Engineer", "taro.yamada@example.com", "012-3456-7890");

        // 更新操作実行
        employeeServiceImpl.updateEmployee(updateDto);

        // 更新操作の検証
        verify(employeeMapper).updateEmployee(argThat(employee ->
                employee.getName().equals("Taro Yamaha") &&
                        employee.getBirthdate().equals(LocalDate.of(2000, 1, 1)) &&
                        employee.getDepartment().equals("Engineering") &&
                        employee.getRole().equals("Engineer") &&
                        employee.getEmail().equals("taro.yamada@example.com") &&
                        employee.getPhone().equals("012-3456-7890")
        ));
    }


    @Test
    public void 存在しない従業員情報を更新しようとしたときにResourceNotFoundExceptionが投げられること() {
        EmployeeDto employeeDto = new EmployeeDto(999, "Taro", LocalDate.of(2000, 1, 1), "Engineering", "Engineer", "taro@email.com", "1234567890");
        doReturn(Optional.empty()).when(employeeMapper).findById(999);
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.updateEmployee(employeeDto));
    }

    @Test
    public void 新規従業員作成時にIDが正常に返されること() {
        //テストデータの用意
        EmployeeDto employeeDto = new EmployeeDto(0, "Taro", LocalDate.of(2000, 01, 01), "Engineering", "Engineer", "taro@email.com", "1234567890");
        Employee createdEmployee = new Employee(1, "Taro", LocalDate.of(2000, 01, 01), "Engineering", "Engineer", "taro@email.com", "1234567890");

        //モックの定義
        doAnswer(invocation -> {
            Employee argEmployee = invocation.getArgument(0);
            ReflectionTestUtils.setField(argEmployee, "id", createdEmployee.getId());
            return null; // voidメソッドなのでnullを返す
        }).when(employeeMapper).create(any(Employee.class));

        //メソッドの実行
        int returnedId = employeeServiceImpl.createEmployee(employeeDto);

        //結果の検証
        assertThat(returnedId).isEqualTo(createdEmployee.getId());
    }

    @Test
    public void 存在しない従業員情報を削除しようとしたときにResourceNotFoundExceptionが投げられること() {
        doReturn(Optional.empty()).when(employeeMapper).findById(999);

        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.deleteEmployee(999));
    }
}



