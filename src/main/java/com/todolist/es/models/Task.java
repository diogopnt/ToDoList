package com.todolist.es.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;
    private String category;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime deadline;

    @Enumerated
    private Prioritization prioritization;

    @Column(name = "user_id")
    private String userId;

    public Task() {
    }

    public Task(Long id, String title, String description, boolean completed, String category, LocalDateTime creationDate, LocalDateTime updateDate, LocalDateTime deadline, Prioritization prioritization, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.category = category;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.deadline = deadline;
        this.prioritization = prioritization;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreation(LocalDateTime creation) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Prioritization getPrioritization() {
        return prioritization;
    }

    public void setPrioritization(Prioritization prioritization) {
        this.prioritization = prioritization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
