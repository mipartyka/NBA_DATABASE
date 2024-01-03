package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import model.Player;
import model.utils.UtilsDatabase;
import model.utils.PythonScriptRunner;

public class MainController {
    public static List<Player> playerList;
    private static MainController mainController;
    private static final PythonScriptRunner pythonScriptRunner = new PythonScriptRunner();
    private static final UtilsDatabase utilsDatabase = new UtilsDatabase();

    private MainController() throws IOException {
        control();
    }

    public static MainController getInstance() throws IOException {
        if (Objects.isNull(mainController)) {
            mainController = new MainController();
        }
        return mainController;
    }

    public void control() throws IOException {
        init();
    }


    private static void init() throws IOException {
//        for (int i = 9; i <= 31; i++) {
//            pythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsForDay.py", List.of(12,i,2023));
//        }
        try {
            utilsDatabase.connectToDatabase();
            UtilsDatabase.setSearchPath("nba_project");
            getPlayers();
//            StandingsFormController.getInstance();
            MainFormController.getInstance();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            utilsDatabase.disconnectFromDatabase();
        }));

    }
    private static void getPlayers() {
        playerList = utilsDatabase.getPlayers();
    }
}
