package com.khanhduy14.todolist.task;


import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
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
    public Task addTask(@RequestBody TaskCreateReqDTO request) {
        return taskService.addTask(request);
    }
}
