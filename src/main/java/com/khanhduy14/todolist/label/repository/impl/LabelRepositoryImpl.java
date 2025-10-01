package com.khanhduy14.todolist.label.repository.impl;

import com.khanhduy14.todolist.label.entity.Label;
import com.khanhduy14.todolist.label.repository.LabelRepository;
import com.khanhduy14.todolist.libs.jooq.generated.tables.records.LabelRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.khanhduy14.todolist.libs.jooq.generated.Tables.LABEL;

@Repository
public class LabelRepositoryImpl implements LabelRepository {
    private final DSLContext dsl;

    public LabelRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<Label> findByNames(List<String> names) {
        return dsl
                .selectFrom(LABEL)
                .where(LABEL.NAME.in(names))
                .fetch(this::map);
    }

    @Override
    public Label save(String name) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return dsl.insertInto(LABEL)
                .set(LABEL.NAME, name)
                .set(LABEL.CREATED_AT, now)
                .set(LABEL.UPDATED_AT, now)
                .returning(LABEL.ID, LABEL.NAME, LABEL.CREATED_AT, LABEL.UPDATED_AT)
                .fetchOne(this::map);
    }

    @Override
    public List<Label> bulkSave(List<String> names) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        var step = dsl.insertInto(LABEL, LABEL.NAME, LABEL.CREATED_AT, LABEL.UPDATED_AT);
        for (String n : names) {
            step.values(n, now, now);
        }
        return step
                .returning(LABEL.ID, LABEL.NAME, LABEL.CREATED_AT, LABEL.UPDATED_AT)
                .fetch(this::map);
    }

    private Label map(LabelRecord r) {
        if (r == null) return null;
        return Label.builder()
                .id(r.get(LABEL.ID))
                .name(r.get(LABEL.NAME))
                .createdAt(r.get(LABEL.CREATED_AT).toInstant(ZoneOffset.UTC))
                .updatedAt(r.get(LABEL.UPDATED_AT).toInstant(ZoneOffset.UTC))
                .build();
    }
}
