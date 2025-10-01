package com.khanhduy14.todolist.task.repository;

import com.khanhduy14.todolist.task.entity.TaskLabel;

import java.util.List;

public interface TaskLabelRepository {
    List<TaskLabel> save(int taskId, List<Integer> labelIds);
    List<Integer> findLabelIdsByTaskId(int taskId);
    void delete(int taskId, List<Integer> labelIds);
    void deleteByTaskId(int taskId);
}
