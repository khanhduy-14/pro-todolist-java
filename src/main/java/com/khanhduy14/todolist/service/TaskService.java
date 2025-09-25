package com.khanhduy14.todolist.service;


import com.khanhduy14.todolist.constant.task.TaskStatus;
import com.khanhduy14.todolist.dto.CreateTaskRequest;
import com.khanhduy14.todolist.model.Task;
import com.khanhduy14.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepo;
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    public Task addTask(CreateTaskRequest task) {
        Task newTask = Task.builder().title(task.title()).description(task.description()).status(TaskStatus.TO_DO).build();

        return taskRepo.save(newTask);
    }
}
