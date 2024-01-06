set search_path to 'nba_project';

Drop view if exists player_stats_view;

-- Create a view for player_stats with advanced stats
CREATE VIEW player_stats_advanced AS
SELECT
    id_player_stats,
    games,
    pts,
    trb,
    ast,
    stl,
    blk,
    mp,
    pf,
    fg,
    fga,
    fg_pct,
    fg3,
    fg3a,
    fg3_pct,
    ft,
    fta,
    ft_pct,
    orb,
    drb,
    tov,
    pf AS personal_fouls,
    plus_minus,
    CAST(ROUND(pts / (2 * (fga + 0.44 * fta))) AS DOUBLE PRECISION) AS true_shooting_pct,
    fg - fg3 AS "2p",
    fga - fg3a AS "2pa",
    CASE WHEN (fga - fg3a) > 0 THEN CAST(ROUND((fg - fg3) / (fga - fg3a)) AS DOUBLE PRECISION) ELSE NULL END AS "2p_pct",
    CAST(ROUND((fg + 0.5 * fg3) / fga) AS DOUBLE PRECISION) AS efg_pct
FROM
    player_stats;

-- Create a view for team_stats with advanced stats
CREATE VIEW team_stats_advanced AS
SELECT
    id_team_stats,
    pts,
    trb,
    ast,
    stl,
    blk,
    fg,
    fga,
    fg_pct,
    fg3,
    fg3a,
    fg3_pct,
    ft,
    fta,
    ft_pct,
    orb,
    drb,
    tov,
    CAST(ROUND(pts / (2 * (fga + 0.44 * fta))) AS DOUBLE PRECISION) AS true_shooting_pct,
    fg - fg3 AS "2p",
    fga - fg3a AS "2pa",
    CASE WHEN (fga - fg3a) > 0 THEN CAST(ROUND((fg - fg3) / (fga - fg3a)) AS DOUBLE PRECISION) ELSE NULL END AS "2p_pct",
    CAST(ROUND((fg + 0.5 * fg3) / fga) AS DOUBLE PRECISION) AS efg_pct
FROM
    team_stats;
