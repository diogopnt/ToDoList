package com.todolist.es.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.todolist.es.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.todolist.es.services.TaskService;
import org.springframework.stereotype.Controller;

import java.sql.SQLOutput;
import java.util.List;
import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task API", description = "Endpoints for managing tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task for the authenticated user. Expects a Task object in the request body.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = Task.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/")
    public ResponseEntity<Task> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task object to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Task.class))
            )
            @RequestBody Task task) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return ResponseEntity.ok(taskService.createNewTask(task, userId));
    }


    @Operation(summary = "Get all tasks",
            description = "Retrieves all tasks for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tasks",
                            content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return ResponseEntity.ok(taskService.getAllTask(userId));
    }

    @Operation(summary = "Update a task",
            description = "Updates an existing task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully",
                            content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Task.class)))
            Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        task.setId(id);
        return ResponseEntity.ok(taskService.updateTask(task, userId));
    }

    @Operation(summary = "Delete a task",
            description = "Deletes a task by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(
            @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        taskService.deleteTask(id, userId);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Get all completed tasks",
            description = "Retrieves all tasks marked as completed",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of completed tasks",
                            content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getAllCompletedTasks() {
        return ResponseEntity.ok(taskService.findAllCompletedTask());
    }

    @Operation(summary = "Get all incomplete tasks",
            description = "Retrieves all tasks marked as incomplete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of incomplete tasks",
                            content = @Content(schema = @Schema(implementation = Task.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getAllIncompleteTasks() {
        return ResponseEntity.ok(taskService.findAllInCompleteTask());
    }
}