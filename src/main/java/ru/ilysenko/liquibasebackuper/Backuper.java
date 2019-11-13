package ru.ilysenko.liquibasebackuper;

import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.StandardObjectChangeFilter;
import liquibase.integration.commandline.CommandLineUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Connection;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@RequiredArgsConstructor
public class Backuper {
    private static final String AUTHOR = "auto-backuper";
    private static final String CHANGELOG_FILE_NAME_TEMPLATE = "backup_{0}.xml";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("yyyyMMdd_HHmmss");

    private final LiquibaseBackuperDataSource dataSource;
    private final LiquibaseBackuperProperties properties;

    @Scheduled(cron = "${backup.schedule}")
    public void init() {
        log.info("Backing up...");
        try (Connection connection = dataSource.getConnection()) {
            generateChangeLog(getChangeLogFileName(), getDatabase(connection), SnapshotType.DATA, AUTHOR, makeDiffOutputControl());
        } catch (Exception e) {
            throw new RuntimeException("Error backing up data", e);
        }
        log.info("Backing up completed");
    }

    @SneakyThrows
    private void generateChangeLog(String fileName, Database database, String snapshotTypes, String author, DiffOutputControl diffOutputControl) {
        CommandLineUtils.doGenerateChangeLog(fileName, database, null, null, snapshotTypes, author, null, null, diffOutputControl);
    }

    private String getChangeLogFileName() {
        return MessageFormat.format(CHANGELOG_FILE_NAME_TEMPLATE, LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @SneakyThrows
    private Database getDatabase(Connection connection) {
        DatabaseConnection databaseConnection = new JdbcConnection(connection);
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(databaseConnection);
    }

    private DiffOutputControl makeDiffOutputControl() {
        DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, false, null);
        List<String> tables = properties.getTables();
        if (tables != null && !tables.isEmpty()) {
            setTablesFilter(diffOutputControl, tables);
        }
        return diffOutputControl;
    }

    private void setTablesFilter(DiffOutputControl diffOutputControl, List<String> tables) {
        String tableNamesPattern = tables.stream()
                .map(t -> MessageFormat.format("(?i){0}", t))
                .collect(Collectors.joining(","));
        StandardObjectChangeFilter filter = new StandardObjectChangeFilter(StandardObjectChangeFilter.FilterType.INCLUDE, tableNamesPattern);
        diffOutputControl.setObjectChangeFilter(filter);
    }
}
