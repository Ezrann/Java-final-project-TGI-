package ui;

import dao.EmployeeDAO;
import models.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterForm extends JFrame {
    private JTextField fullNameField, usernameField, phoneField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public RegisterForm() {
        setTitle("Employee Registration");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new GridLayout(6,2,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"Cashier", "Manager", "Staff"});
        panel.add(roleCombo);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(e -> registerEmployee());

        add(panel, BorderLayout.CENTER);
        add(btnRegister, BorderLayout.SOUTH);
    }

    private void registerEmployee() {
        try {
            Employee emp = new Employee();
            emp.setFullName(fullNameField.getText());
            emp.setUsername(usernameField.getText());
            emp.setPassword(new String(passwordField.getPassword()));
            emp.setRole(roleCombo.getSelectedItem().toString());
            emp.setPhone(phoneField.getText());
            emp.setEmail(emailField.getText());

            boolean success = EmployeeDAO.register(emp);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
