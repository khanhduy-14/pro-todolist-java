package com.khanhduy14.todolist.modules.label.controller.impl;

import com.khanhduy14.todolist.modules.label.controller.LabelController;
import com.khanhduy14.todolist.modules.label.entity.Label;
import com.khanhduy14.todolist.modules.label.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("labels")
public class LabelControllerImpl implements LabelController {

    @Autowired
    private LabelService labelService;
    @Override
    @GetMapping
    public ResponseEntity<List<Label>> getLabels() {
        return ResponseEntity.ok(labelService.findAll());
    }
}
