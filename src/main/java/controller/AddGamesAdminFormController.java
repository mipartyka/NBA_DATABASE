package controller;

import model.utils.PythonScriptRunner;
import model.utils.Utils;
import view.AddGamesAdminForm;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddGamesAdminFormController {
    private static AddGamesAdminForm addGamesAdminForm;
    private static AddGamesAdminFormController addGamesAdminFormController;

    public AddGamesAdminFormController() {
        control();
    }
    public static AddGamesAdminFormController getInstance() {
        if (Objects.isNull(addGamesAdminFormController)) {
            addGamesAdminForm = new AddGamesAdminForm();
            addGamesAdminFormController = new AddGamesAdminFormController();
            fillComboBoxes();
        }
        init();
        return addGamesAdminFormController;
    }
    public void control() {
        addGamesAdminForm.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                MainFormController.getInstance();
            }
        });
        addGamesAdminForm.getButtonBack().addActionListener(e -> onButtonBack());
        addGamesAdminForm.getButtonAddIDURL().addActionListener(e -> onButtonAddIDURL());
        addGamesAdminForm.getButtonAddDate().addActionListener(e -> onButtonAddDate());
        addGamesAdminForm.getButtonAddWeek().addActionListener(e -> onButtonAddWeek());
        addGamesAdminForm.getComboBoxMonth().addActionListener(e -> onComboBoxMonth());
        addGamesAdminForm.getComboBoxYear().addActionListener(e -> onComboBoxYear());
        addGamesAdminForm.getComboBoxDay().addActionListener(e -> onComboBoxDay());
        addGamesAdminForm.getTextFieldIDURL().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                onChangeTextFieldIDURL();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                onChangeTextFieldIDURL();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                onChangeTextFieldIDURL();
            }
        });
    }
    private static void init() {
        addGamesAdminForm.getFrame().setVisible(true);
        addGamesAdminForm.getButtonAddIDURL().setEnabled(false);
    }
    private void onChangeTextFieldIDURL() {
        addGamesAdminForm.getButtonAddIDURL().setEnabled(validateIDURL());
    }
    private void onComboBoxDay() {
        addGamesAdminForm.getButtonAddDate().setEnabled(!Objects.isNull(addGamesAdminForm.getComboBoxMonth().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxYear().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxDay().getSelectedItem()));
    }
    private void onComboBoxMonth() {
        int currentDay = 1;
        addGamesAdminForm.getButtonAddDate().setEnabled(!Objects.isNull(addGamesAdminForm.getComboBoxMonth().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxYear().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxDay().getSelectedItem()));
        if(addGamesAdminForm.getComboBoxDay().getSelectedItem() != null){
            currentDay = (int) addGamesAdminForm.getComboBoxDay().getSelectedItem();
        }
        addGamesAdminForm.getComboBoxDay().removeAllItems();
        int daysInMonth = LocalDate.of((int) addGamesAdminForm.getComboBoxYear().getSelectedItem(), (Month) addGamesAdminForm.getComboBoxMonth().getSelectedItem(), 1).lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            addGamesAdminForm.getComboBoxDay().addItem(day);
        }
        if (currentDay <= daysInMonth) {
            addGamesAdminForm.getComboBoxDay().setSelectedItem(currentDay);
        } else {
            addGamesAdminForm.getComboBoxDay().setSelectedItem(daysInMonth);
        }
    }
    private void onComboBoxYear() {
        addGamesAdminForm.getButtonAddDate().setEnabled(!Objects.isNull(addGamesAdminForm.getComboBoxMonth().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxYear().getSelectedItem()) && !Objects.isNull(addGamesAdminForm.getComboBoxDay().getSelectedItem()));
    }
    private void onButtonAddWeek() {
        String[] days = addGamesAdminForm.getComboBoxWeek().getSelectedItem().toString().split(" - ");
        LocalDate start = LocalDate.parse(days[0]);
        LocalDate end = LocalDate.parse(days[1]);
        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            try {
                PythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsForDay.py", List.of( String.valueOf(date.getMonthValue()), String.valueOf(date.getDayOfMonth()), String.valueOf(date.getYear())));
            } catch (Exception e) {
                Utils.displayOptionPane("Game already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void onButtonAddDate() {
        try {
            PythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsForDay.py", List.of( Month.valueOf(addGamesAdminForm.getComboBoxMonth().getSelectedItem().toString()).getValue(), addGamesAdminForm.getComboBoxDay().getSelectedItem().toString(), addGamesAdminForm.getComboBoxYear().getSelectedItem().toString()));
            Utils.displayOptionPane("Games added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            Utils.displayOptionPane("Game already exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onButtonAddIDURL() {
        String URL = "";
        if(validateIDURL()) {
            if (addGamesAdminForm.getTextFieldIDURL().getText().length() < 12) {
                URL = "https://www.basketball-reference.com/boxscores/" + addGamesAdminForm.getTextFieldIDURL().getText() + ".html";
            } else
                URL = addGamesAdminForm.getTextFieldIDURL().getText();
            try {
                PythonScriptRunner.runPythonScript("src/main/python/GenerateGameInserts.py", List.of(URL));
                Utils.displayOptionPane("Game added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                Utils.displayOptionPane("Game already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateIDURL() {
        if(addGamesAdminForm.getTextFieldIDURL().getText().length() < 11 || (!addGamesAdminForm.getTextFieldIDURL().getText().matches("^\\d{9}[A-Z]{3}$") && !addGamesAdminForm.getTextFieldIDURL().getText().matches("^https://www.basketball-reference.com/boxscores/\\d{9}[A-Z]{3}.html$"))) {
            return false;
        }
        return true;
    }

    private void onButtonBack() {
        if (Utils.PARAMS.containsKey("CURRENT_USER"))
            MainFormAdminController.getInstance();
        else
            MainFormController.getInstance();
        addGamesAdminForm.getFrame().dispose();
    }
    private static void fillComboBoxes() {
        fillYearComboBox();
        fillMonthComboBox();
        fillDayComboBox();
        fillWeekComboBox();
    }
    private static void fillWeekComboBox() {
        LocalDateTime firstDay = LocalDate.of(2023, 10, 23).atStartOfDay();
        for (int i = 0; i < 30; i++) {
            addGamesAdminForm.getComboBoxWeek().addItem(firstDay.plusDays(i * 7).toLocalDate().toString() + " - " + firstDay.plusDays((i + 1) * 7 - 1).toLocalDate().toString());
        }
    }
    private static void fillYearComboBox() {
        int currentYear = LocalDate.now().getYear();
        addGamesAdminForm.getComboBoxYear().addItem(currentYear);
        addGamesAdminForm.getComboBoxYear().addItem(currentYear - 1);
    }

    private static void fillMonthComboBox() {
        for (Month month : Month.values()) {
            addGamesAdminForm.getComboBoxMonth().addItem(month);
        }
    }

    private static void fillDayComboBox() {
        int daysInMonth = LocalDate.now().lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            addGamesAdminForm.getComboBoxDay().addItem(day);
        }
    }
}

