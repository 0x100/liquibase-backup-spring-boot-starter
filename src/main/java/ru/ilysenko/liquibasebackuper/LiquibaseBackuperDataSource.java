package ru.ilysenko.liquibasebackuper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class LiquibaseBackuperDataSource {
    private final SpringDataSourceProperties properties;
    private DataSource ds;

    @PostConstruct
    public void init() {
        HikariConfig config = prepareConfig();
        ds = new HikariDataSource(config);
    }

    private HikariConfig prepareConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());
        return config;
    }

    Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}