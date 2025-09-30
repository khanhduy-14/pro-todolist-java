package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Task> getTask(Integer id) {
        return taskRepo.findById(id);
    }

    public Task updateTask(Task task, TaskUpdateReqDTO updates) {
        updates.title().ifPresent(task::setTitle);
        updates.description().ifPresent(task::setDescription);
        updates.status().ifPresent(task::setStatus);
        return taskRepo.update(task);
    }

    public void deleteTask(Integer id) {
        taskRepo.deleteById(id);
    }
}
