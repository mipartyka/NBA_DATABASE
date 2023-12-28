package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Game {
    private String id;
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;
    private String date;
    private String type;
    private Integer idHomeTeam;
    private Integer idAwayTeam;

    @Override
    public String toString() {
        return  awayTeam + "  " + awayScore + "  @  "
                 + homeScore + "  " + homeTeam;
    }

    public static List<Object> resultSetToGameList(ResultSet resultSet) {
        List<Object> gameList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                // Retrieve attributes from ResultSet
                String gameId = resultSet.getString("id_game");
                String homeTeam = resultSet.getString("home_team");
                String awayTeam = resultSet.getString("away_team");
                String homeScore = resultSet.getString("home_team_points");
                String awayScore = resultSet.getString("away_team_points");
                String date = resultSet.getString("date");
                String type = resultSet.getString("type");
                Integer idHomeTeam = resultSet.getInt("id_home_team");
                Integer idAwayTeam = resultSet.getInt("id_away_team");

                // Add other attributes as needed

                // Create Game object and add to list
                Game game = new Game(gameId, homeTeam, awayTeam, homeScore, awayScore, date, type, idHomeTeam, idAwayTeam);
                gameList.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return gameList;
    }

}
