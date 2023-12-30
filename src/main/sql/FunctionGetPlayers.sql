set search_path to 'nba_project';

DROP FUNCTION IF EXISTS get_players();

CREATE OR REPLACE FUNCTION get_players()
    RETURNS TABLE (
                      id_player INT,
                      name VARCHAR(255),
                      surname VARCHAR(255),
                      pos VARCHAR(255),
                      team VARCHAR(255),
                      games INT,
                      pts DOUBLE PRECISION,
                      trb DOUBLE PRECISION,
                      ast DOUBLE PRECISION,
                      stl DOUBLE PRECISION,
                      blk DOUBLE PRECISION,
                      minutes TIME,
                      fg_made DOUBLE PRECISION,
                      fg_attempted DOUBLE PRECISION,
                      fg_percentage DOUBLE PRECISION,
                      three_p_made DOUBLE PRECISION,
                      three_p_attempted DOUBLE PRECISION,
                      three_p_percentage DOUBLE PRECISION,
                      ft_made DOUBLE PRECISION,
                      ft_attempted DOUBLE PRECISION,
                      ft_percentage DOUBLE PRECISION,
                      off_rebounds DOUBLE PRECISION,
                      def_rebounds DOUBLE PRECISION,
                      turnovers DOUBLE PRECISION,
                      fouls DOUBLE PRECISION,
                      plus_minus DOUBLE PRECISION,
                      player_id INT
                  )
AS $$
BEGIN
    RETURN QUERY
        SELECT
            p.id_player,
            p.name,
            p.surname,
            p.position as pos,
            CASE WHEN p.id_team IS NOT NULL THEN t.name ELSE NULL END AS team,
            ps.games,
            ps.pts,
            ps.trb,
            ps.ast,
            ps.stl,
            ps.blk,
            ps.mp AS minutes,
            ps.fg AS fg_made,
            ps.fga AS fg_attempted,
            ps.fg_pct AS fg_percentage,
            ps.fg3 AS three_p_made,
            ps.fg3a AS three_p_attempted,
            ps.fg3_pct AS three_p_percentage,
            ps.ft AS ft_made,
            ps.fta AS ft_attempted,
            ps.ft_pct AS ft_percentage,
            ps.orb AS off_rebounds,
            ps.drb AS def_rebounds,
            ps.tov,
            ps.pf AS fouls,
            ps.plus_minus,
            p.id_player
        FROM
            player p
                JOIN
            player_stats ps ON p.id_player_stats = ps.id_player_stats
                LEFT JOIN
            team t ON p.id_team = t.id_team
        ORDER BY p.id_player;
END;
$$ LANGUAGE plpgsql;
