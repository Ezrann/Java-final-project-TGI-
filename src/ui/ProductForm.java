package ui;

import dao.ProductDAO;
import models.Employee;
import models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProductForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField, categoryField, priceField, stockField, reorderField, supplierField;

    private Employee currentUser; // logged-in user

    public ProductForm(Employee loggedInEmp) {
        this.currentUser = loggedInEmp;

        setTitle("Product Management - Logged in as: " + loggedInEmp.getRole());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadProducts();
    }

    private void initUI() {
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price", "Stock", "Reorder", "Supplier"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        reorderField = new JTextField();
        supplierField = new JTextField();

        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(new JLabel("Price"));
        inputPanel.add(new JLabel("Stock"));
        inputPanel.add(new JLabel("Reorder"));
        inputPanel.add(new JLabel("Supplier ID"));

        inputPanel.add(nameField);
        inputPanel.add(categoryField);
        inputPanel.add(priceField);
        inputPanel.add(stockField);
        inputPanel.add(reorderField);
        inputPanel.add(supplierField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // 🔹 Button actions
        addBtn.addActionListener(e -> {
            if (!isManager()) return;
            addProduct();
        });

        updateBtn.addActionListener(e -> {
            if (!isManager()) return;
            updateProduct();
        });

        deleteBtn.addActionListener(e -> {
            if (!isManager()) return;
            deleteProduct();
        });

        refreshBtn.addActionListener(e -> loadProducts());

        // Table click -> load values into text fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                nameField.setText(model.getValueAt(row, 1).toString());
                categoryField.setText(model.getValueAt(row, 2).toString());
                priceField.setText(model.getValueAt(row, 3).toString());
                stockField.setText(model.getValueAt(row, 4).toString());
                reorderField.setText(model.getValueAt(row, 5).toString());
                supplierField.setText(model.getValueAt(row, 6).toString());
            }
        });
    }

    private void loadProducts() {
        try {
            model.setRowCount(0);
            List<Product> products = ProductDAO.getAll();
            for (Product p : products) {
                model.addRow(new Object[]{
                        p.getProductID(), p.getName(), p.getCategory(),
                        p.getPrice(), p.getStockQuantity(),
                        p.getReorderLevel(), p.getSupplierID()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            Product p = new Product();
            p.setName(nameField.getText());
            p.setCategory(categoryField.getText());
            p.setPrice(Double.parseDouble(priceField.getText()));
            p.setStockQuantity(Integer.parseInt(stockField.getText()));
            p.setReorderLevel(Integer.parseInt(reorderField.getText()));
            p.setSupplierID(Integer.parseInt(supplierField.getText()));

            String sql = "INSERT INTO Products (ProductName, Category, Price, StockQuantity, ReorderLevel, SupplierID) VALUES (?,?,?,?,?,?)";
            var conn = config.DBConnection.getConnection();
            var ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStockQuantity());
            ps.setInt(5, p.getReorderLevel());
            ps.setInt(6, p.getSupplierID());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product added!");
            loadProducts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage());
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to update.");
            return;
        }

        try {
            Product p = new Product();
            p.setProductID(Integer.parseInt(model.getValueAt(row, 0).toString()));
            p.setName(nameField.getText());
            p.setCategory(categoryField.getText());
            p.setPrice(Double.parseDouble(priceField.getText()));
            p.setStockQuantity(Integer.parseInt(stockField.getText()));
            p.setReorderLevel(Integer.parseInt(reorderField.getText()));
            p.setSupplierID(Integer.parseInt(supplierField.getText()));

            ProductDAO.update(p);
            JOptionPane.showMessageDialog(this, "Product updated!");
            loadProducts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int productId = Integer.parseInt(model.getValueAt(row, 0).toString());
            ProductDAO.delete(productId);
            JOptionPane.showMessageDialog(this, "Product deleted!");
            loadProducts();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage());
        }
    }

    // 🔹 Validation helper
    private boolean isManager() {
        if (!"Manager".equalsIgnoreCase(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "Only Managers can perform this action.");
            return false;
        }
        return true;
    }
}
