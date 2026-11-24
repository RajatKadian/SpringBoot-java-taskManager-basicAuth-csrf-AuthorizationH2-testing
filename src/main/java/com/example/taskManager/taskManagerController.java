package com.example.taskManager;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class taskManagerController {

    private final taskService service;

    // Constructor dependency injection
    public taskManagerController(taskService service) {
        this.service = service;
    }

    // GET /tasks
    @GetMapping
    public List<taskModel> getAllTasks() {
        return service.getAllTasks();
    }

    // Add inside taskManagerController
    @GetMapping("/{id}")
    public taskModel getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PostMapping
    public taskModel createTask(
            @Valid @RequestBody CreateTaskRequest request) {

        taskModel task = new taskModel();

        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());

        return service.createTask(task);
    }

    // POST /tasks
//    @PostMapping
//    public taskModel createTask(@RequestBody taskModel task) {
//        return service.createTask(task);
//    }

    //put
    @PutMapping("/{id}")
    public ResponseEntity<taskModel> updateTask(
            @PathVariable Long id,
            @RequestBody taskModel updatedTask) {

        Optional<taskModel> result =
                service.updateTask(id, updatedTask);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        boolean deleted = service.deleteTask(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


}