package com.khanhduy14.todolist.task.repository.impl;

import com.khanhduy14.todolist.libs.jooq.generated.tables.records.TaskLabelRecord;
import com.khanhduy14.todolist.task.entity.TaskLabel;
import com.khanhduy14.todolist.task.repository.TaskLabelRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.khanhduy14.todolist.libs.jooq.generated.Tables.LABEL;
import static com.khanhduy14.todolist.libs.jooq.generated.Tables.TASK_LABEL;

@Repository
public class TaskLabelRepositoryImpl implements TaskLabelRepository {

    private final DSLContext dsl;
    TaskLabelRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }
    @Override
    public List<TaskLabel> save(int taskId, List<Integer> labelIds) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        var step = dsl.insertInto(TASK_LABEL, TASK_LABEL.TASK_ID, TASK_LABEL.LABEL_ID, TASK_LABEL.CREATED_AT, TASK_LABEL.UPDATED_AT);
        for (Integer labelId : labelIds) {
            step.values(taskId, labelId, now, now);
        }
        return step
                .returning(TASK_LABEL.TASK_ID, TASK_LABEL.LABEL_ID, TASK_LABEL.CREATED_AT, TASK_LABEL.UPDATED_AT)
                .fetch(this::map);
    }

    @Override
    public List<Integer> findLabelIdsByTaskId(int taskId) {
        return dsl.select(TASK_LABEL.LABEL_ID)
                .from(TASK_LABEL)
                .where(TASK_LABEL.TASK_ID.eq(taskId))
                .fetch(TASK_LABEL.LABEL_ID);
    }

    @Override
    public void delete(int taskId, List<Integer> labelIds) {
        dsl.deleteFrom(TASK_LABEL)
                .where(TASK_LABEL.TASK_ID.eq(taskId)
                        .and(TASK_LABEL.LABEL_ID.in(labelIds)))
                .execute();
    }
    @Override
    public void deleteByTaskId(int taskId) {
        dsl.deleteFrom(TASK_LABEL)
                .where(TASK_LABEL.TASK_ID.eq(taskId))
                .execute();
    }

    private TaskLabel map(TaskLabelRecord r) {
        if (r == null) return null;
        return TaskLabel.builder()
                .taskId(r.get(TASK_LABEL.TASK_ID))
                .labelId(r.get(TASK_LABEL.LABEL_ID))
                .createdAt(r.get(LABEL.CREATED_AT).toInstant(ZoneOffset.UTC))
                .updatedAt(r.get(LABEL.UPDATED_AT).toInstant(ZoneOffset.UTC)).build();
    }
}
