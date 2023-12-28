package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class UtilsDatabase {
    private static final String DB_NAME = "cetphhnl";
    private static final String DB_USER = "cetphhnl";
    private static final String DB_PASSWORD = "aAGfNp5piy3_W3aKzF05IuesJLQg44Li";
    private static final String DB_HOST = "dumbo.db.elephantsql.com";
    private static final String DB_PORT = "5432";
    private static final String JDBC_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static Connection connection;

    public void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");

        connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        System.out.println("Connected to the database.");
    }

    public void disconnectFromDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet runSqlFunction(String functionName, List<Object> args) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM " + functionName + "(");
        for (int i = 0; i < args.size(); i++) {
            query.append("?");
            if (i != args.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        for (int i = 0; i < args.size(); i++) {
            preparedStatement.setObject(i + 1, args.get(i));
        }

        return preparedStatement.executeQuery();

    }


    public static void setSearchPath(String searchPath) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Execute the SET search_path query
            statement.execute("SET search_path TO " + searchPath);
        }
    }

    public void deleteAllDataFromTable(String tableName) {
        String updateQuery = "DELETE FROM " + tableName;

        try (
                var preparedStatement = connection.prepareStatement(updateQuery)
        ) {
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " rows updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNULLToAllRecordsFromTable(String tableName, List<Object> primaryKeyList) {
        try {
            // Get metadata to retrieve column names
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);

            // Build the update query dynamically
            StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");

            // Iterate through columns and append column names
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                if (!primaryKeyList.contains(columnName))
                    updateQuery.append(columnName).append(" = NULL, ");
            }

            // Remove the trailing comma and space
            updateQuery.setLength(updateQuery.length() - 2);

            // Execute the update query
            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery.toString())
            ) {
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " rows updated.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        try {
            setSearchPath("nba_project");
            deleteAllDataFromTable("player_game");
            deleteAllDataFromTable("team_game");
            deleteAllDataFromTable("game");
            setNULLToAllRecordsFromTable("team_stats", List.of("id_team_stats"));
            setNULLToAllRecordsFromTable("player_stats", List.of("id_player_stats"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void fillTableFromResultSet(JTable table, ResultSet resultSet) {
        try {
            // Get column names
            int columnCount = resultSet.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = resultSet.getMetaData().getColumnName(i);
            }

            // Create a DefaultTableModel with column names
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            // Populate the table model with data
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Set the table model for the JTable
            table.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet if necessary
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void populateJListFromResultSet(JList<String> jList, ResultSet resultSet) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        try {
            // Populate the list model with data
            while (resultSet.next()) {
                // Assuming the result set has a single column of type String
                String data = resultSet.getString(1);
                listModel.addElement(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet if necessary
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Set the list model for the given JList
        jList.setModel(listModel);
    }

}
