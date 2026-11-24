package com.example.taskManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private taskRepository repository;

    @InjectMocks
    private taskService service;

    @Test
    void testCreateTask() {

        // Step 1: Create a fake task
        taskModel task = new taskModel();
        task.setTitle("Learn JUnit");
        task.setCompleted(false);

        // Step 2: Tell Mockito what to return
        when(repository.save(task)).thenReturn(task);

        // Step 3: Call the real service method
        taskModel result = service.createTask(task);

        // Step 4: Check the result
        assertEquals("Learn JUnit", result.getTitle());

        // Step 5: Verify repository.save was called
        verify(repository).save(task);
    }
}