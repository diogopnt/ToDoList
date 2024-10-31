package com.todolist.es.services;


import com.todolist.es.models.Prioritization;
import com.todolist.es.models.Task;
import com.todolist.es.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        task = new Task(null, "Sample Task", "Sample Description", false, "Work",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(5), Prioritization.MEDIUM, 1L);
    }

    @Test
    public void testCreateNewTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createNewTask(task);
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Sample Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetAllTask() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskService.getAllTask();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Sample Task");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(task);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("Sample Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(anyLong());

        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAllCompletedTask() {
        task.setCompleted(true);
        when(taskRepository.findByCompletedTrue()).thenReturn(Arrays.asList(task));

        List<Task> completedTasks = taskService.findAllCompletedTask();
        assertThat(completedTasks).hasSize(1);
        assertThat(completedTasks.get(0).getTitle()).isEqualTo("Sample Task");
        verify(taskRepository, times(1)).findByCompletedTrue();
    }

    @Test
    public void testFindAllInCompleteTask() {
        task.setCompleted(false);
        when(taskRepository.findByCompletedFalse()).thenReturn(Arrays.asList(task));

        List<Task> incompleteTasks = taskService.findAllInCompleteTask();
        assertThat(incompleteTasks).hasSize(1);
        assertThat(incompleteTasks.get(0).getTitle()).isEqualTo("Sample Task");
        verify(taskRepository, times(1)).findByCompletedFalse();
    }
}
