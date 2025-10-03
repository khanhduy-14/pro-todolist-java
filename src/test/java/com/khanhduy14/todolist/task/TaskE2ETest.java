package com.khanhduy14.todolist.task;

import com.khanhduy14.todolist.common.constant.SortOrder;
import com.khanhduy14.todolist.common.viewModel.PaginationResponse;
import com.khanhduy14.todolist.libs.jooq.generated.tables.records.LabelRecord;
import com.khanhduy14.todolist.modules.task.constant.TaskStatus;
import com.khanhduy14.todolist.modules.task.dto.TaskCreateReqDTO;
import com.khanhduy14.todolist.modules.task.dto.TaskUpdateReqDTO;
import com.khanhduy14.todolist.modules.task.entity.Task;
import com.khanhduy14.todolist.modules.task.params.TaskQueryParams;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class TaskE2ETest extends GlobalE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DSLContext dslContext;

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
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Task t1 = createTask("List Task A " + suffix, "Desc A", List.of("Label A"));
        Task t2 = createTask("List Task B " + suffix, "Desc B");

        TaskQueryParams params = new TaskQueryParams();
        PaginationResponse<Task> resp = getTasks(params);
        List<Task> tasks = resp.data();
        assertThat(resp.metadata().total()).isGreaterThanOrEqualTo(2);
        assertThat(resp.metadata().offset()).isEqualTo(params.getOffset());
        assertThat(resp.metadata().limit()).isEqualTo(params.getLimit());
        assertTaskExists(tasks, t1);
        assertTaskExists(tasks, t2);
    }

    @Test
    void shouldListTasksByTitle() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Task t1 = createTask("Task A " + suffix, "Desc A", List.of("Label A"));
        Task t2 = createTask("Task B " + suffix, "Desc B", List.of("Label B"));

        TaskQueryParams params = new TaskQueryParams();
        params.setTitle("A");

        PaginationResponse<Task> resp = getTasks(params);
        List<Task> tasks = resp.data();

        assertThat(tasks.size()).isEqualTo(1);
        assertTaskExists(tasks, t1);
        assertTaskNotExists(tasks, t2);
    }

    @Test
    void shouldListTasksByStatus() {
        Task t1 = createTask("Task Status 1", "Desc", List.of());
        Task t2 = createTask("Task Status 2", "Desc", List.of());

        TaskUpdateReqDTO updates = new TaskUpdateReqDTO(
               null,
                null,
                Optional.of(TaskStatus.IN_PROGRESS),
                null
        );

        restTemplate.exchange(
                BASE_URL + "/" + t2.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(updates),
                Task.class
        );

        TaskQueryParams params = new TaskQueryParams();
        params.setStatus(TaskStatus.IN_PROGRESS);

        PaginationResponse<Task> resp = getTasks(params);
        List<Task> tasks = resp.data();

        for (Task t : tasks) assertThat(t.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldListTasksByLabel() {
        LabelRecord label1 = dslContext.insertInto(LABEL).set(LABEL.NAME, "1").returning().fetchOne();
        LabelRecord label2 = dslContext.insertInto(LABEL).set(LABEL.NAME, "2").returning().fetchOne();
        dslContext.insertInto(LABEL).set(LABEL.NAME, "3").returning().fetchOne();
        Task t1 = createTask("Task Label A", "Desc", List.of("1", "2"));
        Task t2 = createTask("Task Label B", "Desc", List.of("2","3"));
        TaskQueryParams params = new TaskQueryParams();
        params.setLabelIds(List.of(label1.getId().toString(), label2.getId().toString()));

        PaginationResponse<Task> resp = getTasks(params);
        List<Task> tasks = resp.data();

        assertTaskExists(tasks, t1);
        assertTaskNotExists(tasks, t2);
    }

    @Test
    void shouldListTasksWithPaginationAndSort() {
        for (int i = 1; i <= 10; i++) {
            createTask("Paginated Task " + i, "Desc", List.of());
        }

        TaskQueryParams params = new TaskQueryParams();
        params.setOffset(0);
        params.setLimit(7);
        params.setSortBy("createdAt");
        params.setSortOrder(SortOrder.DESC);

        PaginationResponse<Task> resp = getTasks(params);
        List<Task> tasks = resp.data();

        assertThat(tasks.size()).isEqualTo(7);
        assertThat(tasks.get(0).getCreatedAt()).isAfterOrEqualTo(tasks.get(4).getCreatedAt());
    }

    @Test
    void shouldReturnBadRequestWhenSortOrderInvalid() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("sortOrder", "descend");

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String body = response.getBody();
        assertThat(body).contains("Failed to convert property value");
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
        return createTask(title, description, List.of("Default Label A", "Default Label B"));
    }

    private Task createTask(String title, String description, List<String> labels) {
        if (labels == null) {
            labels = List.of();
        }

        TaskCreateReqDTO req = new TaskCreateReqDTO(title, description, labels);
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

    private void assertTaskExists(List<Task> tasks, Task expected) {
        assertThat(tasks)
                .extracting(Task::getId, Task::getTitle)
                .contains(tuple(expected.getId(), expected.getTitle()));
    }

    private void assertTaskNotExists(List<Task> tasks, Task unexpected) {
        assertThat(tasks)
                .extracting(Task::getId, Task::getTitle)
                .doesNotContain(tuple(unexpected.getId(), unexpected.getTitle()));
    }

    private PaginationResponse<Task> getTasks(TaskQueryParams params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);

        if (params.getTitle() != null) builder.queryParam("title", params.getTitle());
        if (params.getStatus() != null) builder.queryParam("status", params.getStatus());

        if (params.getLabelIds() != null) {
            for (String label : params.getLabelIds()) {
                builder.queryParam("labelIds", label);
            }
        }
        builder.queryParam("offset", params.getOffset());
        builder.queryParam("limit", params.getLimit());
        builder.queryParam("sortBy", params.getSortBy());
        builder.queryParam("sortOrder", params.getSortOrder().name());

        ResponseEntity<PaginationResponse<Task>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<PaginationResponse<Task>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().metadata().offset()).isEqualTo(params.getOffset());
        assertThat(response.getBody().metadata().limit()).isEqualTo(params.getLimit());
        assertThat(response.getBody().metadata().total()).isNotNull();
        return response.getBody();
    }

}
