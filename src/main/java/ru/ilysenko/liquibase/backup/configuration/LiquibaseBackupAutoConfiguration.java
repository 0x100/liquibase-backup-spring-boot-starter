package ru.ilysenko.liquibase.backup.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.ilysenko.liquibase.backup.component.LiquibaseBackupDataSource;
import ru.ilysenko.liquibase.backup.component.LiquibaseBackuper;
import ru.ilysenko.liquibase.backup.component.mail.EmailClient;
import ru.ilysenko.liquibase.backup.properties.LiquibaseBackupProperties;
import ru.ilysenko.liquibase.backup.properties.SpringDataSourceProperties;

@Configuration
@EnableScheduling
@ComponentScan("org.springframework.boot.autoconfigure.mail")
@ConditionalOnProperty(value = "backup.enabled", havingValue = "true")
@EnableConfigurationProperties({LiquibaseBackupProperties.class, SpringDataSourceProperties.class})
public class LiquibaseBackupAutoConfiguration {

    @Bean
    LiquibaseBackupDataSource backuperDataSource(SpringDataSourceProperties properties) {
        return new LiquibaseBackupDataSource(properties);
    }

    @Bean
    @ConditionalOnProperty(value = "spring.mail.username")
    EmailClient emailClient(JavaMailSender mailSender, LiquibaseBackupProperties properties) {
        return new EmailClient(mailSender, properties);
    }

    @Bean
    LiquibaseBackuper backuper(LiquibaseBackupDataSource dataSource, LiquibaseBackupProperties properties, JavaMailSender mailSender) {
        return new LiquibaseBackuper(dataSource, properties, emailClient(mailSender, properties));
    }
}