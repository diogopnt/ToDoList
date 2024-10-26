package com.todolist.es.services;

import com.todolist.es.models.Task;
import com.todolist.es.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createNewTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findAllCompletedTask() {
        return taskRepository.findByCompletedTrue();
    }

    public List<Task> findAllInCompleteTask() {
        return taskRepository.findByCompletedFalse();
    }

    public List<Task> findAllByCategory(String category){
        return taskRepository.findByCategory(category);
    }

    public List<Task>findByDeadline(LocalDateTime deadline){
        return taskRepository.findByDeadline(deadline);
    }

    public Task findTaskById(Long id) {
        return taskRepository.getById(id);
    }

}
