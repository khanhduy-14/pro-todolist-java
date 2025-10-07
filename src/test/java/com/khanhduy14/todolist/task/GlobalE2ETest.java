package com.khanhduy14.todolist.task;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class GlobalE2ETest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeAll
    void setupRestTemplate() {
        restTemplate.getRestTemplate().setRequestFactory(
                new BufferingClientHttpRequestFactory(restTemplate.getRestTemplate().getRequestFactory())
        );

        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            System.out.println("=== Request ===");
            System.out.println(request.getMethod() + " " + request.getURI());
            System.out.println("Headers: " + request.getHeaders());
            if (body.length > 0) {
                System.out.println("Body: " + new String(body, StandardCharsets.UTF_8));
            }

            ClientHttpResponse response = execution.execute(request, body);

            String respBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("=== Response ===");
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + respBody);

            return response;
        });
    }
    @AfterEach
    void cleanDatabase() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM task_label");
            stmt.executeUpdate("DELETE FROM label");
            stmt.executeUpdate("DELETE FROM task");
        }
    }
}











