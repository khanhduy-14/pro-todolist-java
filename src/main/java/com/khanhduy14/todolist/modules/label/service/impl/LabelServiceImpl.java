package com.khanhduy14.todolist.modules.label.service.impl;

import com.khanhduy14.todolist.modules.label.entity.Label;
import com.khanhduy14.todolist.modules.label.repository.LabelRepository;
import com.khanhduy14.todolist.modules.label.service.LabelService;
    import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepo;

    public LabelServiceImpl(LabelRepository labelRepo) {
        this.labelRepo = labelRepo;
    }

    @Override
    public List<Label> findAll() {
        return labelRepo.findAll();
    }

    @Override
    public List<Label> getOrCreateLabels(List<String> labelNames) {
        List<Label> existingLabels = labelRepo.findByNames(labelNames);
        Set<String> existingNames = new HashSet<>();
        for (Label label : existingLabels) {
            existingNames.add(label.getName());
        }
        List<String> toCreate = labelNames.stream()
                .filter(name -> !existingNames.contains(name))
                .distinct()
                .toList();
        List<Label> newLabels = labelRepo.bulkSave(toCreate);

        List<Label> allLabels = new ArrayList<>();
        allLabels.addAll(existingLabels);
        allLabels.addAll(newLabels);

        return allLabels;
    }
}
