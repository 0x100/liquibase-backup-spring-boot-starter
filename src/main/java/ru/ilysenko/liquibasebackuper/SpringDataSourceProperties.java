package ru.ilysenko.liquibasebackuper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("spring.datasource")
public class SpringDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}