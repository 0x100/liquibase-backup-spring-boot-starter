package ru.ilysenko.liquibase.backup.component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import ru.ilysenko.liquibase.backup.properties.SpringDataSourceProperties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class LiquibaseBackupDataSource {
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