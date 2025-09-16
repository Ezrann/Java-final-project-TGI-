package dao;

import config.DBConnection;
import models.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    // Create
    public static void insert(Supplier s) throws SQLException {
        String sql = "INSERT INTO Suppliers (SupplierName, ContactName, Phone, Email, Address) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());
            ps.executeUpdate();
        }
    }

    // Read
    public static List<Supplier> getAll() throws SQLException {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT SupplierID, SupplierName, ContactName, Phone, Email, Address FROM Suppliers";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("SupplierID"));
                s.setSupplierName(rs.getString("SupplierName"));
                s.setContactName(rs.getString("ContactName"));
                s.setPhone(rs.getString("Phone"));
                s.setEmail(rs.getString("Email"));
                s.setAddress(rs.getString("Address"));
                list.add(s);
            }
        }
        return list;
    }

    // Update
    public static void update(Supplier s) throws SQLException {
        String sql = "UPDATE Suppliers SET SupplierName=?, ContactName=?, Phone=?, Email=?, Address=? WHERE SupplierID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());
            ps.setInt(6, s.getSupplierID());
            ps.executeUpdate();
        }
    }

    // Delete
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM Suppliers WHERE SupplierID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
