# About

In theory this solution supports all databases supported by the Liquibase library, such as `PostgreSQL, Oracle, Microsoft SQL Server, MySQL, H2, Apache Derby, HSQLDB (HyperSQL), DB2, Firebird, MariaDB, Sybase, SQLite`, but that's not tested yet.

# Steps to start using the starter

1. Run `mvn install` command in the root of this project (for install a dependency in the local Maven repository). 

2. Add the dependency
 
    - in case of using Maven:

        ```xml
        <dependency>
            <groupId>ru.ilysenko</groupId>
            <artifactId>liquibase-backup-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        ```
    
    - or if you are using Gradle: 
    
        ```groovy
            implementation 'ru.ilysenko:liquibase-backup-spring-boot-starter:0.0.1-SNAPSHOT'
        ```

3. Configure SMTP and backup params in your `application.yaml` (or `application.properties`) file. 

    - sample yaml-config:
        ```yaml
        spring:
            mail:
                host: smtp.your-email-service.com
                port: 587
                username: sender-user_or_email
                password: password
             
        backup:
          enabled: true
          format: sql # or `xml`
          tables: # optional property
            - users
            - tasks
            - comments
          schedule: '0 0 3 ? * *' #cron schedule expression
          receiverEmail: backup-receiver-user-email
          deleteFileAfterSend: true # optional property
        ```
    - sample properties-config:
        ```properties
        spring.mail.host=smtp.your-email-service.com
        spring.mail.port=587
        spring.mail.username=sender-user_or_email
        spring.mail.password=password
             
        backup.enabled=true

        # or `xml`
        backup.format=sql

        # optional property
        backup.tables=users,tasks,comments

         #cron schedule expression
        backup.schedule='0 0 3 ? * *'
        backup.receiverEmail=backup-receiver-user-email

        # optional property
        backup.deleteFileAfterSend=true
        ```
        
    The `format` property is optional, can take `sql` or `xml` values. Default format is `sql`.
    
    The `tables` property is optional. If not set all tables will backed up.
    
    The `deleteFileAfterSend` property is optional. Default value is `true`.
 
 4. Check your email at the scheduled time!