package com.example.employeedb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    public static Connection getConnection(DbConfig cfg) throws SQLException, ClassNotFoundException {
        String driver = cfg.getDriver();
        if (driver != null && !driver.isBlank()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                // rethrow with more context
                throw new ClassNotFoundException("JDBC driver class not found: " + driver, e);
            }
        }
        return DriverManager.getConnection(cfg.getUrl(), cfg.getUser(), cfg.getPassword());
    }
}
