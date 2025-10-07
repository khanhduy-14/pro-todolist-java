package com.khanhduy14.todolist.config.module;

import lombok.Data;

import java.util.List;

@Data
public class FlywayConfig {
    private boolean enabled;
    private List<String> locations;
    private boolean baselineOnMigrate;
}
