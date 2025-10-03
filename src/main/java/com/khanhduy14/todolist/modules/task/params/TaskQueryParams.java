package com.khanhduy14.todolist.modules.task.params;


import com.khanhduy14.todolist.common.params.PaginationParams;
import com.khanhduy14.todolist.modules.task.constant.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQueryParams extends PaginationParams {
    private String title;
    private TaskStatus status;
    private List<String> labelIds;
}