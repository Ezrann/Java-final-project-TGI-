package ui;

import dao.EmployeeDAO;
import models.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Supermarket Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10));

        // 🔹 Title
        JLabel lblTitle = new JLabel("Supermarket Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        add(lblTitle, BorderLayout.NORTH);

        // 🔹 Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(lblUser, gbc);

        usernameField = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        formPanel.add(usernameField, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(lblPass, gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 🔹 Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        btnLogin.setPreferredSize(new Dimension(100,30));
        btnRegister.setPreferredSize(new Dimension(100,30));

        // Login action
        btnLogin.addActionListener(e -> doLogin());

        // Register action
        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
        });

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void doLogin() {
        try {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Employee emp = EmployeeDAO.authenticate(u, p);
            if (emp != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + emp.getFullName(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainMenuForm(emp).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
