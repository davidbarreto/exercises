package io.teamway.workplanning.controller;

import io.teamway.workplanning.model.Worker;
import io.teamway.workplanning.repository.WorkerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private WorkerRepository workerRepository;

    @Test
    @DisplayName("When all workers are requested then they are all returned")
    void allWorkersRequested() throws Exception {

        mockMvc
            .perform(get("/api/workers"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("When a specific element is requested then it is returned")
    void specificWorkerRequestedAndReturned() throws Exception {

        var expectedId = 1L;
        mockMvc
            .perform(get("/api/workers/" + expectedId))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").value(expectedId));
    }

    @Test
    @DisplayName("When a specific element is requested then it's not found")
    void specificWorkerRequestedAndNotFound() throws Exception {

        mockMvc
            .perform(get("/api/workers/4"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When an element is created then it is returned")
    void workerCreatedAndReturned() throws Exception {

        var expectedId = 4L;
        var expectedName = "Test";

        mockMvc
            .perform(post("/api/workers")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content("{ \"name\": \"" + expectedName + "\" }"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedId))
            .andExpect(jsonPath("$.name").value(expectedName));

        Worker worker = workerRepository.findById(expectedId)
                .orElseThrow(() -> new IllegalStateException("New Worker has not been saved in the repository"));

        assertThat(worker.getId(), equalTo(expectedId));
        assertThat(worker.getName(), equalTo(expectedName));
    }

    @Test
    @DisplayName("When a specific element is updated then it is returned")
    void specificWorkerUpdatedAndReturned() throws Exception {

        var expectedId = 1L;
        var expectedName = "Daniel";

        mockMvc
            .perform(put("/api/workers/" + expectedId)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content("{ \"id\": " + expectedId + ", \"name\": \"" + expectedName + "\" }"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").value(expectedId))
            .andExpect(jsonPath("$.name").value(expectedName));

        Worker worker = workerRepository.findById(expectedId)
                                .orElseThrow(() -> new IllegalStateException("The Worker has not been saved in the repository"));

        assertThat(worker.getId(), equalTo(expectedId));
        assertThat(worker.getName(), equalTo(expectedName));
    }

    @Test
    @DisplayName("When a specific element is updated then it is not found")
    void specificWorkerUpdatedAndNotFound() throws Exception {

        var expectedId = 4L;
        var expectedName = "Daniel";

        mockMvc
            .perform(put("/api/workers/" + expectedId)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content("{ \"id\": " + expectedId + ", \"name\": \"" + expectedName + "\" }"))
            .andExpect(status().isNotFound());

        assertThat(workerRepository.findById(expectedId), equalTo(Optional.empty()));
    }

    @Test
    @DisplayName("When a specific element is deleted successfully")
    void specificWorkerDeletedSuccessfully() throws Exception {

        var expectedId = 1L;
        mockMvc
            .perform(delete("/api/workers/" + expectedId))
            .andExpect(status().is2xxSuccessful());

        assertThat(workerRepository.findById(expectedId), equalTo(Optional.empty()));
    }

    @Test
    @DisplayName("When a specific element is deleted then it is not found")
    void specificWorkerDeletedAndNotFound() throws Exception {

        var expectedId = 4L;

        mockMvc
            .perform(delete("/api/workers/" + expectedId))
            .andExpect(status().isNotFound());

        assertThat(workerRepository.findById(expectedId), equalTo(Optional.empty()));
    }
}
