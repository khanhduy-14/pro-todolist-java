package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
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

    public Task addTask(TaskCreateReqDTO request) {
        Task newTask = Task.builder().title(request.title()).description(request.description()).status(TaskStatus.TO_DO).build();

        return taskRepo.save(newTask);
    }
}
