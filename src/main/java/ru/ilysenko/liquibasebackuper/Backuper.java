package ru.ilysenko.liquibasebackuper;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.StandardObjectChangeFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Connection;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static liquibase.integration.commandline.CommandLineUtils.doGenerateChangeLog;

@Slf4j
@RequiredArgsConstructor
public class Backuper {
    private static final String AUTHOR = "auto-backuper";
    private static final String CHANGELOG_FILE_NAME_TEMPLATE = "backup_{0}.xml";

    private final LiquibaseBackuperDataSource dataSource;
    private final LiquibaseBackuperProperties properties;

    @Scheduled(cron = "${backup.schedule}")
    public void init() {
        log.info("Backing up...");
        try (Connection connection = dataSource.getConnection()) {
            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
            DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false, null);

            List<String> tables = properties.getTables();
            if (tables != null && !tables.isEmpty()) {
                setTablesFilter(diffOutputControl, tables);
            }
            String changeLogFile = MessageFormat.format(CHANGELOG_FILE_NAME_TEMPLATE, LocalDateTime.now().format(ofPattern("yyyyMMdd_HHmmss")));
            doGenerateChangeLog(changeLogFile, database, null, null, SnapshotType.DATA, AUTHOR, null, null, diffOutputControl);
        } catch (Exception e) {
            throw new RuntimeException("Error backing up data", e);
        }
        log.info("Backing up completed");
    }

    private void setTablesFilter(DiffOutputControl diffOutputControl, List<String> tables) {
        String tableNamesPattern = tables.stream()
                .map(t -> MessageFormat.format("(?i){0}", t))
                .collect(Collectors.joining(","));
        StandardObjectChangeFilter filter = new StandardObjectChangeFilter(StandardObjectChangeFilter.FilterType.INCLUDE, tableNamesPattern);
        diffOutputControl.setObjectChangeFilter(filter);
    }
}
