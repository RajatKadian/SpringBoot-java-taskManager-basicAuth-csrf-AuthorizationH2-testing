package com.example.taskManager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface taskRepository
        extends JpaRepository<taskModel, Long> {

}