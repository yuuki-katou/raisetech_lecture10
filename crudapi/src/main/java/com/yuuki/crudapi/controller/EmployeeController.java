package com.yuuki.crudapi.controller;

import com.yuuki.crudapi.ResourceNotFoundException;
import com.yuuki.crudapi.dto.EmployeeDto;
import com.yuuki.crudapi.entity.Employee;
import com.yuuki.crudapi.form.EmployeeCreateForm;
import com.yuuki.crudapi.form.EmployeeUpdateForm;
import com.yuuki.crudapi.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 全ての従業員を取得，または特定の部署に所属する従業員を取得する
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

    // 特定のIDの従業員を取得する
    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable("id") int id) throws Exception {
        return employeeService.findById(id);
    }

    //新しい従業員を追加する
    @PostMapping("/employees")
    public ResponseEntity<Map<String, String>> addEmployee(@RequestBody EmployeeCreateForm employeeCreateForm) {

        //EmployeeCreateFormをEmployeeDtoに変換
        EmployeeDto employeeDto = employeeCreateForm.toDto();

        //登録処理の呼び出し、戻り値のId情報を格納
        int newEmployeeId = employeeService.createEmployee(employeeDto);

        //URIの生成
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEmployeeId)
                .toUri();

        //メッセージとURIをレスポンスとして返す
        return ResponseEntity.created(location).body(Map.of("message", "A new employee is created!"));
    }

    //IDによって既存の従業員の情報を更新する
    @PatchMapping("/employees/{id}")
    public ResponseEntity<Map<String, String>> updateEmployee(
            @PathVariable("id") int id,
            @RequestBody @Valid EmployeeUpdateForm employeeUpdateForm) {

        EmployeeDto employeeDto = employeeUpdateForm.toDto();
        employeeDto.setId(id);
        employeeService.updateEmployee(employeeDto);

        return ResponseEntity.ok(Map.of("message", "Employee with ID has been updated"));
    }

    // IDによって従業員を削除する
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployeeById(@PathVariable("id") int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Employee with ID has been deleted"));
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

    //バリデーションエラーが発生した際の例外ハンドラー
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // エラーメッセージとフィールド名をマップに保存
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 応答ボディを作成
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", ZonedDateTime.now().toString());
        body.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", "Validation failed");
        body.put("details", errors);
        body.put("path", request.getRequestURI());

        // 応答ボディとHTTPステータスを使用して、ResponseEntityを返す
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

