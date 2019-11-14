package ru.ilysenko.liquibase.backup.component;

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
import ru.ilysenko.liquibase.backup.enums.BackupFormat;
import ru.ilysenko.liquibase.backup.enums.SnapshotType;
import ru.ilysenko.liquibase.backup.properties.LiquibaseBackupProperties;

import java.sql.Connection;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LiquibaseBackuper {
    private static final String AUTHOR = "liquibase-backuper";
    private static final String CHANGELOG_FILE_NAME_TEMPLATE = "backup_%s.%s%s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final LiquibaseBackupDataSource dataSource;
    private final LiquibaseBackupProperties properties;

    @Scheduled(cron = "${backup.schedule}")
    public void backup() {
        log.info("Backing up data...");
        try (Connection connection = dataSource.getConnection()) {
            Database database = getDatabase(connection);
            generateChangeLog(getChangeLogFileName(database), database, SnapshotType.DATA, AUTHOR, makeDiffOutputControl());
        } catch (Exception e) {
            throw new RuntimeException("Error backing up data", e);
        }
        log.info("Backup completed");
    }

    @SneakyThrows
    private void generateChangeLog(String fileName, Database database, String snapshotTypes, String author, DiffOutputControl diffOutputControl) {
        CommandLineUtils.doGenerateChangeLog(fileName, database, null, null, snapshotTypes, author, null, null, diffOutputControl);
    }

    private String getChangeLogFileName(Database database) {
        String fileId = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        BackupFormat format = properties.getFormat();
        String extension = format.name().toLowerCase();
        String databaseType = format == BackupFormat.SQL ? String.format("%s.", database.getShortName()) : "";
        return String.format(CHANGELOG_FILE_NAME_TEMPLATE, fileId, databaseType, extension);
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
                .map(t -> MessageFormat.format("table:(?i){0}", t))
                .collect(Collectors.joining(","));
        StandardObjectChangeFilter filter = new StandardObjectChangeFilter(StandardObjectChangeFilter.FilterType.INCLUDE, tableNamesPattern);
        diffOutputControl.setObjectChangeFilter(filter);
    }
}
