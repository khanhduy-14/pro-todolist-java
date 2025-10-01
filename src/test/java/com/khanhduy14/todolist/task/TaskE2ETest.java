package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.task.constant.TaskStatus;
import com.khanhduy14.todolist.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.task.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskE2ETest extends GlobalE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/tasks";

    @Test
    void shouldCreateTask() {
        TaskCreateReqDTO req = new TaskCreateReqDTO("Test task", "Test description", List.of("Label A", "Label B"));

        ResponseEntity<Task> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(req),
                Task.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(req.title());
        assertThat(response.getBody().getId()).isGreaterThan(0);
        assertThat(response.getBody().getStatus()).isEqualTo(TaskStatus.TO_DO);
        assertThat(response.getBody().getDescription()).isEqualTo(req.description());
        assertThat(response.getBody().getLabels()).containsExactlyInAnyOrderElementsOf(req.labels());
    }

    @Test
    void shouldListTasks() {
        // Create two unique tasks
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Task t1 = createTask("List Task A " + suffix, "Desc A");
        Task t2 = createTask("List Task B " + suffix, "Desc B");

        ResponseEntity<Task[]> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Task[].class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<Task> tasks = Arrays.asList(response.getBody());
        assertThat(tasks.stream().anyMatch(t -> t.getId() == t1.getId() && t.getTitle().equals(t1.getTitle()))).isTrue();
        assertThat(tasks.stream().anyMatch(t -> t.getId() == t2.getId() && t.getTitle().equals(t2.getTitle()))).isTrue();
    }

    @Test
    void shouldGetTaskDetail() {
        Task created = createTask("Detail Task", "Detail desc");

        ResponseEntity<Task> response = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Task.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Task body = response.getBody();
        assertThat(body.getId()).isEqualTo(created.getId());
        assertThat(body.getTitle()).isEqualTo(created.getTitle());
        assertThat(body.getDescription()).isEqualTo(created.getDescription());
        assertThat(body.getLabels()).containsExactlyInAnyOrderElementsOf(created.getLabels());
        assertThat(body.getStatus()).isEqualTo(TaskStatus.TO_DO);
    }

    @Test
    void shouldPatchTask() {
        Task created = createTask("Patch Task", "Patch desc");

        TaskUpdateReqDTO updates = new TaskUpdateReqDTO(
                Optional.of("Patched Title"),
                Optional.empty(),
                Optional.of(TaskStatus.IN_PROGRESS),
                Optional.of(List.of("Label X Updated", "Label B"))
        );

        ResponseEntity<Task> response = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(updates),
                Task.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Task updated = response.getBody();
        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getTitle()).isEqualTo(updates.title().orElse(null));
        assertThat(updated.getDescription()).isEqualTo(created.getDescription());
        assertThat(updated.getLabels()).containsExactlyInAnyOrderElementsOf(updates.labels().orElse(null));
        assertThat(updated.getStatus()).isEqualTo(updates.status().orElse(null));
    }

    @Test
    void shouldDeleteTask() {
        Task created = createTask("Delete Task", "Delete desc");

        ResponseEntity<Void> deleteResp = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getAfterDelete = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    private Task createTask(String title, String description) {
        TaskCreateReqDTO req = new TaskCreateReqDTO(title, description,  List.of("Label A", "Label B", "Label C"));
        ResponseEntity<Task> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(req),
                Task.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }
}
