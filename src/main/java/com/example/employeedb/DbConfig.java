package com.example.employeedb;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Database configuration holder.
 *
 * Loads from a properties file if present, otherwise falls back to environment variables:
 *  - DB_URL
 *  - DB_USER
 *  - DB_PASSWORD
 *  - DB_DRIVER
 */
public class DbConfig {
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    public DbConfig(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public static DbConfig loadFromFile(Path path) throws IOException {
        Properties p = new Properties();
        if (path != null && Files.exists(path) && Files.isRegularFile(path)) {
            try (FileInputStream in = new FileInputStream(path.toFile())) {
                p.load(in);
            }
        }

        String url = firstNonBlank(p.getProperty("url"), System.getenv("DB_URL"));
        String user = firstNonBlank(p.getProperty("user"), System.getenv("DB_USER"));
        String password = firstNonBlank(p.getProperty("password"), System.getenv("DB_PASSWORD"));
        String driver = firstNonBlank(p.getProperty("driver"), System.getenv("DB_DRIVER"));

        if (url == null || url.isBlank()) {
            throw new IOException("No JDBC URL configured. Set 'url' in config/db.properties or the environment variable DB_URL.");
        }

        return new DbConfig(url.trim(), safeTrim(user), safeTrim(password), safeTrim(driver));
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String safeTrim(String s) { return s == null ? null : s.trim(); }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
}
