package com.khanhduy14.todolist.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanhduy14.todolist.config.module.DbConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class BootstrapConfig {

    @Bean
    public AppConfig appConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AppConfig config = mapper.readValue(new ClassPathResource("config.json").getInputStream(), AppConfig.class);
        return config;
    }

    @Bean
    public DataSource dataSource(AppConfig config) {
        DbConfig db = config.getDatabase().getPrimary();
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(db.getDriverClassName());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());
        System.out.println("💾 Setup datasource: url=" + db.getUrl() + ", user=" + db.getUsername());

        return ds;
    }

}
