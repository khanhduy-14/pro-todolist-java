package com.khanhduy14.todolist.utils;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RecordUtils {
    public static Map<String, Object> toMap(Object record) {
        if (!record.getClass().isRecord()) {
            throw new IllegalArgumentException("Object is not a record: " + record.getClass());
        }
        Map<String, Object> map = new HashMap<>();
        for (RecordComponent rc : record.getClass().getRecordComponents()) {
            try {
                Object value = rc.getAccessor().invoke(record);
                if (value instanceof Optional<?> opt) {
                    opt.ifPresent(v -> map.put(rc.getName(), v));
                } else if (value != null) {
                    map.put(rc.getName(), value);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to access record component: " + rc.getName(), e);
            }
        }
        return map;
    }
}

