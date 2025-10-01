package com.khanhduy14.todolist.task.dto;


import java.util.List;

public record TaskCreateReqDTO(String title, String description, List<String> labels) { }
