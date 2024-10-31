package com.todolist.es.controllers;

import com.todolist.es.models.Prioritization;
import com.todolist.es.models.Task;
import com.todolist.es.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private MockMvc mockMvc;

    private Task task;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        task = new Task(1L, "Sample Task", "Sample Description", false, "Work",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(5), Prioritization.MEDIUM, 1L);
    }

    @Test
    public void testCreateTask() throws Exception {
        when(taskService.createNewTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/v1/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Task\", \"description\":\"Sample Description\", \"completed\":false, \"category\":\"Work\", \"creationDate\":\"2023-10-30T10:00:00\", \"updateDate\":\"2023-10-30T10:00:00\", \"deadline\":\"2023-11-04T10:00:00\", \"prioritization\":\"MEDIUM\", \"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> tasks = Collections.singletonList(task);
        when(taskService.getAllTask()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        when(taskService.updateTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Task\", \"description\":\"Updated Description\", \"completed\":true, \"category\":\"Home\", \"creationDate\":\"2023-10-30T10:00:00\", \"updateDate\":\"2023-10-30T10:00:00\", \"deadline\":\"2023-11-04T10:00:00\", \"prioritization\":\"HIGH\", \"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testGetAllCompletedTasks() throws Exception {
        List<Task> tasks = Collections.singletonList(task);
        when(taskService.findAllCompletedTask()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    public void testGetAllIncompleteTasks() throws Exception {
        List<Task> tasks = Collections.singletonList(task);
        when(taskService.findAllInCompleteTask()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks/incomplete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }
}
