# About

In theory this solution supports all databases which supported by the Liquibase library: `PostgreSQL, Oracle, Microsoft SQL Server, MySQL, H2, Apache Derby, HSQLDB (HyperSQL), DB2, Firebird, MariaDB, Sybase, SQLite`.

# Steps to start using the starter

1. Run `mvn install` command in the root of this project (for install a dependency in the local Maven repository). 

2. Add the dependency in your POM:

    ```xml
    <dependency>
        <groupId>ru.ilysenko</groupId>
        <artifactId>liquibase-backup-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```

3. Configure SMTP and backup params in your application.yaml (or application.properties) file. Sample YAML-config:
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
      tables: # optional
        - users
        - tasks
        - comments
      schedule: '0 0 3 ? * *' #cron schedule expression
      receiverEmail: backup-receiver-user-email
      deleteFileAfterSend: true # optional
    ```
    The `format` property is optional, can take `sql` or `xml` values. Default format is `sql`.
    
    The `tables` property is optional. If not set all tables will backed up.
    
    The `deleteFileAfterSend` property is optional. Default value is `true`.