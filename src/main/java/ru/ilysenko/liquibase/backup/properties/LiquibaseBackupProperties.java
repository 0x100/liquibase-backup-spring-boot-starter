package ru.ilysenko.liquibase.backup.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.ilysenko.liquibase.backup.enums.BackupFormat;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "backup")
public
class LiquibaseBackupProperties {
    private boolean enabled;
    private BackupFormat format = BackupFormat.SQL;
    private List<String> tables;
    private String schedule;
    private String receiverEmail;
    private boolean deleteFileAfterSend = true;

    public void setFormat(String format) {
        this.format = BackupFormat.valueOf(format.toUpperCase());
    }
}