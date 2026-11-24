
package com.example.taskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private taskRepository repository;

    @BeforeEach
    void setup() {
        // Remove previous test data
        repository.deleteAll();
    }

    @Test
    void testGetTaskById() throws Exception {

        // STEP 1: Create a task
        taskModel task = new taskModel();
        task.setTitle("Prat");
        task.setCompleted(false);

        // STEP 2: Save task to database
        taskModel savedTask = repository.save(task);

        // STEP 3: Send GET /tasks/{id}
        mockMvc.perform(get("/tasks/{id}", savedTask.getId()))

                // STEP 4: Expect HTTP 200
                .andExpect(status().isOk())

                // STEP 5: Check returned JSON
                .andExpect(jsonPath("$.title").value("Prat"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testCreateTask() throws Exception {

        // STEP 1: JSON request body
        String json = """
            {
                "title": "Prat",
                "completed": false
            }
            """;

        // STEP 2: POST request
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // Change to isCreated() if controller returns 201
                .andExpect(status().isOk())

                // STEP 3: Verify response JSON
                .andExpect(jsonPath("$.title").value("Prat"))
                .andExpect(jsonPath("$.completed").value(false));

        // STEP 4: Check saved database count
        assertEquals(1, repository.count());
    }
}
