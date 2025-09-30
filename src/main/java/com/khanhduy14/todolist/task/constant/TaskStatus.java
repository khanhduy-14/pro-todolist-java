package com.khanhduy14.todolist.task.constant;

import lombok.Getter;

@Getter
public enum TaskStatus {
    TO_DO((short)0),
    IN_PROGRESS((short)1),
    DONE((short)2),
    DELETED((short)3);

    private final short code;

    TaskStatus(short code) {
        this.code = code;
    }

    public static TaskStatus fromCode(short code) {
        for (TaskStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown TaskStatus code: " + code);
    }
}
