package com.khanhduy14.todolist.config.module;

import lombok.Data;

@Data
public class DbConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}

