package com.khanhduy14.todolist.modules.label.controller;


import com.khanhduy14.todolist.modules.label.entity.Label;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LabelController {
    ResponseEntity<List<Label>> getLabels();
}
