package ru.ilysenko.liquibase.backup.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.ilysenko.liquibase.backup.component.LiquibaseBackupDataSource;
import ru.ilysenko.liquibase.backup.component.LiquibaseBackuper;
import ru.ilysenko.liquibase.backup.properties.SpringDataSourceProperties;
import ru.ilysenko.liquibase.backup.properties.LiquibaseBackupProperties;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "backup.enabled", havingValue = "true")
@EnableConfigurationProperties({LiquibaseBackupProperties.class, SpringDataSourceProperties.class})
public class LiquibaseBackupAutoConfiguration {

    @Bean
    LiquibaseBackupDataSource backuperDataSource(SpringDataSourceProperties properties) {
        return new LiquibaseBackupDataSource(properties);
    }

    @Bean
    LiquibaseBackuper backuper(LiquibaseBackupDataSource dataSource, LiquibaseBackupProperties properties) {
        return new LiquibaseBackuper(dataSource, properties);
    }
}