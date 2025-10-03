package com.khanhduy14.todolist.modules.task.service;

import com.khanhduy14.todolist.modules.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.modules.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.modules.task.entity.Task;
import com.khanhduy14.todolist.modules.task.params.TaskQueryParams;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getTasks(TaskQueryParams params);
    Task addTask(TaskCreateReqDTO request);
    Optional<Task> getTask(Integer id);
    Task updateTask(Task task, TaskUpdateReqDTO updates);
    void deleteTask(Integer id);
}

