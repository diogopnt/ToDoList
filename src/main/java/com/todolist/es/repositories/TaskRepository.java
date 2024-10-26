package com.todolist.es.repositories;

import com.todolist.es.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByCompletedTrue();
    public List<Task> findByCompletedFalse();
    public List<Task> findByCategory(String category);
    public List<Task> findByDeadline(LocalDateTime deadline);
    public Task getById(Long id);
}
