package com.todolist.es.controllers;

import org.springframework.web.bind.annotation.*;
import com.todolist.es.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.todolist.es.services.TaskService;
import org.springframework.stereotype.Controller;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createNewTask(task));
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTask());
    }
}
