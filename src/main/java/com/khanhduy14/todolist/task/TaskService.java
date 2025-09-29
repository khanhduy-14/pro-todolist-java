package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public Task updateTaskByFields(Task task, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Task.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, task, value);
            }
        });

        return task;
    }

    public void deleteTask(Integer id) {
        taskRepo.deleteById(id);
    }
}
