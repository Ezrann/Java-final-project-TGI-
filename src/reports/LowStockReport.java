package reports;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LowStockReport {
    public static Object[][] getData() throws SQLException {
        String sql = "SELECT ProductID, ProductName, StockQuantity, ReorderLevel FROM Products WHERE StockQuantity <= ReorderLevel";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getInt("StockQuantity"),
                        rs.getInt("ReorderLevel")
                });
            }
        }
        return rows.toArray(new Object[0][]);
    }
}
