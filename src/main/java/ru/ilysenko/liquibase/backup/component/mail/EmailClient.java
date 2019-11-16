package ru.ilysenko.liquibase.backup.component.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import ru.ilysenko.liquibase.backup.properties.LiquibaseBackupProperties;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@RequiredArgsConstructor
public class EmailClient {
    private final JavaMailSender mailSender;
    private final LiquibaseBackupProperties properties;

    public void sendFile(String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(fileName);

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(properties.getReceiverEmail());
            helper.addAttachment(fileName, new File(fileName));
            helper.setText("See attached file " + fileName);

            log.info("Sending the {} file to {}...", fileName, properties.getReceiverEmail());
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Can't send email", e);
        }
    }
}
