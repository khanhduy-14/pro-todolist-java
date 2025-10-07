package com.khanhduy14.todolist.modules.label.service;

import com.khanhduy14.todolist.modules.label.entity.Label;

import java.util.List;

public interface LabelService {
    List<Label> findAll();
    List<Label> getOrCreateLabels(List<String> labelNames);
}
