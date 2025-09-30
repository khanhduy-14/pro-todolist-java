package com.khanhduy14.todolist.task.service.impl;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.task.entity.Task;
import com.khanhduy14.todolist.task.repository.TaskRepository;
import com.khanhduy14.todolist.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;

    public TaskServiceImpl(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Task addTask(TaskCreateReqDTO request) {
        Task newTask = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TO_DO)
                .build();
        return taskRepo.save(newTask);
    }

    @Override
    public Optional<Task> getTask(Integer id) {
        return taskRepo.findById(id);
    }

    @Override
    public Task updateTask(Task task, TaskUpdateReqDTO updates) {
        updates.title().ifPresent(task::setTitle);
        updates.description().ifPresent(task::setDescription);
        updates.status().ifPresent(task::setStatus);
        return taskRepo.update(task);
    }

    @Override
    public void deleteTask(Integer id) {
        taskRepo.deleteById(id);
    }
}

