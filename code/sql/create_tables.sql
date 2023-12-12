create schema nba_project;
set search_path to nba_project;

-- Create team table
CREATE TABLE team (
    id_team INT PRIMARY KEY,
    id_team_stats INT,
    id_coach INT,
    name VARCHAR(255),
    city VARCHAR(255),
    arena VARCHAR(255),
    conference VARCHAR(255),
    division VARCHAR(255)
);

-- Create player table
CREATE TABLE player (
    id_player INT PRIMARY KEY,
    id_team INT,
    id_contract INT,
    id_player_stats INT,
    name VARCHAR(255),
    surname VARCHAR(255),
    date_of_birth DATE,
    nationality VARCHAR(255),
    position VARCHAR(255)
);

-- Create player_stats table
CREATE TABLE player_stats (
    id_player_stats INT PRIMARY KEY,
    games INT,
    points INT,
    rebounds INT,
    assists INT,
    steals INT,
    minutes INT,
    fouls INT,
    fgm INT,
    fga INT,
    fg_percent DOUBLE precision,
    three_p INT,
    three_pa INT,
    three_p_percent DOUBLE precision,
    ft INT,
    fta INT,
    ft_percent DOUBLE precision,
    o_rebounds INT,
    d_rebounds INT,
    turnovers INT,
    plus_minus INT
);

-- Create team_stats table
CREATE TABLE team_stats (
    id_team_stats INT PRIMARY KEY,
    points INT,
    rebounds INT,
    assists INT,
    steals INT,
    fgm INT,
    fga INT,
    fg_percent DOUBLE precision,
    three_p INT,
    three_pa INT,
    three_p_percent DOUBLE precision,
    ft INT,
    fta INT,
    ft_percent DOUBLE precision,
    o_rebounds INT,
    d_rebounds INT,
    turnovers INT
);

-- Create player_contract table
CREATE TABLE player_contract (
    id_contract INT PRIMARY KEY,
    money DECIMAL(15, 2), -- Adjust precision and scale as needed
    sign_year INT,
    length INT,
    contract_type VARCHAR(255),
    contract_option VARCHAR(255)
);

-- Create contract_year table
CREATE TABLE contract_year (
    id_contract_year INT PRIMARY KEY,
    id_contract INT,
    year INT,
    money DECIMAL(15, 2) -- Adjust precision and scale as needed
);

-- Create player_game table
CREATE TABLE player_game (
    id_game INT,
    id_player INT,
    points INT,
    rebounds INT,
    assists INT,
    steals INT,
    fgm INT,
    fga INT,
    fg_percent DOUBLE precision,
    three_p INT,
    three_pa INT,
    three_p_percent DOUBLE precision,
    ft INT,
    fta INT,
    ft_percent DOUBLE precision,
    o_rebounds INT,
    d_rebounds INT,
    turnovers INT,
    PRIMARY KEY (id_game, id_player)
);

-- Create team_game table
CREATE TABLE team_game (
    id_game INT,
    id_team INT,
    points INT,
    rebounds INT,
    assists INT,
    steals INT,
    fgm INT,
    fga INT,
    fg_percent DOUBLE precision,
    three_p INT,
    three_pa INT,
    three_p_percent DOUBLE precision,
    ft INT,
    fta INT,
    ft_percent DOUBLE precision,
    o_rebounds INT,
    d_rebounds INT,
    turnovers INT,
    PRIMARY KEY (id_game, id_team)
);

-- Create game table
CREATE TABLE game (
    id_game INT PRIMARY KEY,
    id_home_team INT,
    id_away_team INT,
    game_date DATE,
    game_type VARCHAR(255)
);

-- Create coach table
CREATE TABLE coach (
    id_coach INT PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255)
);


-- Add foreign key constraints
ALTER TABLE team
ADD CONSTRAINT fk_team_stats FOREIGN KEY (id_team_stats) REFERENCES team_stats(id_team_stats);

ALTER TABLE team
ADD CONSTRAINT fk_coach FOREIGN KEY (id_coach) REFERENCES coach(id_coach);

-- Add foreign key constraints
ALTER TABLE player
ADD CONSTRAINT fk_team FOREIGN KEY (id_team) REFERENCES team(id_team);

ALTER TABLE player
ADD CONSTRAINT fk_contract FOREIGN KEY (id_contract) REFERENCES player_contract(id_contract);

ALTER TABLE player
ADD CONSTRAINT fk_player_stats FOREIGN KEY (id_player_stats) REFERENCES player_stats(id_player_stats);

-- Add foreign key constraint
ALTER TABLE contract_year
ADD CONSTRAINT fk_contract FOREIGN KEY (id_contract) REFERENCES player_contract(id_contract);

-- Add foreign key constraints
ALTER TABLE player_game
ADD CONSTRAINT fk_game FOREIGN KEY (id_game) REFERENCES game(id_game);

ALTER TABLE player_game
ADD CONSTRAINT fk_player FOREIGN KEY (id_player) REFERENCES player(id_player);

-- Add foreign key constraints
ALTER TABLE team_game
ADD CONSTRAINT fk_game FOREIGN KEY (id_game) REFERENCES game(id_game);

ALTER TABLE team_game
ADD CONSTRAINT fk_team FOREIGN KEY (id_team) REFERENCES team(id_team);

-- Add foreign key constraints
ALTER TABLE game
ADD CONSTRAINT fk_home_team FOREIGN KEY (id_home_team) REFERENCES team(id_team);

ALTER TABLE game
ADD CONSTRAINT fk_away_team FOREIGN KEY (id_away_team) REFERENCES team(id_team);
