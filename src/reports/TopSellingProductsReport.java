package reports;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopSellingProductsReport {
    public static Object[][] getData() throws SQLException {
        String sql = """
            SELECT p.ProductName, SUM(si.Quantity) AS TotalSold
            FROM SaleItems si
            JOIN Products p ON si.ProductID = p.ProductID
            GROUP BY p.ProductName
            ORDER BY TotalSold DESC
            LIMIT 10
        """;
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getString("ProductName"),
                        rs.getInt("TotalSold")
                });
            }
        }
        return rows.toArray(new Object[0][]);
    }
}
