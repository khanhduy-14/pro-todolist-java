package com.khanhduy14.todolist.task.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskLabel {
    private int taskId;
    private int labelId;
    private Instant createdAt;
    private Instant updatedAt;
}
