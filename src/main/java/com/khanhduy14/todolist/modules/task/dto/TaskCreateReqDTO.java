package com.khanhduy14.todolist.modules.task.dto;


import java.util.List;

public record TaskCreateReqDTO(String title, String description, List<String> labels) { }
