package com.khanhduy14.todolist.modules.task.repository;

import com.khanhduy14.todolist.modules.task.entity.TaskLabel;

import java.util.List;

public interface TaskLabelRepository {
    List<TaskLabel> save(int taskId, List<Integer> labelId);
    List<Integer> findLabelIdsByTaskId(int taskId);
    void delete(int taskId, List<Integer> labelIds);
    void deleteByTaskId(int taskId);
}
