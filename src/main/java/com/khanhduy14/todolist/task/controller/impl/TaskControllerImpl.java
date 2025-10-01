package com.khanhduy14.todolist.task.controller.impl;


import com.khanhduy14.todolist.common.params.PaginationParams;
import com.khanhduy14.todolist.task.controller.TaskController;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.task.entity.Task;
import com.khanhduy14.todolist.task.params.TaskQueryParams;
import com.khanhduy14.todolist.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("/tasks")
public class TaskControllerImpl implements TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    @Override
    public ResponseEntity<List<Task>> getTasks(TaskQueryParams params) {
       return ResponseEntity.ok(taskService.getTasks(params));
    }

    @PostMapping
    @Override
    public ResponseEntity<Task> addTask(@RequestBody TaskCreateReqDTO request) {
        return ResponseEntity.ok(taskService.addTask(request));
    }


    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Task> getTask(@PathVariable Integer id) {
        return ResponseEntity.ok(findTaskOrFail(id));
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody TaskUpdateReqDTO updates) {
        Task task = findTaskOrFail(id);
        Task updatedTask = taskService.updateTask(task, updates);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        findTaskOrFail(id);
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Task findTaskOrFail(Integer id) {
        return taskService.getTask(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}

