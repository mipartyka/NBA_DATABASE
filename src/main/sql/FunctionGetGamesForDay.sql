set search_path to 'nba_project';

DROP FUNCTION IF EXISTS get_games_by_date(target_date DATE);

CREATE OR REPLACE FUNCTION get_games_by_date(target_date character varying)
    RETURNS TABLE (
                      id_game VARCHAR(255),
                      home_team INT,
                      away_team INT,
                      date DATE,
                      type VARCHAR(255)
                  )
AS $$
BEGIN
    RETURN QUERY
        SELECT
            g.id_game as id_game,
            g.id_home_team as home_team,
            g.id_away_team as away_team,
            g.game_date as gdate,
            g.game_type as type
        FROM
            game g
        WHERE
                g.game_date = target_date::DATE;
END;
$$ LANGUAGE plpgsql;
