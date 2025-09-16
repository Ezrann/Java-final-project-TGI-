package ui;

import dao.EmployeeDAO;
import models.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class EmployeeDialog extends JDialog {
    private JTextField txtFullName, txtUsername, txtPhone, txtEmail, txtRole;
    private JPasswordField txtPassword;
    private boolean saved = false;
    private Employee employee;

    public EmployeeDialog(JFrame parent, Employee emp) {
        super(parent, emp == null ? "Add Employee" : "Edit Employee", true);
        this.employee = emp;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Full Name:"));
        txtFullName = new JTextField(emp != null ? emp.getFullName() : "");
        form.add(txtFullName);

        form.add(new JLabel("Username:"));
        txtUsername = new JTextField(emp != null ? emp.getUsername() : "");
        form.add(txtUsername);

        form.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);

        form.add(new JLabel("Role:"));
        txtRole = new JTextField(emp != null ? emp.getRole() : "");
        form.add(txtRole);

        form.add(new JLabel("Phone:"));
        txtPhone = new JTextField(emp != null ? emp.getPhone() : "");
        form.add(txtPhone);

        form.add(new JLabel("Email:"));
        txtEmail = new JTextField(emp != null ? emp.getEmail() : "");
        form.add(txtEmail);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> saveEmployee());
        btnCancel.addActionListener(e -> dispose());

        buttons.add(btnSave);
        buttons.add(btnCancel);
        add(buttons, BorderLayout.SOUTH);
    }

    private void saveEmployee() {
        try {
            if (txtFullName.getText().isEmpty() || txtUsername.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Full Name and Username are required!");
                return;
            }

            if (employee == null) {
                // New employee
                employee = new Employee();
            }

            employee.setFullName(txtFullName.getText());
            employee.setUsername(txtUsername.getText());
            String password = new String(txtPassword.getPassword());
            if (!password.isEmpty()) {
                employee.setPassword(password);
            }
            employee.setRole(txtRole.getText());
            employee.setPhone(txtPhone.getText());
            employee.setEmail(txtEmail.getText());

            boolean ok;
            if (employee.getEmployeeID() == 0) {
                ok = EmployeeDAO.register(employee);
            } else {
                // ✅ you can add update DAO later
                ok = EmployeeDAO.update(employee);
            }

            if (ok) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Employee saved successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save employee.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
