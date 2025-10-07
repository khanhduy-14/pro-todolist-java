package com.khanhduy14.todolist.common.constant;


import lombok.Getter;

@Getter
public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortOrder(String value) {
        this.value = value;
    }

    public static SortOrder fromString(String value) {
        if (value == null) return DESC; // default
        return "asc".equalsIgnoreCase(value) ? ASC : DESC;
    }

    @Override
    public String toString() {
        return value;
    }
}
