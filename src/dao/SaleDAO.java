package dao;
import config.DBConnection;
import models.Sale;
import java.sql.*;

public class SaleDAO {
    public static int createSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO Sales (SaleDate, EmployeeID, CustomerID, TotalAmount, PaymentMethod) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setTimestamp(1, new Timestamp(sale.getSaleDate().getTime()));
            p.setInt(2, sale.getEmployeeID());
            if (sale.getCustomerID() == null) p.setNull(3, Types.INTEGER);
            else p.setInt(3, sale.getCustomerID());
            p.setDouble(4, sale.getTotalAmount());
            p.setString(5, sale.getPaymentMethod());
            p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}
