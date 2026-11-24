package com.example.taskManager;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class taskService {

    private final taskRepository repository;

    // Constructor dependency injection
    public taskService(taskRepository repository) {     //dependency injection
        this.repository = repository;
    }

    // Get all tasks
    public List<taskModel> getAllTasks() {
        return repository.findAll();
    }

    // Add inside taskService class
//    public taskModel getTaskById(Long id) {
//        return repository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Task not found")
//                );
//    }

    public taskModel getTaskById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found: " + id)
                );
    }

    // Create a new task
    public taskModel createTask(taskModel task) {
        return repository.save(task);
    }

    //update task
    public Optional<taskModel> updateTask(
            Long id, taskModel updatedTask) {

        // Step 1: Find existing task
        Optional<taskModel> existingTask = repository.findById(id);

        // Step 2: Check whether it exists
        if (existingTask.isPresent()) {

            // Step 3: Get the existing object
            taskModel task = existingTask.get();

            // Step 4: Update fields
            task.setTitle(updatedTask.getTitle());
            task.setCompleted(updatedTask.isCompleted());

            // Step 5: Save updated task
            return Optional.of(repository.save(task));
        }

        // Task not found
        return Optional.empty();
    }

    //delete
    public boolean deleteTask(Long id) {

        // Check whether task exists
        if (repository.existsById(id)) {

            // Delete task
            repository.deleteById(id);

            return true;
        }

        return false;
    }
}