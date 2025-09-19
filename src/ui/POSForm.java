package ui;

import models.Product;
import models.Sale;
import models.SaleItem;
import dao.ProductDAO;
import dao.SaleDAO;
import dao.SaleItemDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class POSForm extends JFrame {
    private JTable productTable;
    private DefaultTableModel productModel;

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private List<SaleItem> currentItems = new ArrayList<>();
    private JComboBox<String> categoryBox; // 🔹 Category dropdown

    public POSForm(int employeeId) {
        setTitle("Point of Sale");
        setSize(850, 600);
        setLocationRelativeTo(null);
        init(employeeId);
    }

    private void init(int employeeId) {
        setLayout(new BorderLayout());

        // 🔹 Top panel with category selector
        JPanel topPanel = new JPanel(new BorderLayout());

        categoryBox = new JComboBox<>(new String[]{
                "All", "Beverages", "Bakery", "Dairy", "Snacks", "Personal Care"
        });
        topPanel.add(new JLabel("Select Category:"), BorderLayout.WEST);
        topPanel.add(categoryBox, BorderLayout.CENTER);

        JButton filterBtn = new JButton("Filter");
        topPanel.add(filterBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Product table
        productModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Category", "Price", "Stock"}, 0
        );
        productTable = new JTable(productModel);
        JScrollPane productScroll = new JScrollPane(productTable);
        productScroll.setPreferredSize(new Dimension(850, 200));

        JButton addBtn = new JButton("Add Selected Product");

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.add(productScroll, BorderLayout.CENTER);
        productPanel.add(addBtn, BorderLayout.SOUTH);

        add(productPanel, BorderLayout.CENTER);

        // 🔹 Cart table instead of JList
        cartModel = new DefaultTableModel(
                new Object[]{"Product", "Quantity", "Price", "Subtotal"}, 0
        );
        cartTable = new JTable(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setPreferredSize(new Dimension(850, 150));

        JButton checkout = new JButton("Checkout");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(cartScroll, BorderLayout.CENTER);
        bottomPanel.add(checkout, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🔹 Load all products at start
        loadProducts("All");

        // 🔹 Filter by category
        filterBtn.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            loadProducts(category);
        });

        // 🔹 Add product from selection
        addBtn.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                try {
                    int pid = (int) productModel.getValueAt(row, 0);
                    Product p = ProductDAO.findById(pid);
                    if (p == null) {
                        JOptionPane.showMessageDialog(this, "Product not found");
                        return;
                    }

                    if (p.getStockQuantity() <= 0) {
                        JOptionPane.showMessageDialog(this, "Out of stock!");
                        return;
                    }

                    // check if already in cart
                    boolean found = false;
                    for (int i = 0; i < currentItems.size(); i++) {
                        SaleItem item = currentItems.get(i);
                        if (item.getProductID() == pid) {
                            item.setQuantity(item.getQuantity() + 1);
                            cartModel.setValueAt(item.getQuantity(), i, 1);
                            cartModel.setValueAt(item.getQuantity() * item.getPrice(), i, 3);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        SaleItem item = new SaleItem();
                        item.setProductID(pid);
                        item.setQuantity(1);
                        item.setPrice(p.getPrice());
                        currentItems.add(item);

                        cartModel.addRow(new Object[]{
                                p.getName(),
                                1,
                                p.getPrice(),
                                p.getPrice()
                        });
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product first.");
            }
        });

        // 🔹 Checkout
        checkout.addActionListener(e -> {
            if (currentItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items in cart");
                return;
            }
            try {
                Sale sale = new Sale();
                sale.setSaleDate(new Date());
                sale.setEmployeeID(employeeId);
                double total = currentItems.stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity())
                        .sum();
                sale.setTotalAmount(total);
                sale.setPaymentMethod("Cash");

                int saleId = SaleDAO.createSale(sale);

                for (SaleItem it : currentItems) {
                    it.setSaleID(saleId);
                    SaleItemDAO.createSaleItem(it);

                    // update stock
                    Product p = ProductDAO.findById(it.getProductID());
                    if (p != null) {
                        int newStock = p.getStockQuantity() - it.getQuantity();
                        if (newStock < 0) newStock = 0;
                        ProductDAO.updateStock(it.getProductID(), newStock);
                    }
                }

                JOptionPane.showMessageDialog(this, "Sale completed. ID: " + saleId);

                // clear cart and reload products
                cartModel.setRowCount(0);
                currentItems.clear();
                loadProducts((String) categoryBox.getSelectedItem());

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
            }
        });
    }

    // 🔹 Load products by category
    private void loadProducts(String category) {
        try {
            productModel.setRowCount(0);
            List<Product> products = ProductDAO.getAll();
            for (Product p : products) {
                if (category.equals("All") || p.getCategory().equalsIgnoreCase(category)) {
                    productModel.addRow(new Object[]{
                            p.getProductID(),
                            p.getName(),
                            p.getCategory(),
                            p.getPrice(),
                            p.getStockQuantity()
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }
}
