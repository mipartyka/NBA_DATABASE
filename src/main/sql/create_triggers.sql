set search_path to 'nba_project';

-- Trigger to update team_stats when a new team_game record is inserted
CREATE OR REPLACE FUNCTION update_team_stats_trigger()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE team_stats
    SET
        pts = (SELECT AVG(pts) FROM team_game WHERE id_team = NEW.id_team),
        trb = (SELECT AVG(trb) FROM team_game WHERE id_team = NEW.id_team),
        ast = (SELECT AVG(ast) FROM team_game WHERE id_team = NEW.id_team),
        stl = (SELECT AVG(stl) FROM team_game WHERE id_team = NEW.id_team),
        fg = (SELECT AVG(fg) FROM team_game WHERE id_team = NEW.id_team),
        fga = (SELECT AVG(fga) FROM team_game WHERE id_team = NEW.id_team),
        fg_pct = (SELECT CASE WHEN AVG(fga) = 0 THEN NULL ELSE AVG(fg)/AVG(fga) END FROM team_game WHERE id_team = NEW.id_team),
        fg3 = (SELECT AVG(fg3) FROM team_game WHERE id_team = NEW.id_team),
        fg3a = (SELECT AVG(fg3a) FROM team_game WHERE id_team = NEW.id_team),
        fg3_pct = (SELECT CASE WHEN AVG(fg3a) = 0 THEN NULL ELSE AVG(fg3)/AVG(fg3a) END FROM team_game WHERE id_team = NEW.id_team),
        ft = (SELECT AVG(ft) FROM team_game WHERE id_team = NEW.id_team),
        fta = (SELECT AVG(fta) FROM team_game WHERE id_team = NEW.id_team),
        ft_pct = (SELECT CASE WHEN AVG(fta) = 0 THEN NULL ELSE AVG(ft)/AVG(fta) END FROM team_game WHERE id_team = NEW.id_team),
        orb = (SELECT AVG(orb) FROM team_game WHERE id_team = NEW.id_team),
        drb = (SELECT AVG(drb) FROM team_game WHERE id_team = NEW.id_team),
        tov = (SELECT AVG(tov) FROM team_game WHERE id_team = NEW.id_team),
        blk = (SELECT AVG(blk) FROM team_game WHERE id_team = NEW.id_team)
    WHERE id_team_stats = NEW.id_team;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to activate the update_team_stats_trigger function on insert into team_game
CREATE TRIGGER team_game_insert_trigger
AFTER INSERT ON team_game
FOR EACH ROW
EXECUTE FUNCTION update_team_stats_trigger();



-- Trigger to update player_stats when a new player_game record is inserted
CREATE OR REPLACE FUNCTION update_player_stats_trigger()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE player_stats
    SET
        games = (SELECT COUNT(*) FROM player_game WHERE id_player = NEW.id_player),
        pts = (SELECT AVG(pts) FROM player_game WHERE id_player = NEW.id_player),
        trb = (SELECT AVG(trb) FROM player_game WHERE id_player = NEW.id_player),
        ast = (SELECT AVG(ast) FROM player_game WHERE id_player = NEW.id_player),
        stl = (SELECT AVG(stl) FROM player_game WHERE id_player = NEW.id_player),
        mp = (SELECT AVG(mp) FROM player_game WHERE id_player = NEW.id_player),
        pf = (SELECT AVG(pf) FROM player_game WHERE id_player = NEW.id_player),
        fg = (SELECT AVG(fg) FROM player_game WHERE id_player = NEW.id_player),
        fga = (SELECT AVG(fga) FROM player_game WHERE id_player = NEW.id_player),
        fg_pct = (SELECT CASE WHEN AVG(fga) = 0 THEN NULL ELSE AVG(fg)/AVG(fga) END FROM player_game WHERE id_player = NEW.id_player),
        fg3 = (SELECT AVG(fg3) FROM player_game WHERE id_player = NEW.id_player),
        fg3a = (SELECT AVG(fg3a) FROM player_game WHERE id_player = NEW.id_player),
        fg3_pct = (SELECT CASE WHEN AVG(fg3a) = 0 THEN NULL ELSE AVG(fg3)/AVG(fg3a) END FROM player_game WHERE id_player = NEW.id_player),
        ft = (SELECT AVG(ft) FROM player_game WHERE id_player = NEW.id_player),
        fta = (SELECT AVG(fta) FROM player_game WHERE id_player = NEW.id_player),
        ft_pct = (SELECT CASE WHEN AVG(fta) = 0 THEN NULL ELSE AVG(ft)/AVG(fta) END FROM player_game WHERE id_player = NEW.id_player),
        orb = (SELECT AVG(orb) FROM player_game WHERE id_player = NEW.id_player),
        drb = (SELECT AVG(drb) FROM player_game WHERE id_player = NEW.id_player),
        tov = (SELECT AVG(tov) FROM player_game WHERE id_player = NEW.id_player),
        blk = (SELECT AVG(blk) FROM player_game WHERE id_player = NEW.id_player),
        plus_minus = (SELECT AVG(plus_minus) FROM player_game WHERE id_player = NEW.id_player)
    WHERE id_player_stats = NEW.id_player;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Trigger to activate the update_player_stats_trigger function on insert into player_game
CREATE TRIGGER player_game_insert_trigger
AFTER INSERT ON player_game
FOR EACH ROW
EXECUTE FUNCTION update_player_stats_trigger();



CREATE OR REPLACE FUNCTION update_team_game_trigger() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    -- Check if a record for the same game and team already exists
    IF EXISTS (
        SELECT 1
        FROM team_game
        WHERE id_game = NEW.id_game AND id_team = NEW.id_team
    ) THEN
        -- Update existing record
UPDATE team_game
SET
    pts = subquery.pts,
    trb = subquery.trb,
    ast = subquery.ast,
    stl = subquery.stl,
    fg = subquery.fg,
    fga = subquery.fga,
    fg_pct = subquery.fg_pct,
    fg3 = subquery.fg3,
    fg3a = subquery.fg3a,
    fg3_pct = subquery.fg3_pct,
    ft = subquery.ft,
    fta = subquery.fta,
    ft_pct = subquery.ft_pct,
    orb = subquery.orb,
    drb = subquery.drb,
    tov = subquery.tov,
    blk = subquery.blk
    FROM (
            SELECT
                SUM(pts) AS pts,
                SUM(trb) AS trb,
                SUM(ast) AS ast,
                SUM(stl) AS stl,
                SUM(fg) AS fg,
                SUM(fga) AS fga,
                CASE WHEN COUNT(*) > 0 THEN SUM(fg_pct) / COUNT(*) ELSE 0 END AS fg_pct,
                SUM(fg3) AS fg3,
                SUM(fg3a) AS fg3a,
                CASE WHEN COUNT(*) > 0 THEN SUM(fg3_pct) / COUNT(*) ELSE 0 END AS fg3_pct,
                SUM(ft) AS ft,
                SUM(fta) AS fta,
                CASE WHEN COUNT(*) > 0 THEN SUM(ft_pct) / COUNT(*) ELSE 0 END AS ft_pct,
                SUM(orb) AS orb,
                SUM(drb) AS drb,
                SUM(tov) AS tov,
                SUM(blk) AS blk
            FROM player_game
            WHERE
                id_game = NEW.id_game AND
                id_team = NEW.id_team
        ) AS subquery
WHERE
    id_game = NEW.id_game AND
    id_team = NEW.id_team;
ELSE
        -- Insert new record
        INSERT INTO team_game (id_game, id_team, pts, trb, ast, stl, fg, fga, fg_pct, fg3, fg3a, fg3_pct, ft, fta, ft_pct, orb, drb, tov, blk)
SELECT
    NEW.id_game,
    NEW.id_team,
    SUM(pts),
    SUM(trb),
    SUM(ast),
    SUM(stl),
    SUM(fg),
    SUM(fga),
    CASE WHEN COUNT(*) > 0 THEN SUM(fg_pct) / COUNT(*) ELSE 0 END,
    SUM(fg3),
    SUM(fg3a),
    CASE WHEN COUNT(*) > 0 THEN SUM(fg3_pct) / COUNT(*) ELSE 0 END,
    SUM(ft),
    SUM(fta),
    CASE WHEN COUNT(*) > 0 THEN SUM(ft_pct) / COUNT(*) ELSE 0 END,
    SUM(orb),
    SUM(drb),
    SUM(tov),
    SUM(blk)
FROM player_game
WHERE
        id_game = NEW.id_game AND
        id_team = NEW.id_team;
END IF;

RETURN NULL; -- No need to return anything for an AFTER UPDATE trigger
END;
$$;



-- Trigger to activate the update_team_game_trigger function on insert into player_game
CREATE TRIGGER player_game_insert_team_game_trigger
AFTER INSERT ON player_game
FOR EACH ROW
EXECUTE FUNCTION update_team_game_trigger();
