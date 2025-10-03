package com.khanhduy14.todolist.common.viewModel;

import java.util.List;

public record PaginationResponse<T>(
        List<T> data,
        Metadata metadata
) {
    public record Metadata(
            long total,
            int offset,
            int limit
    ) {}
}