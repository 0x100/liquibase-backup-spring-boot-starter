package ru.ilysenko.liquibasebackuper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "backup")
class LiquibaseBackuperProperties {
    private Boolean enabled;
    private List<String> tables;
    private String schedule;
}