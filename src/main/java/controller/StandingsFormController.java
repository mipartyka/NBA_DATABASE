package controller;

import model.utils.Utils;
import model.utils.UtilsDatabase;
import view.StandingsForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class StandingsFormController {
    private static StandingsForm standingsForm;
    private static StandingsFormController standingsFormController;


    public StandingsFormController() {
        control();
    }

    public static StandingsFormController getInstance() {
        if (Objects.isNull(standingsFormController)) {
            standingsForm = new StandingsForm();
            standingsFormController = new StandingsFormController();
        }
        init();
        return standingsFormController;
    }

    public void control() {
        standingsForm.getComboBoxStandings().addActionListener(e -> onComboBoxStandings());
        standingsForm.getComboBoxDetailed().addActionListener(e -> onComboBoxDetailed());
        standingsForm.getButtonBack().addActionListener(e -> onButtonBack());
    }

    private void onComboBoxDetailed() {
        if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Eastern")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("eastern")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Western")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("western")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Atlantic")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("atlantic")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Central")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("central")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Southeast")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("southeast")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Northwest")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("northwest")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Pacific")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("pacific")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxDetailed().getSelectedItem().equals("Southwest")) {
            try {
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("southwest")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
//        Utils.addDescriptionRowToTable(standingsForm.getTableStandings(), List.of("", "Team", "Wins", "Losses", "Win Percentage", "Points For", "Points Against"));
        Utils.addRowNumbersToTable(standingsForm.getTableStandings(), "#");
    }

    private void onComboBoxStandings() {
        if(standingsForm.getComboBoxStandings().getSelectedItem().equals("League")){
            standingsForm.getComboBoxDetailed().setEnabled(false);
            try{
                UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("league")));
//                Utils.addDescriptionRowToTable(standingsForm.getTableStandings(), List.of("", "Team", "Wins", "Losses", "Win Percentage", "Points For", "Points Against"));
                Utils.addRowNumbersToTable(standingsForm.getTableStandings(), "#");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (standingsForm.getComboBoxStandings().getSelectedItem().equals("Conference")) {
            standingsForm.getComboBoxDetailed().setEnabled(true);
            standingsForm.getComboBoxDetailed().removeAllItems();
            standingsForm.getComboBoxDetailed().addItem("Eastern");
            standingsForm.getComboBoxDetailed().addItem("Western");
        } else if (standingsForm.getComboBoxStandings().getSelectedItem().equals("Division")) {
            standingsForm.getComboBoxDetailed().setEnabled(true);
            standingsForm.getComboBoxDetailed().removeAllItems();
            standingsForm.getComboBoxDetailed().addItem("Atlantic");
            standingsForm.getComboBoxDetailed().addItem("Central");
            standingsForm.getComboBoxDetailed().addItem("Southeast");
            standingsForm.getComboBoxDetailed().addItem("Northwest");
            standingsForm.getComboBoxDetailed().addItem("Pacific");
            standingsForm.getComboBoxDetailed().addItem("Southwest");
        }
    }

    private void onButtonBack() {
        MainFormController.getInstance();
        standingsForm.getFrame().dispose();
    }

    private static void init() {
        standingsForm.getFrame().setVisible(true);
        initiallyFillTable();
        fillComboBox();
    }

    private static void initiallyFillTable() {
        try {
            UtilsDatabase.fillTableFromResultSet(standingsForm.getTableStandings(), UtilsDatabase.runSqlFunction("get_league_standings", List.of("league")));
//            Utils.addDescriptionRowToTable(standingsForm.getTableStandings(), List.of("", "Team", "Wins", "Losses", "Win Percentage", "Points For", "Points Against"));
            Utils.addRowNumbersToTable(standingsForm.getTableStandings(), "#");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void fillComboBox(){
        standingsForm.getComboBoxStandings().addItem("League");
        standingsForm.getComboBoxStandings().addItem("Conference");
        standingsForm.getComboBoxStandings().addItem("Division");
    }
}
