package com.khanhduy14.todolist.task.service.impl;

import com.khanhduy14.todolist.label.entity.Label;
import com.khanhduy14.todolist.label.service.LabelService;
import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.task.entity.Task;
import com.khanhduy14.todolist.task.repository.TaskLabelRepository;
import com.khanhduy14.todolist.task.repository.TaskRepository;
import com.khanhduy14.todolist.task.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final LabelService labelService;
    private final TaskLabelRepository taskLabelRepo;
    public TaskServiceImpl(TaskRepository taskRepo, TaskLabelRepository taskLabelRepo, LabelService labelService) {
        this.taskRepo = taskRepo;
        this.labelService = labelService;
        this.taskLabelRepo = taskLabelRepo;
    }

    @Override
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    @Override
    @Transactional
    public Task addTask(TaskCreateReqDTO request) {
        List<Label> labels = labelService.getOrCreateLabels(request.labels());
        Task createdTask = taskRepo.save(Task.builder()
                .title(request.title())
                .description(request.description())
                .status(TaskStatus.TO_DO)
                .build());
        taskLabelRepo.save(createdTask.getId(), labels.stream().map(Label::getId).toList());
        List<String> listTaskLabelName = labels.stream().map(Label::getName).toList();
        createdTask.setLabels(listTaskLabelName);
        return createdTask;
    }

    @Override
    public Optional<Task> getTask(Integer id) {
        return taskRepo.findById(id);
    }

    @Override
    @Transactional
    public Task updateTask(Task task, TaskUpdateReqDTO updates) {
        // Update task thông tin cơ bản
        updates.title().ifPresent(task::setTitle);
        updates.description().ifPresent(task::setDescription);
        updates.status().ifPresent(task::setStatus);

        Task updatedTask = taskRepo.update(task);

        updates.labels().ifPresent(labelNames -> {
            List<Label> newLabels = labelService.getOrCreateLabels(labelNames);

            List<Integer> currentLabelIds = taskLabelRepo.findLabelIdsByTaskId(task.getId());

            List<Integer> newLabelIds = newLabels.stream().map(Label::getId).toList();

            List<Integer> toInsert = newLabelIds.stream()
                    .filter(id -> !currentLabelIds.contains(id))
                    .toList();

            List<Integer> toDelete = currentLabelIds.stream()
                    .filter(id -> !newLabelIds.contains(id))
                    .toList();

            if (!toInsert.isEmpty()) taskLabelRepo.save(task.getId(), toInsert);
            if (!toDelete.isEmpty()) taskLabelRepo.delete(task.getId(), toDelete);

            updatedTask.setLabels(newLabels.stream().map(Label::getName).toList());
        });
        return updatedTask;
    }


    @Override
    public void deleteTask(Integer id) {
        taskLabelRepo.deleteByTaskId(id);
        taskRepo.deleteById(id);
    }
}

