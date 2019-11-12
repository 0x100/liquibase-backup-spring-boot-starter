# About

In theory supports all databases which supported by the Liquibase library: PostgreSQL, Oracle, Microsoft SQL Server, MySQL, H2, Apache Derby, HSQLDB (HyperSQL), DB2, Firebird, MariaDB, Sybase, SQLite.

# Steps to start using the Backuper starter

1. Run `mvn install` command in the root of this project (for install a dependency in the local Maven repository). 

2. Add the dependency in your POM:

```xml
<dependency>
    <groupId>ru.ilysenko</groupId>
    <artifactId>liquibase-backuper-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

3. Configure the Backuper . Sample config:
```yaml
backup:
  enabled: true
  tables:
    - users
    - tasks
    - comments
  schedule: '0 0 3 ? * *'
```
