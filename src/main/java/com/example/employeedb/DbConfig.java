package com.example.employeedb;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

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
        try (FileInputStream in = new FileInputStream(path.toFile())) {
            p.load(in);
        }
        String url = p.getProperty("url", "");
        String user = p.getProperty("user", "");
        String password = p.getProperty("password", "");
        String driver = p.getProperty("driver", "");
        return new DbConfig(url, user, password, driver);
    }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
}
