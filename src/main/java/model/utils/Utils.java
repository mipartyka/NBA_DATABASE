package model.utils;

import model.Player;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Utils {
    public static final Map<String, Object> PARAMS = new HashMap<>();

    public static void addDescriptionRowToTable(JTable table, List<String> columnNames) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // Insert a new row at the top
        tableModel.insertRow(0, columnNames.toArray(new Object[0]));
    }

    public static void addRowNumbersToTable(JTable table, String columnName) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

//        tableModel.setValueAt(columnName, 0, 0);
        for (int i = 1; i < tableModel.getRowCount() + 1; i++) {
            tableModel.setValueAt(i, i-1, 0); // Assuming 1-based row numbers
        }
    }

    public static void displayOptionPane(String message, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(messageType);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setVisible(true);
    }

    public static List<Player> getPlayersFromResultSet(ResultSet resultSet) {
        try {
            List<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                players.add(new Player(
                        resultSet.getInt("id_player"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("team"),
                        resultSet.getTime("minutes"),
                        resultSet.getDouble("pts"),
                        resultSet.getDouble("trb"),
                        resultSet.getDouble("ast"),
                        resultSet.getDouble("stl"),
                        resultSet.getDouble("blk"),
                        resultSet.getDouble("fg_made"),
                        resultSet.getDouble("fg_attempted"),
                        resultSet.getDouble("fg_percentage"),
                        resultSet.getDouble("three_p_made"),
                        resultSet.getDouble("three_p_attempted"),
                        resultSet.getDouble("three_p_percentage"),
                        resultSet.getDouble("ft_made"),
                        resultSet.getDouble("ft_attempted"),
                        resultSet.getDouble("ft_percentage"),
                        resultSet.getDouble("off_rebounds"),
                        resultSet.getDouble("def_rebounds"),
                        resultSet.getDouble("turnovers"),
                        resultSet.getDouble("fouls"),
                        resultSet.getDouble("plus_minus")
                ));
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static TableModel fillTablePlayers(List<Player> playerList, String filter, String order) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Lastname");
        tableModel.addColumn("Team");
        tableModel.addColumn("Minutes");
        tableModel.addColumn("Points");
        tableModel.addColumn("Rebounds");
        tableModel.addColumn("Assists");
        tableModel.addColumn("Steals");
        tableModel.addColumn("Blocks");
        tableModel.addColumn("FG Made");
        tableModel.addColumn("FG Attempted");
        tableModel.addColumn("FG%");
        tableModel.addColumn("3P Made");
        tableModel.addColumn("3P Attempted");
        tableModel.addColumn("3P%");
        tableModel.addColumn("FT Made");
        tableModel.addColumn("FT Attempted");
        tableModel.addColumn("FT%");
        tableModel.addColumn("Off Rebounds");
        tableModel.addColumn("Def Rebounds");
        tableModel.addColumn("Turnovers");
        tableModel.addColumn("Fouls");
        tableModel.addColumn("Plus Minus");
        tableModel.addColumn("ID");

        switch (filter.toLowerCase()) {
            case "id":
                Collections.sort(playerList, Comparator.comparingInt(Player::getId).reversed());
                break;
            case "name":
                Collections.sort(playerList, Comparator.comparing(Player::getName, Comparator.reverseOrder()));
                break;
            case "lastname":
                Collections.sort(playerList, Comparator.comparing(Player::getLastName, Comparator.reverseOrder()));
                break;
            case "team":
                Collections.sort(playerList, Comparator.comparing(Player::getTeam, Comparator.reverseOrder()));
                break;
            case "minutes":
                Collections.sort(playerList, Comparator.comparing(Player::getMinutes, Comparator.reverseOrder()));
                break;
            case "points":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getPoints).reversed());
                break;
            case "rebounds":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getRebounds).reversed());
                break;
            case "assists":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getAssists).reversed());
                break;
            case "steals":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getSteals).reversed());
                break;
            case "blocks":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getBlocks).reversed());
                break;
            case "fg made":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFgMade).reversed());
                break;
            case "fg attempted":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFgAttempted).reversed());
                break;
            case "fg%":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFgPercentage).reversed());
                break;
            case "3p made":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getThreePMade).reversed());
                break;
            case "3p attempted":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getThreePAttempted).reversed());
                break;
            case "3p%":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getThreePPercentage).reversed());
                break;
            case "ft made":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFtMade).reversed());
                break;
            case "ft attempted":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFtAttempted).reversed());
                break;
            case "ft%":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFtPercentage).reversed());
                break;
            case "off rebounds":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getOffRebounds).reversed());
                break;
            case "def rebounds":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getDefRebounds).reversed());
                break;
            case "turnovers":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getTurnovers).reversed());
                break;
            case "fouls":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getFouls).reversed());
                break;
            case "plus minus":
                Collections.sort(playerList, Comparator.comparingDouble(Player::getPlusMinus).reversed());
                break;
            default:
                // Default sorting (e.g., by ID)
                Collections.sort(playerList, Comparator.comparingInt(Player::getId).reversed());
        }

        if(!Objects.isNull(order) && order.toLowerCase().equals("ascending"))
            Collections.reverse(playerList);

        for (Player player : playerList) {
            tableModel.addRow(new Object[]{
                    player.getId(),
                    player.getName(),
                    player.getLastName(),
                    player.getTeam(),
                    player.getMinutes(),
                    player.getPoints(),
                    player.getRebounds(),
                    player.getAssists(),
                    player.getSteals(),
                    player.getBlocks(),
                    player.getFgMade(),
                    player.getFgAttempted(),
                    player.getFgPercentage(),
                    player.getThreePMade(),
                    player.getThreePAttempted(),
                    player.getThreePPercentage(),
                    player.getFtMade(),
                    player.getFtAttempted(),
                    player.getFtPercentage(),
                    player.getOffRebounds(),
                    player.getDefRebounds(),
                    player.getTurnovers(),
                    player.getFouls(),
                    player.getPlusMinus(),
                    player.getId()
            });
        }
        return tableModel;
    }
}
