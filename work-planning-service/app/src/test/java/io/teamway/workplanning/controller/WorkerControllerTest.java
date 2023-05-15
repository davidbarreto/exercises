package io.teamway.workplanning.controller;

import io.teamway.workplanning.repository.WorkerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
@AutoConfigureMockMvc
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkerRepository repository;

    @Test
    @DisplayName("When all workers are requested then they are all returned")
    void allWorkersRequested() throws Exception {

        mockMvc
            .perform(get("/api/workers"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$", hasSize(3)));
    }
}
