package dao;

import config.DBConnection;
import models.Employee;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class EmployeeDAO {
    // Login check
    public static Employee authenticate(String username, String password) throws SQLException {
        String sql = "SELECT EmployeeID, FullName, Username, PasswordHash, Role, Phone, Email FROM Employees WHERE Username=? AND PasswordHash=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, password);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setEmployeeID(rs.getInt("EmployeeID"));
                    e.setFullName(rs.getString("FullName"));
                    e.setUsername(rs.getString("Username"));
                    e.setPassword(rs.getString("PasswordHash"));
                    e.setRole(rs.getString("Role"));
                    e.setPhone(rs.getString("Phone"));
                    e.setEmail(rs.getString("Email"));
                    return e;
                }
            }
        }
        return null;
    }
    public static boolean register(Employee emp) throws SQLException {
        String sql = "INSERT INTO Employees (FullName, Username, PasswordHash, Role, Phone, Email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, emp.getFullName());
            p.setString(2, emp.getUsername());
            p.setString(3, emp.getPassword()); // ⚠️ hash this if needed
            p.setString(4, emp.getRole());
            p.setString(5, emp.getPhone());
            p.setString(6, emp.getEmail());
            return p.executeUpdate() > 0;
        }
    }
    // Update employee
    public static boolean update(Employee emp) throws SQLException {
        String sql = "UPDATE Employees SET FullName=?, Username=?, " +
                (emp.getPassword() != null ? "PasswordHash=?," : "") +
                " Role=?, Phone=?, Email=? WHERE EmployeeID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            int idx = 1;
            p.setString(idx++, emp.getFullName());
            p.setString(idx++, emp.getUsername());
            if (emp.getPassword() != null) {
                p.setString(idx++, emp.getPassword());
            }
            p.setString(idx++, emp.getRole());
            p.setString(idx++, emp.getPhone());
            p.setString(idx++, emp.getEmail());
            p.setInt(idx, emp.getEmployeeID());
            return p.executeUpdate() > 0;
        }
    }

    // Delete employee
    public static boolean delete(int employeeId) throws SQLException {
        String sql = "DELETE FROM Employees WHERE EmployeeID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, employeeId);
            return p.executeUpdate() > 0;
        }
    }





    // Get all employees
    public static List<Employee> getAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT EmployeeID, FullName, Username, Role, Phone, Email FROM Employees";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeID(rs.getInt("EmployeeID"));
                e.setFullName(rs.getString("FullName"));
                e.setUsername(rs.getString("Username"));
                e.setRole(rs.getString("Role"));
                e.setPhone(rs.getString("Phone"));
                e.setEmail(rs.getString("Email"));
                list.add(e);
            }
        }
        return list;
    }
}
