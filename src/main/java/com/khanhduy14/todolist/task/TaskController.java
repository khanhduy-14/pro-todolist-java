package com.khanhduy14.todolist.task;


import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.utils.RecordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
       return ResponseEntity.ok(taskService.getTasks());
    }
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody TaskCreateReqDTO request) {
        return ResponseEntity.ok(taskService.addTask(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Integer id) {
        return ResponseEntity.ok(findTaskOrFail(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody TaskUpdateReqDTO updates) {
        Task task = findTaskOrFail(id);
        Task updatedTask = taskService.updateTaskByFields(task, RecordUtils.toMap(updates));
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        findTaskOrFail(id);
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public Task findTaskOrFail(Integer id) {
        return taskService.getTask(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
