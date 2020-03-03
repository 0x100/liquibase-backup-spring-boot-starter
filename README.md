# Liquibase Backup Starter

In theory this solution supports all databases supported by the Liquibase library, such as `PostgreSQL, Oracle, Microsoft SQL Server, MySQL, H2, Apache Derby, HSQLDB (HyperSQL), DB2, Firebird, MariaDB, Sybase, SQLite`, but that's not tested yet.

###
![build](https://github.com/0x100/liquibase-backup-spring-boot-starter/workflows/build/badge.svg?branch=master)
[![](https://jitpack.io/v/0x100/liquibase-backup-spring-boot-starter.svg)](https://jitpack.io/#0x100/liquibase-backup-spring-boot-starter)

##

## How to add it in your project

### 1. Add a dependency

#### Maven

- Add the `jitpack` repository in your `pom.xml`

    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ```

- Add the `n-loops` dependency

    ```xml
    <dependency>
        <groupId>com.github.0x100</groupId>
        <artifactId>liquibase-backup-spring-boot-starter</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
    ```

#### Gradle

- Add the `jitpack` repository in your root `build.gradle` at the end of repositories:
    ```groovy
    allprojects {
        repositories {
            // ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```

- Add the dependency
    ```groovy
    dependencies {
        implementation 'com.github.0x100:liquibase-backup-spring-boot-starter:master-SNAPSHOT'
    }
    ```
### 2. Config

Configure SMTP and backup params in your `application.yaml` (or `application.properties`) file. 

- sample `YAML`-config:
    ```yaml
    spring:
        mail:
            host: smtp.gmail.com
            port: 587
            username: sender@gmail.com
            password: password
         
    backup:
      enabled: true
      format: sql # or `xml`
      tables: # optional property
        - users
        - tasks
        - comments
      schedule: '0 0 3 ? * *' #cron schedule expression
      receiverEmail: receiver@your-domain.com
      deleteFileAfterSend: true # optional property
    ```
- sample `properties`-config:
    ```properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=sender@gmail.com
    spring.mail.password=password
         
    backup.enabled=true

    # or `xml`
    backup.format=sql

    # optional property
    backup.tables=users,tasks,comments

    #cron schedule expression
    backup.schedule='0 0 3 ? * *'
    backup.receiverEmail=receiver@your-domain.com

    # optional property
    backup.deleteFileAfterSend=true
    ```
        
    The `format` property is optional, can take `sql` or `xml` values.
    Default format is `sql`.
    
    The `tables` property is optional. If is not set all tables will backed up.
    
    The `deleteFileAfterSend` property is optional. This option determines whether will a generated changeset file deleted or not after sending its via email. 
    Default value is `true`.

### 3. Finally  

If you did everything correctly at your email at the scheduled time will be sent a backup file.

## How to contribute
Fork the repository, make changes, write a test for your code, send me a pull request. 
I will review your changes and apply them to the master branch shortly, provided they don't violate quality standards. 
To avoid frustration, before sending a pull request please run the Maven build:
```
$ mvnw clean package
```

##

Good luck and have fun!