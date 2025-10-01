package com.khanhduy14.todolist;

import com.khanhduy14.todolist.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodolistApplication implements CommandLineRunner {

    private final AppConfig config;

    public TodolistApplication(AppConfig config) {
        this.config = config;
    }
    public static void main(String[] args) {
        System.out.println("🚀 Starting Todolist Application...");
        SpringApplication.run(TodolistApplication.class, args);
        System.out.println("✅ Application started successfully.");
    }

    @Override
    public void run(String... args) {
        System.out.println("🎯 Application "+  config.getApplication().getName()+  " is up and ready." );
    }
}
