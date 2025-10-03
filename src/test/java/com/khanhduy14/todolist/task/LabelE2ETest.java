package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.modules.label.entity.Label;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import static com.khanhduy14.todolist.libs.jooq.generated.Tables.LABEL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelE2ETest extends  GlobalE2ETest{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DSLContext dsl;

    private static final String BASE_URL = "/labels";

    @Test
    void shouldListLabel() {
        dsl.insertInto(LABEL)
                .set(LABEL.NAME, "Work")
                .execute();

        dsl.insertInto(LABEL)
                .set(LABEL.NAME, "Study")
                .execute();


        List<Label> labels = getLabels();
        assertThat(labels).isNotNull();
        assertThat(labels).isNotEmpty();
        for (Label label : labels) {
            assertThat(label.getId()).isNotNull();
            assertThat(label.getName()).isNotNull();
            assertThat(label.getName()).isIn("Work", "Study");
            assertThat(label.getCreatedAt()).isNotNull();
            assertThat(label.getUpdatedAt()).isNotNull();
        }
    }


    private List<Label> getLabels() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);


        ResponseEntity<List<Label>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Label>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
