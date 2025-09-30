package com.khanhduy14.todolist.task.repository;

import com.khanhduy14.todolist.task.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Task save(Task task);
    Task update(Task task);
    Optional<Task> findById(Integer id);
    void deleteById(Integer id);
}
