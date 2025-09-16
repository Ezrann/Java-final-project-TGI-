package ui;

import dao.SupplierDAO;
import models.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SupplierForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public SupplierForm() {
        setTitle("Supplier Management");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Contact", "Phone", "Email", "Address"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh"); // ✅ New button

        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnRefresh); // ✅ add refresh button
        add(buttons, BorderLayout.SOUTH);

        loadSuppliers();

        // Add Supplier
        btnAdd.addActionListener(e -> {
            Supplier s = showSupplierDialog(null);
            if (s != null) {
                try {
                    SupplierDAO.insert(s);
                    loadSuppliers();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        // Edit Supplier
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Supplier s = new Supplier();
                s.setSupplierID((int) model.getValueAt(row, 0));
                s.setSupplierName((String) model.getValueAt(row, 1));
                s.setContactName((String) model.getValueAt(row, 2));
                s.setPhone((String) model.getValueAt(row, 3));
                s.setEmail((String) model.getValueAt(row, 4));
                s.setAddress((String) model.getValueAt(row, 5));

                Supplier updated = showSupplierDialog(s);
                if (updated != null) {
                    try {
                        SupplierDAO.update(updated);
                        loadSuppliers();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                }
            }
        });

        // Delete Supplier
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                try {
                    SupplierDAO.delete(id);
                    loadSuppliers();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        // ✅ Refresh Supplier list
        btnRefresh.addActionListener(e -> loadSuppliers());
    }

    private void loadSuppliers() {
        try {
            model.setRowCount(0);
            List<Supplier> list = SupplierDAO.getAll();
            for (Supplier s : list) {
                model.addRow(new Object[]{
                        s.getSupplierID(),
                        s.getSupplierName(),
                        s.getContactName(),
                        s.getPhone(),
                        s.getEmail(),
                        s.getAddress()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private Supplier showSupplierDialog(Supplier s) {
        JTextField tfName = new JTextField(s != null ? s.getSupplierName() : "");
        JTextField tfContact = new JTextField(s != null ? s.getContactName() : "");
        JTextField tfPhone = new JTextField(s != null ? s.getPhone() : "");
        JTextField tfEmail = new JTextField(s != null ? s.getEmail() : "");
        JTextField tfAddress = new JTextField(s != null ? s.getAddress() : "");

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:")); panel.add(tfName);
        panel.add(new JLabel("Contact:")); panel.add(tfContact);
        panel.add(new JLabel("Phone:")); panel.add(tfPhone);
        panel.add(new JLabel("Email:")); panel.add(tfEmail);
        panel.add(new JLabel("Address:")); panel.add(tfAddress);

        int result = JOptionPane.showConfirmDialog(this, panel, "Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Supplier sup = (s != null ? s : new Supplier());
            sup.setSupplierName(tfName.getText());
            sup.setContactName(tfContact.getText());
            sup.setPhone(tfPhone.getText());
            sup.setEmail(tfEmail.getText());
            sup.setAddress(tfAddress.getText());
            if (s != null) sup.setSupplierID(s.getSupplierID()); // preserve ID
            return sup;
        }
        return null;
    }
}
