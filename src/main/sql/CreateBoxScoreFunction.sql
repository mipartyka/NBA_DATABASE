DROP FUNCTION IF EXISTS getBoxScore(character varying);

CREATE OR REPLACE FUNCTION getBoxScore(p_id_game VARCHAR(255))
    RETURNS TABLE (
                      player_id INT,
                      name character varying,
                      surname character varying,
                      pts INT,
                      trb INT,
                      ast INT,
                      stl INT,
                      blk INT,  -- Added blk for player
                      fg INT,
                      fga INT,
                      fg_pct DOUBLE PRECISION,
                      fg3 INT,
                      fg3a INT,
                      fg3_pct DOUBLE PRECISION,
                      ft INT,
                      fta INT,
                      ft_pct DOUBLE PRECISION,
                      orb INT,
                      drb INT,
                      tov INT,
                      plus_minus INT,  -- Added plus_minus for player
                      team_id INT,
                      team_name VARCHAR(255),
                      team_pts INT,
                      team_trb INT,
                      team_ast INT,
                      team_stl INT,
                      team_blk INT,  -- Added blk for team
                      team_fg INT,
                      team_fga INT,
                      team_fg_pct DOUBLE PRECISION,
                      team_fg3 INT,
                      team_fg3a INT,
                      team_fg3_pct DOUBLE PRECISION,
                      team_ft INT,
                      team_fta INT,
                      team_ft_pct DOUBLE PRECISION,
                      team_orb INT,
                      team_drb INT,
                      team_tov INT
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            pg.id_player,
            p.name ,
            p.surname,
            pg.pts,
            pg.trb,
            pg.ast,
            pg.stl,
            pg.blk,  -- Added blk for player
            pg.fg,
            pg.fga,
            pg.fg_pct,
            pg.fg3,
            pg.fg3a,
            pg.fg3_pct,
            pg.ft,
            pg.fta,
            pg.ft_pct,
            pg.orb,
            pg.drb,
            pg.tov AS tov,
            pg.plus_minus,  -- Added plus_minus for player
            tg.id_team,
            t.name AS team_name,
            tg.pts AS team_pts,
            tg.trb AS team_trb,
            tg.ast AS team_ast,
            tg.stl AS team_stl,
            tg.blk AS team_blk,  -- Added blk for team
            tg.fg AS team_fg,
            tg.fga AS team_fga,
            tg.fg_pct AS team_fg_pct,
            tg.fg3 AS team_fg3,
            tg.fg3a AS team_fg3a,
            tg.fg3_pct AS team_fg3_pct,
            tg.ft AS team_ft,
            tg.fta AS team_fta,
            tg.ft_pct AS team_ft_pct,
            tg.orb AS team_orb,
            tg.drb AS team_drb,
            tg.tov AS team_tov
        FROM
            player_game pg
                INNER JOIN player p ON pg.id_player = p.id_player
                INNER JOIN team_game tg ON pg.id_game = tg.id_game AND pg.id_team = tg.id_team
                INNER JOIN team t ON tg.id_team = t.id_team
        WHERE
                pg.id_game = p_id_game
        ORDER BY
            id_team, pts DESC NULLS LAST;
END;
$$ LANGUAGE plpgsql;
