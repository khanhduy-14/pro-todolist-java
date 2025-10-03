package com.khanhduy14.todolist.modules.label.repository;

import com.khanhduy14.todolist.modules.label.entity.Label;

import java.util.List;

public interface LabelRepository {
    List<Label> findAll();
    List<Label> findByNames(List<String> names);
    Label save(String name);
    List<Label> bulkSave(List<String> names);
}
