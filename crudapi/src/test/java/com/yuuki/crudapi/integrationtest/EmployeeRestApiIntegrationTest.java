package com.yuuki.crudapi.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRestApiIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 従業員情報をすべて取得した際にステータスコード200を返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                   [
                       {
                           "name": "Taro Yamada",
                           "department": "Sales",
                           "role": "Manager",
                           "email": "taro.yamada@example.com",
                           "phone": "012-3456-7890"
                       },
                       {
                           "name": "Hanako Tanaka",
                           "department": "Sales",
                           "role": "Staff",
                           "email": "hanako.tanaka@example.com",
                           "phone": "023-4567-8901"
                       }
                   ]
                   
                """, response, JSONCompareMode.STRICT);


    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在するIDを指定して該当する従業員を取得した際にステータスコードが200を返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/employees/1")).
                andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                   "id": 1,
                   "name": "Taro Yamada",
                   "birthdate": "1990-01-01",
                   "department": "Sales",
                   "role": "Manager",
                   "email": "taro.yamada@example.com",
                   "phone": "012-3456-7890"
                }
                """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在しないIDを指定した場合にステータスコードが404を返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在する部署名を指定して該当する従業員情報を取得した際にステータスコード200を返すこと() throws Exception {
        String departmentName = "Sales";
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .param("department", departmentName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在しない部署名を指定した場合にステータスコードが404を返すこと() throws Exception {
        String departmentName = "HR";
        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .param("department", departmentName))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterInsert.yml", ignoreCols = "id")
    @Transactional
    void 従業員を新規登録した際にステータスコード201を返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                   "id": 3,
                                   "name": "Ichiro Sato",
                                   "birthdate": "1990-03-03",
                                   "department": "Information Systems",
                                   "role": "System Engineer",
                                   "email": "ichiro.sato@example.com",
                                   "phone": "012-3456-7890"
                                }
                                                                 
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                   "message": "A new employee is created!"
                }
                """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterUpdate.yml")
    @Transactional
    void 指定したIDの従業員情報を更新した際にステータスコード200を返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/employees/2")
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                      "name": "Hanako Tanaka",
                                      "birthdate": "1995-02-02",
                                      "department": "Information Systems",
                                      "role": "System Engineer",
                                      "email": "hanako.tanaka@example.com",
                                      "phone": "023-4567-8901"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                   "message": "Employee with ID has been updated"
                }
                """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在しないIDのメンバーを更新した際にエラーメッセージを返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/employees/99")
                        .contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                   "name": "John Doe",
                                   "birthdate": "1995-02-02",
                                   "department": "Information Systems",
                                   "role": "System Engineer",
                                   "email": "john.doe@example.com",
                                   "phone": "023-4567-8901"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                   "path": "/employees/99",
                   "status": "404",
                   "message": "resource not found",
                   "timestamp": "2023-08-01T14:00:25.696199300+09:00[GMT+09:00]",
                   "error": "Not Found"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("timestamp", ((o1, o2) -> true))));
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @ExpectedDataSet(value = "datasets/EmployeesAfterDelete.yml")
    @Transactional
    void 存在するIDを指定して従業員情報を削除した際にステータスコード200を返すこと() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/employees/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                "message": "Employee with ID has been deleted"
                }
                """, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/Employees.yml")
    @Transactional
    void 存在しないIDを削除した際のレスポンスが404を返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/members/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}




