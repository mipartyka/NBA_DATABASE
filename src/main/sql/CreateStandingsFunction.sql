set search_path to 'nba_project';

DROP FUNCTION IF EXISTS get_league_standings(character varying);

CREATE OR REPLACE FUNCTION get_league_standings(conference_filter character varying)
    RETURNS TABLE (
                      pos BIGINT,
                      team_name VARCHAR(255),
                      wins BIGINT,
                      losses BIGINT,
                      points_for BIGINT,
                      points_against BIGINT
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT
                    ROW_NUMBER() OVER (ORDER BY wins DESC, points_for DESC) AS pos,
                    t.name AS team_name,
                    COUNT(DISTINCT CASE WHEN t.id_team = tg.id_team AND tg.pts > tg.opponent_pts THEN tg.id_game END)::BIGINT AS wins,
                    COUNT(DISTINCT CASE WHEN t.id_team = tg.id_team AND tg.pts < tg.opponent_pts THEN tg.id_game END)::BIGINT AS losses,
                    SUM(CASE WHEN t.id_team = tg.id_team THEN tg.pts ELSE 0 END)::BIGINT AS points_for,
                    SUM(CASE WHEN t.id_team = tg.id_team THEN tg.opponent_pts ELSE 0 END)::BIGINT AS points_against
        FROM
            team t
                LEFT JOIN
            (
                SELECT
                    tg.id_team,
                    tg.id_game,
                    tg.pts,
                    COALESCE(og.pts, 0) AS opponent_pts
                FROM
                    team_game tg
                        LEFT JOIN
                    game g ON tg.id_game = g.id_game
                        LEFT JOIN
                    team_game og ON g.id_game = og.id_game AND tg.id_team <> og.id_team
            ) tg ON t.id_team = tg.id_team
        WHERE
            (conference_filter = 'league') OR
            (conference_filter = 'eastern' AND t.conference = 'Eastern') OR
            (conference_filter = 'western' AND t.conference = 'Western') OR
            (conference_filter = lower(t.division))
        GROUP BY
            t.id_team, t.name
        ORDER BY
            wins DESC, points_for DESC;  -- You can customize the order as needed
END;
$$ LANGUAGE plpgsql;
