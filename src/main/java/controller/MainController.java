package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import model.UtilsDatabase;
import model.PythonScriptRunner;

public class MainController {
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
//        MainFormController.getInstance();
        for (int i = 1; i <= 30; i++) {
            pythonScriptRunner.runPythonScript("src/main/python/GenerateGameInsertsForDay.py", List.of(11,i,2023));
        }
//        try {
//            utilsDatabase.connectToDatabase();
//            utilsDatabase.resetDatabase();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            utilsDatabase.disconnectFromDatabase();
//        }
    }
}
