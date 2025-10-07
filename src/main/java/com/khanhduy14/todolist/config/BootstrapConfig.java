package com.khanhduy14.todolist.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khanhduy14.todolist.config.module.DbConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.flywaydb.core.Flyway;
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

    @Bean(initMethod = "migrate")
    public Flyway flyway(AppConfig appConfig) {
        DbConfig db = appConfig.getDatabase().getPrimary();
        var fw = appConfig.getFlyway();

        if (fw == null || !fw.isEnabled()) {
            System.out.println("⚠️ Flyway disabled in config.json");
            return null;
        }

        System.out.println("🚀 Initializing Flyway...");
        System.out.println("💾 DB URL: " + db.getUrl());
        System.out.println("👤 DB User: " + db.getUsername());
        System.out.println("📂 Flyway Locations: " + fw.getLocations());
        System.out.println("🛠️ baselineOnMigrate: " + fw.isBaselineOnMigrate());

        Flyway flyway = Flyway.configure()
                .dataSource(db.getUrl(), db.getUsername(), db.getPassword())
                .locations(fw.getLocations().toArray(new String[0]))
                .baselineOnMigrate(fw.isBaselineOnMigrate())
                .load();

        System.out.println("✅ Flyway bean created. Migration will run now...");
        return flyway;
    }
}
