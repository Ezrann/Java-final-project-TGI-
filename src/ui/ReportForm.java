package ui;

import reports.DailySalesReport;
import reports.LowStockReport;
import reports.TopSellingProductsReport;

import com.toedter.calendar.JDateChooser; // JCalendar date picker

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;

public class ReportForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ReportForm() {
        setTitle("Reports");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnDailySales = new JButton("Daily Sales");
        JButton btnLowStock = new JButton("Low Stock");
        JButton btnTopSelling = new JButton("Top Selling Products");
        topPanel.add(btnDailySales);
        topPanel.add(btnLowStock);
        topPanel.add(btnTopSelling);
        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === Daily Sales Report with Calendar Picker ===
        btnDailySales.addActionListener(e -> {
            JDateChooser dateChooser = new JDateChooser();
            int option = JOptionPane.showConfirmDialog(
                    this, dateChooser, "Select Date", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION && dateChooser.getDate() != null) {
                try {
                    java.util.Date utilDate = dateChooser.getDate();
                    Date sqlDate = new Date(utilDate.getTime()); // convert to java.sql.Date
                    Object[][] data = DailySalesReport.getData(sqlDate);
                    model.setDataVector(data, new Object[]{"Transactions", "Revenue"});
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // === Low Stock Report ===
        btnLowStock.addActionListener(e -> {
            try {
                Object[][] data = LowStockReport.getData();
                if (data.length == 0) {
                    JOptionPane.showMessageDialog(this,
                            "No products are below the reorder level.",
                            "Low Stock Report",
                            JOptionPane.INFORMATION_MESSAGE);
                    model.setRowCount(0); // clear table
                } else {
                    model.setDataVector(data,
                            new Object[]{"Product ID", "Product Name", "Stock", "Reorder Level"});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });

        // === Top Selling Products Report ===
        btnTopSelling.addActionListener(e -> {
            try {
                Object[][] data = TopSellingProductsReport.getData();
                model.setDataVector(data, new Object[]{"Product Name", "Total Sold"});
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });
    }
}
