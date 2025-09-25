package com.khanhduy14.todolist.controller;


import com.khanhduy14.todolist.dto.CreateTaskRequest;
import com.khanhduy14.todolist.model.Task;
import com.khanhduy14.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;
    @GetMapping
    public List<Task> getTasks() {
       return taskService.getTasks();
    }
    @PostMapping
    public Task addTask(@RequestBody CreateTaskRequest task) {
        return taskService.addTask(task);
    }
}
