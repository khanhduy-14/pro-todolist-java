package com.khanhduy14.todolist.task.dto;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import java.util.Optional;

public record TaskUpdateReqDTO (
        Optional<String> title,
        Optional<String> description,
        Optional<TaskStatus> status) {
}