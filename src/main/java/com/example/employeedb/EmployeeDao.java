package com.example.employeedb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private final DbConfig cfg;

    public EmployeeDao(DbConfig cfg) {
        this.cfg = cfg;
    }

    private boolean isPostgres() {
        String url = cfg.getUrl();
        return url != null && url.startsWith("jdbc:postgresql:");
    }

    public void createTableIfNotExists() throws SQLException, ClassNotFoundException {
        String ddl;
        if (isPostgres()) {
            ddl = "CREATE TABLE IF NOT EXISTS employees (id SERIAL PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), department VARCHAR(50))";
        } else {
            // assume MySQL-style
            ddl = "CREATE TABLE IF NOT EXISTS employees (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), department VARCHAR(50))";
        }
        try (Connection conn = DbUtil.getConnection(cfg);
             Statement st = conn.createStatement()) {
            st.execute(ddl);
        }
    }

    public Employee create(Employee e) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO employees(name, email, department) VALUES (?, ?, ?)";
        try (Connection conn = DbUtil.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Insert failed, no rows affected");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    e.setId(keys.getLong(1));
                }
            }
            return e;
        }
    }

    public Employee findById(long id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT id, name, email, department FROM employees WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Employee> listAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT id, name, email, department FROM employees ORDER BY id";
        List<Employee> out = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(mapRow(rs));
        }
        return out;
    }

    public boolean update(Employee e) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE employees SET name = ?, email = ?, department = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            ps.setLong(4, e.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getLong("id"));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setDepartment(rs.getString("department"));
        return e;
    }
}
