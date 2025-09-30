package com.khanhduy14.todolist.task.repository.impl;

import com.khanhduy14.todolist.libs.jooq.generated.tables.records.TaskRecord;
import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.entity.Task;
import com.khanhduy14.todolist.task.repository.TaskRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static com.khanhduy14.todolist.libs.jooq.generated.tables.Task.TASK;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;


@Repository
public class TaskRepositoryImpl  implements TaskRepository {

    private final DSLContext dsl;


    public TaskRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Task> findAll() {
        return dsl
                .selectFrom(TASK)
                .fetch(this::map);
    }
    @Override
    public Task save(Task task) {
        return dsl.insertInto(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.STATUS, task.getStatus().getCode())
                .set(TASK.CREATED_AT, LocalDateTime.now(ZoneOffset.UTC))
                .set(TASK.UPDATED_AT, LocalDateTime.now(ZoneOffset.UTC))
                .returning(TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.STATUS, TASK.CREATED_AT, TASK.UPDATED_AT)
                .fetchOne(this::map);
    }
    @Override
    public Task update(Task task) {
        return dsl.update(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.STATUS, task.getStatus().getCode())
                .set(TASK.UPDATED_AT, LocalDateTime.now(ZoneOffset.UTC))
                .where(TASK.ID.eq(task.getId()))
                .returning(TASK.ID, TASK.TITLE, TASK.DESCRIPTION, TASK.STATUS, TASK.CREATED_AT, TASK.UPDATED_AT)
                .fetchOne(this::map);
    }
    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(
                dsl.selectFrom(TASK)
                        .where(TASK.ID.eq(id))
                        .fetchOne(this::map)
        );
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
                .createdAt(r.get(TASK.CREATED_AT).toInstant(ZoneOffset.UTC))
                .updatedAt(r.get(TASK.UPDATED_AT).toInstant(ZoneOffset.UTC))
                .build();
    }

}
