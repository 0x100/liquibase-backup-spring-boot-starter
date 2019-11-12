package ru.ilysenko.liquibasebackuper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "backup.enabled", havingValue = "true")
@EnableConfigurationProperties({LiquibaseBackuperProperties.class, SpringDataSourceProperties.class})
public class LiquibaseBackuperAutoConfiguration {

    @Bean
    LiquibaseBackuperDataSource backuperDataSource(SpringDataSourceProperties properties) {
        return new LiquibaseBackuperDataSource(properties);
    }

    @Bean
    Backuper backuper(LiquibaseBackuperDataSource dataSource, LiquibaseBackuperProperties properties) {
        return new Backuper(dataSource, properties);
    }
}