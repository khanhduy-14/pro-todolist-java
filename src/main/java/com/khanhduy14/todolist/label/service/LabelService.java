package com.khanhduy14.todolist.label.service;

import com.khanhduy14.todolist.label.entity.Label;

import java.util.List;

public interface LabelService {
    List<Label> getOrCreateLabels(List<String> labelNames);
}
