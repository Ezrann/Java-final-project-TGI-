package reports;

import config.DBConnection;
import java.sql.*;

public class DailySalesReport {
    public static Object[][] getData(java.sql.Date date) throws SQLException {
        String sql = """
            SELECT COUNT(SaleID) AS Transactions, SUM(TotalAmount) AS Revenue
            FROM Sales
            WHERE DATE(SaleDate) = ?
        """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setDate(1, date);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Object[][] {
                            {rs.getInt("Transactions"), rs.getDouble("Revenue")}
                    };
                }
            }
        }
        return new Object[0][0];
    }
}
