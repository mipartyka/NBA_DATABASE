set search_path to 'nba_project';

DROP FUNCTION IF EXISTS get_games_by_date(target_date character varying);

CREATE OR REPLACE FUNCTION get_games_by_date(target_date character varying)
    RETURNS TABLE (
                      id_game VARCHAR(255),
                      home_team VARCHAR(255),
                      away_team VARCHAR(255),
                      date DATE,
                      type VARCHAR(255),
                      home_team_points BIGINT,
                      away_team_points BIGINT,
                      id_home_team INT,
                      id_away_team INT
                  )
AS $$
BEGIN
    RETURN QUERY
        SELECT
            g.id_game as id_game,
            home.name as home_team,
            away.name as away_team,
            g.game_date as gdate,
            g.game_type as type,
            COALESCE(SUM(tg_home.pts), 0) as home_team_points,
            COALESCE(SUM(tg_away.pts), 0) as away_team_points,
            g.id_home_team as id_home_team,
            g.id_away_team as id_away_team
        FROM
            game g
                LEFT JOIN team_game tg_home ON g.id_game = tg_home.id_game AND g.id_home_team = tg_home.id_team
                LEFT JOIN team_game tg_away ON g.id_game = tg_away.id_game AND g.id_away_team = tg_away.id_team
                LEFT JOIN team home ON g.id_home_team = home.id_team
                LEFT JOIN team away ON g.id_away_team = away.id_team
        WHERE
                g.game_date = target_date::DATE
        GROUP BY
            g.id_game, home.name, away.name, g.game_date, g.game_type, g.id_home_team, g.id_away_team;
END;
$$ LANGUAGE plpgsql;

