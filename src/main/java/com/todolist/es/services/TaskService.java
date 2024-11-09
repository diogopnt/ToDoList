package com.todolist.es.services;

import com.todolist.es.models.Task;
import com.todolist.es.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createNewTask(Task task, String userId) {
        task.setUserId(userId);
        System.out.printf("Tarefa" + task);
        return taskRepository.save(task);
    }

    public List<Task> getAllTask(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task updateTask(Task task, String userId) {
        task.setUserId(userId);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String userId) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent() && task.get().getUserId().equals(userId)) {
            taskRepository.deleteById(id);
        }
    }

    public List<Task> findAllCompletedTask() {
        return taskRepository.findByCompletedTrue();
    }

    public List<Task> findAllInCompleteTask() {
        return taskRepository.findByCompletedFalse();
    }

}
