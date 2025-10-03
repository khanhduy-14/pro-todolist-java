package com.khanhduy14.todolist.modules.label.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Label {
    private int id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
