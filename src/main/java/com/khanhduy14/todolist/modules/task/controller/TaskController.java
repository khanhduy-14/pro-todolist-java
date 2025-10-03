package com.khanhduy14.todolist.modules.task.controller;

import com.khanhduy14.todolist.common.viewModel.PaginationResponse;
import com.khanhduy14.todolist.modules.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.modules.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.modules.task.entity.Task;
import com.khanhduy14.todolist.modules.task.params.TaskQueryParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TaskController {
    ResponseEntity<PaginationResponse<Task>> getTasks(TaskQueryParams params);

    ResponseEntity<Task> addTask(@RequestBody TaskCreateReqDTO request);

    ResponseEntity<Task> getTask(@PathVariable Integer id);

    ResponseEntity<Task> updateTask(@PathVariable Integer id,
                                    @RequestBody TaskUpdateReqDTO updates);

    ResponseEntity<Void> deleteTask(@PathVariable Integer id);
}
