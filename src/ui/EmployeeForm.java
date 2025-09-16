package ui;

import dao.EmployeeDAO;
import models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmployeeForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Employee currentUser; // logged-in employee

    public EmployeeForm(Employee loggedInEmp) {
        this.currentUser = loggedInEmp;
        setTitle("Employee Management - Logged in as: " + loggedInEmp.getRole());
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(
                new Object[]{"ID", "Full Name", "Username", "Role", "Phone", "Email"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        // 🔹 Everyone can refresh
        refreshBtn.addActionListener(e -> loadEmployees());

        // 🔹 Add employee (Manager only)
        addBtn.addActionListener(e -> {
            if (!isManager()) return;
            JTextField fullNameField = new JTextField();
            JTextField usernameField = new JTextField();
            JTextField passwordField = new JTextField();
            JTextField roleField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();

            Object[] inputs = {
                    "Full Name:", fullNameField,
                    "Username:", usernameField,
                    "Password:", passwordField,
                    "Role:", roleField,
                    "Phone:", phoneField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(this, inputs, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Employee emp = new Employee();
                emp.setFullName(fullNameField.getText());
                emp.setUsername(usernameField.getText());
                emp.setPassword(passwordField.getText());
                emp.setRole(roleField.getText());
                emp.setPhone(phoneField.getText());
                emp.setEmail(emailField.getText());

                try {
                    if (EmployeeDAO.register(emp)) {
                        JOptionPane.showMessageDialog(this, "Employee added successfully.");
                        loadEmployees();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add employee.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // 🔹 Update employee (Manager only)
        updateBtn.addActionListener(e -> {
            if (!isManager()) return;
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee to update.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String currentName = (String) model.getValueAt(row, 1);
            String currentUsername = (String) model.getValueAt(row, 2);
            String currentRole = (String) model.getValueAt(row, 3);
            String currentPhone = (String) model.getValueAt(row, 4);
            String currentEmail = (String) model.getValueAt(row, 5);

            JTextField fullNameField = new JTextField(currentName);
            JTextField usernameField = new JTextField(currentUsername);
            JTextField passwordField = new JTextField(); // blank = unchanged
            JTextField roleField = new JTextField(currentRole);
            JTextField phoneField = new JTextField(currentPhone);
            JTextField emailField = new JTextField(currentEmail);

            Object[] inputs = {
                    "Full Name:", fullNameField,
                    "Username:", usernameField,
                    "Password (leave blank if unchanged):", passwordField,
                    "Role:", roleField,
                    "Phone:", phoneField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(this, inputs, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Employee emp = new Employee();
                emp.setEmployeeID(id);
                emp.setFullName(fullNameField.getText());
                emp.setUsername(usernameField.getText());
                emp.setPassword(passwordField.getText().isEmpty() ? null : passwordField.getText());
                emp.setRole(roleField.getText());
                emp.setPhone(phoneField.getText());
                emp.setEmail(emailField.getText());

                try {
                    if (EmployeeDAO.update(emp)) {
                        JOptionPane.showMessageDialog(this, "Employee updated successfully.");
                        loadEmployees();
                    } else {
                        JOptionPane.showMessageDialog(this, "Update failed.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // 🔹 Delete employee (Manager only)
        deleteBtn.addActionListener(e -> {
            if (!isManager()) return;
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an employee to delete.");
                return;
            }

            int id = (int) model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete employee: " + name + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (EmployeeDAO.delete(id)) {
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                        loadEmployees();
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage());
                }
            }
        });

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        loadEmployees();
    }

    private void loadEmployees() {
        try {
            model.setRowCount(0);
            List<Employee> list = EmployeeDAO.getAll();
            for (Employee emp : list) {
                model.addRow(new Object[]{
                        emp.getEmployeeID(),
                        emp.getFullName(),
                        emp.getUsername(),
                        emp.getRole(),
                        emp.getPhone(),
                        emp.getEmail()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage());
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
