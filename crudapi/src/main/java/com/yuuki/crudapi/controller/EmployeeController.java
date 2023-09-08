package com.yuuki.crudapi.controller;

import com.yuuki.crudapi.ResourceNotFoundException;
import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;
import com.yuuki.crudapi.form.EmployeeCreateForm;
import com.yuuki.crudapi.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;


@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 全ての従業員を取得，または特定の部署に所属する従業員を取得するメソッド
    @GetMapping("/employees")
    public List<EmployeeResponse> getEmployees(@RequestParam(value = "department", required = false) String department) {
        List<Employee> employees;
        if (department == null || department.isEmpty()) {
            employees = employeeService.findAll();
        } else {
            employees = employeeService.findByDepartment(department);
        }
        return employees.stream().map(EmployeeResponse::new).toList();
    }

    // 特定のIDの従業員を取得するメソッド
    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable("id") int id) throws Exception {
        return employeeService.findById(id);
    }

    //従業員の情報を登録するメソッド
    @PostMapping("/employees")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeCreateForm employeeCreateForm) {

        //ModelMapperインスタンスを作成し、EmployeeCreateFormをEmployeeDtoに変換
        ModelMapper modelMapper = new ModelMapper();
        EmployeeDto employeeDto = modelMapper.map(employeeCreateForm, EmployeeDto.class);

        //登録処理の呼び出し、戻り値のId情報を格納
        int newEmployeeId = employeeService.createEmployee(employeeDto);

        //URIの生成
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEmployeeId)
                .toUri();

        //メッセージとURIをレスポンスとして返す
        return ResponseEntity.created(location).body("a new employee is created!!");
    }

    // ResourceNotFoundException がスローされた場合のハンドラ
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(
            ResourceNotFoundException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
