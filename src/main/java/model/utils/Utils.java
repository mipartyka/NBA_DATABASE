package model.utils;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final Map<String, Object> PARAMS = new HashMap<>();

    public static void displayOptionPane(String message, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(messageType);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setVisible(true);
    }
}
