package dao;

import config.DBConnection;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public static List<Product> getAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, ProductName, Category, Price, StockQuantity, ReorderLevel, SupplierID FROM Products";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setName(rs.getString("ProductName"));
                p.setCategory(rs.getString("Category"));
                p.setPrice(rs.getDouble("Price"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setReorderLevel(rs.getInt("ReorderLevel"));
                p.setSupplierID(rs.getInt("SupplierID"));
                list.add(p);
            }
        }
        return list;
    }

    public static Product findById(int id) throws SQLException {
        String sql = "SELECT ProductID, ProductName, Category, Price, StockQuantity, ReorderLevel, SupplierID FROM Products WHERE ProductID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    Product prod = new Product();
                    prod.setProductID(rs.getInt("ProductID"));
                    prod.setName(rs.getString("ProductName"));
                    prod.setCategory(rs.getString("Category"));
                    prod.setPrice(rs.getDouble("Price"));
                    prod.setStockQuantity(rs.getInt("StockQuantity"));
                    prod.setReorderLevel(rs.getInt("ReorderLevel"));
                    prod.setSupplierID(rs.getInt("SupplierID"));
                    return prod;
                }
            }
        }
        return null;
    }

    public static int updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE Products SET StockQuantity=? WHERE ProductID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, newStock);
            p.setInt(2, productId);
            return p.executeUpdate();
        }
    }

    // 🔹 Update product
    public static int update(Product p) throws SQLException {
        String sql = "UPDATE Products SET ProductName=?, Category=?, Price=?, StockQuantity=?, ReorderLevel=?, SupplierID=? WHERE ProductID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStockQuantity());
            ps.setInt(5, p.getReorderLevel());
            ps.setInt(6, p.getSupplierID());
            ps.setInt(7, p.getProductID());
            return ps.executeUpdate();
        }
    }

    // 🔹 Delete product
    public static int delete(int productId) throws SQLException {
        String sql = "DELETE FROM Products WHERE ProductID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate();
        }
    }
}
