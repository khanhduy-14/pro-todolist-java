package com.khanhduy14.todolist.task;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class GlobalE2ETest {

    @Autowired
    protected DataSource dataSource;

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    String testSchema;


    @BeforeAll
    void setupSchema() throws Exception {
        try (Connection conn = this.dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + testSchema);
        }
    }
//    @AfterAll
//    void tearDown() throws Exception {
//        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
//            stmt.execute("DROP SCHEMA IF EXISTS" + schemaName + "CASCADE");
//        }
//    }
}











