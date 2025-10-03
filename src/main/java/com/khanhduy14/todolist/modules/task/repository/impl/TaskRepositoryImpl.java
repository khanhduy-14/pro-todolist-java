package com.khanhduy14.todolist.modules.task.repository.impl;

import com.khanhduy14.todolist.common.constant.SortOrder;
import com.khanhduy14.todolist.libs.jooq.generated.tables.records.TaskRecord;
import com.khanhduy14.todolist.modules.task.constant.TaskStatus;
import com.khanhduy14.todolist.modules.task.entity.Task;
import com.khanhduy14.todolist.modules.task.repository.TaskRepository;
import com.khanhduy14.todolist.utils.DateTimeUtils;
import org.jooq.DSLContext;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import static com.khanhduy14.todolist.libs.jooq.generated.tables.Task.TASK;
import static com.khanhduy14.todolist.libs.jooq.generated.tables.TaskLabel.TASK_LABEL;

import static com.khanhduy14.todolist.libs.jooq.generated.tables.Label.LABEL;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Repository
public class TaskRepositoryImpl  implements TaskRepository {

    private final DSLContext dsl;


    public TaskRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Task> findAll(int offset,
                              int limit,
                              String sortBy,
                              SortOrder sortOrder,
                              String title,
                              TaskStatus status,
                              List<String> labelIds) {

        var query = dsl.select(
                        TASK.ID,
                        TASK.TITLE,
                        TASK.DESCRIPTION,
                        TASK.STATUS,
                        TASK.CREATED_AT,
                        TASK.UPDATED_AT,
                        DSL.field("COALESCE(string_agg(label.name, ','), '')", String.class).as("labelIds")
                )
                .from(TASK)
                .leftJoin(TASK_LABEL).on(TASK.ID.eq(TASK_LABEL.TASK_ID))
                .leftJoin(LABEL).on(TASK_LABEL.LABEL_ID.eq(LABEL.ID));

        applyFilters(query, title, status, labelIds);

        query.groupBy(TASK.ID);

        var sortField = TASK.field(sortBy);
        if (sortOrder == SortOrder.DESC) {
            query.orderBy(sortField.desc());
        } else {
            query.orderBy(sortField.asc());
        }

        query.limit(limit).offset(offset);

        return query.fetch(r -> {
            Task t = map(r.into(TASK));
            t.setLabels(Optional.ofNullable(r.get("labelIds", String.class))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Arrays.asList(s.split(",")))
                    .orElse(List.of()));
            return t;
        });
    }

    @Override
    public Task save(Task task) {
        return dsl.insertInto(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.STATUS, task.getStatus().getCode())
                .set(TASK.CREATED_AT, DateTimeUtils.now())
                .set(TASK.UPDATED_AT, DateTimeUtils.now())
                .returning(TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.STATUS, TASK.CREATED_AT, TASK.UPDATED_AT)
                .fetchOne(this::map);
    }
    @Override
    public Task update(Task task) {
        return dsl.update(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.STATUS, task.getStatus().getCode())
                .set(TASK.UPDATED_AT, DateTimeUtils.now())
                .where(TASK.ID.eq(task.getId()))
                .returning(TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.STATUS, TASK.CREATED_AT, TASK.UPDATED_AT)
                .fetchOne(this::map);
    }
    @Override
    public Optional<Task> findById(Integer id) {
        Task t = dsl.select(
                        TASK.ID,
                        TASK.TITLE,
                        TASK.STATUS,
                        TASK.CREATED_AT,
                        TASK.UPDATED_AT,
                        TASK.DESCRIPTION,
                        DSL.field("COALESCE(string_agg(label.name, ','), '')", String.class).as("labels")
                )
                .from(TASK)
                .leftJoin(TASK_LABEL).on(TASK.ID.eq(TASK_LABEL.TASK_ID))
                .leftJoin(LABEL).on(TASK_LABEL.LABEL_ID.eq(LABEL.ID))
                .where(TASK.ID.eq(id))
                .groupBy(TASK.ID)
                .fetchOne(r -> {
                    Task task = map(r.into(TASK));
                    task.setLabels(Optional.ofNullable(r.get("labels", String.class))
                            .filter(s -> !s.isEmpty())
                            .map(s -> Arrays.asList(s.split(",")))
                            .orElse(List.of()));
                    return task;
                });

        return Optional.ofNullable(t);
    }
    @Override
    public void deleteById(Integer id) {
        dsl.deleteFrom(TASK).where(TASK.ID.eq(id)).execute();
    }

    private Task map(TaskRecord r) {
        if (r == null) return null;
        return Task.builder()
                .id(r.get(TASK.ID))
                .title(r.get(TASK.TITLE))
                .description(r.get(TASK.DESCRIPTION))
                .status(TaskStatus.fromCode(r.get(TASK.STATUS)))
                .createdAt(DateTimeUtils.toInstant(r.get(TASK.CREATED_AT)))
                .updatedAt(DateTimeUtils.toInstant(r.get(TASK.UPDATED_AT)))
                .build();
    }

    private void applyFilters(SelectJoinStep<?> query,
                              String title,
                              TaskStatus status,
                              List<String> labelIds) {
        if (title != null && !title.isEmpty()) {
            query.where(TASK.TITLE.like("%" + title + "%"));
        }
        if (status != null) {
            query.where(TASK.STATUS.eq(status.getCode()));
        }
        if (labelIds != null && !labelIds.isEmpty()) {
            query.where(TASK.ID.in(
                    DSL.select(TASK_LABEL.TASK_ID)
                            .from(TASK_LABEL)
                            .leftJoin(LABEL).on(TASK_LABEL.LABEL_ID.eq(LABEL.ID))
                            .where(LABEL.ID.in(labelIds))
                            .groupBy(TASK_LABEL.TASK_ID)
                            .having(DSL.countDistinct(TASK_LABEL.LABEL_ID).ge(labelIds.size()))
            ));
        }
    }


}
