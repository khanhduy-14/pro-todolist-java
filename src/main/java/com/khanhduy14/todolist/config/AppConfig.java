package com.khanhduy14.todolist.config;

import com.khanhduy14.todolist.config.module.ApplicationConfig;
import com.khanhduy14.todolist.config.module.DatabasesConfig;
import lombok.Data;
@Data
public class AppConfig {
    private ApplicationConfig application;
    private DatabasesConfig database;
}
