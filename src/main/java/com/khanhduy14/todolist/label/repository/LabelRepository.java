package com.khanhduy14.todolist.label.repository;

import com.khanhduy14.todolist.label.entity.Label;

import java.util.List;

public interface LabelRepository {
    List<Label> findByNames(List<String> names);
    Label save(String name);
    List<Label> bulkSave(List<String> names);
}
