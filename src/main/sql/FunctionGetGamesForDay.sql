set search_path to 'nba_project';

DROP FUNCTION IF EXISTS get_games_by_date(target_date character varying);

CREATE OR REPLACE FUNCTION get_games_by_date(target_date character varying)
    RETURNS TABLE (
                      id_game VARCHAR(255),
                      home_team INT,
                      away_team INT,
                      date DATE,
                      type VARCHAR(255),
                      home_team_points BIGINT,
                      away_team_points BIGINT
                  )
AS $$
BEGIN
    RETURN QUERY
        SELECT
            g.id_game as id_game,
            g.id_home_team as home_team,
            g.id_away_team as away_team,
            g.game_date as gdate,
            g.game_type as type,
            COALESCE(SUM(tg_home.pts), 0) as home_team_points,
            COALESCE(SUM(tg_away.pts), 0) as away_team_points
        FROM
            game g
                LEFT JOIN team_game tg_home ON g.id_game = tg_home.id_game AND g.id_home_team = tg_home.id_team
                LEFT JOIN team_game tg_away ON g.id_game = tg_away.id_game AND g.id_away_team = tg_away.id_team
        WHERE
                g.game_date = target_date::DATE
        GROUP BY
            g.id_game, g.id_home_team, g.id_away_team, g.game_date, g.game_type;
END;
$$ LANGUAGE plpgsql;
