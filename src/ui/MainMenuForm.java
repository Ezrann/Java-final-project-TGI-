package ui;

import models.Employee;
import javax.swing.*;
import java.awt.*;

public class MainMenuForm extends JFrame {
    private Employee currentUser;

    public MainMenuForm(Employee user) {
        this.currentUser = user;
        setTitle("Supermarket - Main Menu (" + user.getRole() + ")");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new FlowLayout());

        JButton btnProducts = new JButton("Products");
        JButton btnPOS = new JButton("POS");
        JButton btnEmployees = new JButton("Employees");
        JButton btnSuppliers = new JButton("Suppliers");
        JButton btnReports = new JButton("Reports");

        // ✅ Open actual forms instead of showing placeholders
        btnProducts.addActionListener(e -> new ProductForm(currentUser).setVisible(true));
        btnPOS.addActionListener(e -> new POSForm(currentUser.getEmployeeID()).setVisible(true));
        btnEmployees.addActionListener(e -> new EmployeeForm(currentUser).setVisible(true));
        btnSuppliers.addActionListener(e -> new SupplierForm().setVisible(true));
        btnReports.addActionListener(e -> new ReportForm().setVisible(true));

        p.add(btnProducts);
        p.add(btnPOS);
        p.add(btnEmployees);
        p.add(btnSuppliers);
        p.add(btnReports);

        add(p, BorderLayout.CENTER);
    }
}
