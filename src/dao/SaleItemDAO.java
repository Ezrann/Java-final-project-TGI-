package dao;
import config.DBConnection;
import models.SaleItem;
import java.sql.*;

public class SaleItemDAO {
    public static void createSaleItem(SaleItem item) throws SQLException {
        String sql = "INSERT INTO SaleItems (SaleID, ProductID, Quantity, UnitPrice, Subtotal) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, item.getSaleID());
            p.setInt(2, item.getProductID());
            p.setInt(3, item.getQuantity());
            p.setDouble(4, item.getPrice());
            p.setDouble(5, item.getSubTotal());
            p.executeUpdate();
        }
    }
}
