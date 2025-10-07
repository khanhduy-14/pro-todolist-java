package com.khanhduy14.todolist.modules.task.constant.converter;

import com.khanhduy14.todolist.modules.task.constant.TaskStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTaskStatusConverter implements Converter<String, TaskStatus> {

    @Override
    public TaskStatus convert(String source) {
        if (source == null || source.isEmpty()) return null;

        try {
            short code = Short.parseShort(source);
            for (TaskStatus status : TaskStatus.values()) {
                if (status.getCode() == code) return status;
            }
        } catch (NumberFormatException e) {
        }

        throw new IllegalArgumentException("Unknown TaskStatus code: " + source);
    }
}
