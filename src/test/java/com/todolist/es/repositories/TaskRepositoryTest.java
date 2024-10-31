package com.todolist.es.repositories;

import com.todolist.es.models.Prioritization;
import com.todolist.es.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.todolist.es.models.Prioritization.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setup() {
        task = new Task(null, "Sample Task", "Sample Description", false, "Work",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(5), MEDIUM, 1L);
    }

    @Test
    public void testSaveTask() {
        Task savedTask = taskRepository.save(task);
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Sample Task");
        assertThat(savedTask.getDescription()).isEqualTo("Sample Description");
        assertThat(savedTask.isCompleted()).isEqualTo(false);
        assertThat(savedTask.getCategory()).isEqualTo("Work");
        //assertThat(savedTask.getCreationDate()).isEqualTo(LocalDateTime.now());
        //assertThat(savedTask.getUpdateDate()).isEqualTo(LocalDateTime.now());
        //assertThat(savedTask.getDeadline()).isEqualTo(LocalDateTime.now().plusDays(5));
        assertThat(savedTask.getPrioritization()).isEqualTo(MEDIUM);
        assertThat(savedTask.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testFindByCompletedTrue() {
        task.setCompleted(true);
        taskRepository.save(task);

        List<Task> completedTasks = taskRepository.findByCompletedTrue();
        assertThat(completedTasks).hasSize(1);
        assertThat(completedTasks.get(0).getTitle()).isEqualTo("Sample Task");
    }

    @Test
    public void testFindByCompletedFalse() {
        task.setCompleted(false);
        taskRepository.save(task);

        List<Task> incompleteTasks = taskRepository.findByCompletedFalse();
        assertThat(incompleteTasks).hasSize(1);
        assertThat(incompleteTasks.get(0).getTitle()).isEqualTo("Sample Task");
    }

    @Test
    public void testFindByCategory() {
        task.setCategory("Work");
        taskRepository.save(task);

        List<Task> tasksByCategory = taskRepository.findByCategory("Work");
        assertThat(tasksByCategory).hasSize(1);
        assertThat(tasksByCategory.get(0).getTitle()).isEqualTo("Sample Task");
    }

    @Test
    public void testGetById() {
        Task savedTask = taskRepository.save(task);
        Task foundTask = taskRepository.getById(savedTask.getId());

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getId()).isEqualTo(savedTask.getId());
    }
}
