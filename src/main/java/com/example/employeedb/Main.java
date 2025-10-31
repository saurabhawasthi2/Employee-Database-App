package com.example.employeedb;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path cfgPath = Path.of("config", "db.properties");
        System.out.println("Loading DB config from: " + cfgPath.toAbsolutePath());
        DbConfig cfg;
        try {
            cfg = DbConfig.loadFromFile(cfgPath);
        } catch (Exception e) {
            System.err.println("Failed to load config file: " + e.getMessage());
            System.err.println("Please copy and edit config/db.properties with your DB connection details.");
            return;
        }

        EmployeeDao dao = new EmployeeDao(cfg);
        try {
            dao.createTableIfNotExists();

            System.out.println("Creating sample employee...");
            Employee emp = new Employee("Alice Example", "alice@example.com", "Engineering");
            emp = dao.create(emp);
            System.out.println("Created: " + emp);

            System.out.println("Listing employees:");
            List<Employee> list = dao.listAll();
            list.forEach(System.out::println);

            System.out.println("Updating employee...");
            emp.setDepartment("R&D");
            boolean updated = dao.update(emp);
            System.out.println("Updated: " + updated);

            System.out.println("Find by id: " + emp.getId());
            Employee found = dao.findById(emp.getId());
            System.out.println(found);

            System.out.println("Deleting employee: " + emp.getId());
            boolean deleted = dao.delete(emp.getId());
            System.out.println("Deleted: " + deleted);

            System.out.println("Final list:");
            dao.listAll().forEach(System.out::println);

        } catch (SQLException sqe) {
            System.err.println("SQL error: " + sqe.getMessage());
            sqe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Driver class not found: " + cnfe.getMessage());
            cnfe.printStackTrace();
        }
    }
}
