package com.khanhduy14.todolist.modules.task.dto;

import com.khanhduy14.todolist.modules.task.constant.TaskStatus;

import java.util.List;
import java.util.Optional;

public record TaskUpdateReqDTO (
        Optional<String> title,
        Optional<String> description,
        Optional<TaskStatus> status,
        Optional<List<String>> labels
) {
}