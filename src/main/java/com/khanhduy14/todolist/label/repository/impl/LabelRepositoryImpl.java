package com.khanhduy14.todolist.label.repository.impl;

import com.khanhduy14.todolist.label.entity.Label;
import com.khanhduy14.todolist.label.repository.LabelRepository;
import com.khanhduy14.todolist.libs.jooq.generated.tables.records.LabelRecord;
import com.khanhduy14.todolist.utils.DateTimeUtils;
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
        return dsl.insertInto(LABEL)
                .set(LABEL.NAME, name)
                .set(LABEL.CREATED_AT, DateTimeUtils.now())
                .set(LABEL.UPDATED_AT, DateTimeUtils.now())
                .returning(LABEL.ID, LABEL.NAME, LABEL.CREATED_AT, LABEL.UPDATED_AT)
                .fetchOne(this::map);
    }

    @Override
    public List<Label> bulkSave(List<String> names) {
        var step = dsl.insertInto(LABEL, LABEL.NAME, LABEL.CREATED_AT, LABEL.UPDATED_AT);
        for (String n : names) {
            step.values(n, DateTimeUtils.now(), DateTimeUtils.now());
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
                .createdAt(DateTimeUtils.toInstant(r.get(LABEL.CREATED_AT)))
                .updatedAt(DateTimeUtils.toInstant(r.get(LABEL.UPDATED_AT)))
                .build();
    }
}
