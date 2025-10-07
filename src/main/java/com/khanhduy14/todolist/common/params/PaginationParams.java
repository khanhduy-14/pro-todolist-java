package com.khanhduy14.todolist.common.params;

import com.khanhduy14.todolist.common.constant.SortOrder;
import lombok.Data;

@Data
public class PaginationParams {
    private int offset = 0;
    private int limit = 5;
    private String sortBy = "createdAt";
    private SortOrder sortOrder = SortOrder.DESC;
}