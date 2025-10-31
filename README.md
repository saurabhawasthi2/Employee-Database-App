# Employee-Database-App

Master JDBC Connectivity

This project is a minimal Java (Maven) application demonstrating JDBC connectivity to MySQL or PostgreSQL and performing basic CRUD operations on an `employees` table.

Files added:
- `pom.xml` — Maven build file with MySQL and PostgreSQL drivers
- `config/db.properties` — sample DB config (edit with your DB connection details)
- `src/main/java/com/example/employeedb/` — Java sources (DbConfig, DbUtil, Employee, EmployeeDao, Main)

Quick start
1. Edit `config/db.properties` and set `url`, `user`, `password`, and optionally `driver` for your database.
2. Create the database (for example `employees_db`) in your MySQL or PostgreSQL server.
3. Build and run:

```bash
# from project root
mvn -q package
mvn exec:java -Dexec.mainClass="com.example.employeedb.Main"
```

The `Main` class will attempt to create the `employees` table (DB-specific DDL) and run a simple sequence of create/read/update/delete operations, printing output to the console.

If Maven is not installed in your environment, you can compile and run the sources with `javac` and `java`, but you'll need to provide the JDBC driver jars on the classpath.

Notes
- The project includes both MySQL and PostgreSQL JDBC drivers. Choose the appropriate JDBC URL in `config/db.properties`.
- The create-table DDL adapts for MySQL (AUTO_INCREMENT) vs PostgreSQL (SERIAL) based on the JDBC URL.

Environment variables
You can alternatively provide configuration through environment variables. The app looks for these variables when `config/db.properties` is not present or doesn't contain values:

- `DB_URL` — JDBC URL (required)
- `DB_USER` — DB username (optional if embedded auth used)
- `DB_PASSWORD` — DB password
- `DB_DRIVER` — JDBC driver class name (optional; drivers are on the classpath)

Example (bash):

```bash
export DB_URL="jdbc:postgresql://localhost:5432/employees_db"
export DB_USER=postgres
export DB_PASSWORD=secret
mvn -DskipTests package
mvn exec:java -Dexec.mainClass="com.example.employeedb.Main"
```

# Employee-Database-App
Master JDBC Connectivity
