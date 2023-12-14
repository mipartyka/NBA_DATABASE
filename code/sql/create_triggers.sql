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
        fg_pct = (SELECT AVG(fg_pct) FROM team_game WHERE id_team = NEW.id_team),
        fg3 = (SELECT AVG(fg3) FROM team_game WHERE id_team = NEW.id_team),
        fg3a = (SELECT AVG(fg3a) FROM team_game WHERE id_team = NEW.id_team),
        fg3_pct = (SELECT AVG(fg3_pct) FROM team_game WHERE id_team = NEW.id_team),
        ft = (SELECT AVG(ft) FROM team_game WHERE id_team = NEW.id_team),
        fta = (SELECT AVG(fta) FROM team_game WHERE id_team = NEW.id_team),
        ft_pct = (SELECT AVG(ft_pct) FROM team_game WHERE id_team = NEW.id_team),
        orb = (SELECT AVG(orb) FROM team_game WHERE id_team = NEW.id_team),
        drb = (SELECT AVG(drb) FROM team_game WHERE id_team = NEW.id_team),
        turnovers = (SELECT AVG(turnovers) FROM team_game WHERE id_team = NEW.id_team)
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
        fg_pct = (SELECT AVG(fg_pct) FROM player_game WHERE id_player = NEW.id_player),
        fg3 = (SELECT AVG(fg3) FROM player_game WHERE id_player = NEW.id_player),
        fg3a = (SELECT AVG(fg3a) FROM player_game WHERE id_player = NEW.id_player),
        fg3_pct = (SELECT AVG(fg3_pct) FROM player_game WHERE id_player = NEW.id_player),
        ft = (SELECT AVG(ft) FROM player_game WHERE id_player = NEW.id_player),
        fta = (SELECT AVG(fta) FROM player_game WHERE id_player = NEW.id_player),
        ft_pct = (SELECT AVG(ft_pct) FROM player_game WHERE id_player = NEW.id_player),
        orb = (SELECT AVG(orb) FROM player_game WHERE id_player = NEW.id_player),
        drb = (SELECT AVG(drb) FROM player_game WHERE id_player = NEW.id_player),
        turnovers = (SELECT AVG(turnovers) FROM player_game WHERE id_player = NEW.id_player),
        plus_minus = (SELECT AVG(plus_minus) FROM player_game WHERE id_player = NEW.id_player)
    WHERE id_player_stats = NEW.id_player_stats;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to activate the update_player_stats_trigger function on insert into player_game
CREATE TRIGGER player_game_insert_trigger
AFTER INSERT ON player_game
FOR EACH ROW
EXECUTE FUNCTION update_player_stats_trigger();



-- Trigger to update team_game with sums when a new player_game record is inserted
CREATE OR REPLACE FUNCTION update_team_game_trigger()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE team_game
    SET
        pts = (SELECT SUM(pts) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        trb = (SELECT SUM(trb) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        ast = (SELECT SUM(ast) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        stl = (SELECT SUM(stl) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fg = (SELECT SUM(fg) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fga = (SELECT SUM(fga) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fg_pct = (SELECT CASE WHEN COUNT(*) > 0 THEN SUM(fg_pct) / COUNT(*) ELSE 0 END FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fg3 = (SELECT SUM(fg3) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fg3a = (SELECT SUM(fg3a) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fg3_pct = (SELECT CASE WHEN COUNT(*) > 0 THEN SUM(fg3_pct) / COUNT(*) ELSE 0 END FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        ft = (SELECT SUM(ft) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        fta = (SELECT SUM(fta) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        ft_pct = (SELECT CASE WHEN COUNT(*) > 0 THEN SUM(ft_pct) / COUNT(*) ELSE 0 END FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        orb = (SELECT SUM(orb) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        drb = (SELECT SUM(drb) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team),
        turnovers = (SELECT SUM(turnovers) FROM player_game WHERE id_game = NEW.id_game AND id_team = NEW.id_team)
    WHERE id_game = NEW.id_game AND id_team = NEW.id_team;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to activate the update_team_game_trigger function on insert into player_game
CREATE TRIGGER player_game_insert_team_game_trigger
AFTER INSERT ON player_game
FOR EACH ROW
EXECUTE FUNCTION update_team_game_trigger();
