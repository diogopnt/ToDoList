package com.todolist.es.controllers;

import org.springframework.web.bind.annotation.*;
import com.todolist.es.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.todolist.es.services.TaskService;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getAllCompletedTasks() {
        return ResponseEntity.ok(taskService.findAllCompletedTask());
    }
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getAllIncompleteTasks() {
        return ResponseEntity.ok(taskService.findAllInCompleteTask());
    }

    @GetMapping("/filterCategory/{category}")
    public ResponseEntity<List<Task>> filterByCategory(@PathVariable String category) {
        return ResponseEntity.ok(taskService.findAllByCategory(category));
    }

    @GetMapping("/filterDeadline/{deadline}")
    public ResponseEntity<List<Task>> filterByDeadline(@PathVariable String deadline) {
        LocalDateTime parsedDeadline = LocalDateTime.parse(deadline);
        return ResponseEntity.ok(taskService.findByDeadline(parsedDeadline));
    }
}
