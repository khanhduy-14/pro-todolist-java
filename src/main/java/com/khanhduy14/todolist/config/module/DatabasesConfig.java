package com.khanhduy14.todolist.config.module;

import lombok.Data;

@Data
public class DatabasesConfig {
    private DbConfig primary;
    private DbConfig analytics;
}