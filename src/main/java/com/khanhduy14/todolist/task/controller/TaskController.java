package com.khanhduy14.todolist.task.controller;

import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.task.entity.Task;
import com.khanhduy14.todolist.task.params.TaskQueryParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tasks")
public interface TaskController {
    @GetMapping
    ResponseEntity<List<Task>> getTasks(TaskQueryParams params);

    @PostMapping
    ResponseEntity<Task> addTask(@RequestBody TaskCreateReqDTO request);

    @GetMapping("/{id}")
    ResponseEntity<Task> getTask(@PathVariable Integer id);

    @PatchMapping("/{id}")
    ResponseEntity<Task> updateTask(@PathVariable Integer id,
                                    @RequestBody TaskUpdateReqDTO updates);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable Integer id);
}
