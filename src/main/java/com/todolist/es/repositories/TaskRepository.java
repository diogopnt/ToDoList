package com.todolist.es.repositories;

import com.todolist.es.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByCompletedTrue();
    public List<Task> findByCompletedFalse();
}
