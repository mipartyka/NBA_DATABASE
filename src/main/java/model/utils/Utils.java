package model.utils;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class Utils {
    public static final Map<String, Object> PARAMS = new HashMap<>();

    public static void addDescriptionRowToTable(JTable table, List<String> columnNames) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // Insert a new row at the top
        tableModel.insertRow(0, columnNames.toArray(new Object[0]));
    }

    public static void addRowNumbersToTable(JTable table, String columnName) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableModel.setValueAt(columnName, 0, 0);
        for (int i = 1; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i, i, 0); // Assuming 1-based row numbers
        }
    }

    public static void displayOptionPane(String message, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(messageType);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setVisible(true);
    }
}
