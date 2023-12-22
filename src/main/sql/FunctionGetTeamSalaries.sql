set search_path to 'nba_project';

CREATE OR REPLACE FUNCTION getTeamSalaries(p_team_id INT)
    RETURNS TABLE (
    team_id INT,
    team_name VARCHAR(255),
    total_salary DECIMAL(15, 2)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        t.id_team,
        t.name AS team_name,
        COALESCE(SUM(cy.money), 0.00) AS total_salary
    FROM
        team t
    LEFT JOIN player p ON t.id_team = p.id_team
    LEFT JOIN player_contract pc ON p.id_contract = pc.id_contract
    LEFT JOIN contract_year cy ON pc.id_contract = cy.id_contract
    WHERE
        t.id_team = p_team_id
        AND cy.year = '2023-24'
    GROUP BY
        t.id_team, t.name;
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION getTeamPlayerSalaries(p_team_id INT);

CREATE OR REPLACE FUNCTION getTeamPlayerSalaries(p_team_id INT)
    RETURNS TABLE (
                      player_id INT,
                      name VARCHAR(255),
                      surname VARCHAR(255),
                      salary DECIMAL(15, 2)
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            p.id_player,
            p.name,
            p.surname,
            COALESCE(SUM(cy.money), 0.00) AS salary
        FROM
            player p
                INNER JOIN player_contract pc ON p.id_contract = pc.id_contract
                LEFT JOIN contract_year cy ON pc.id_contract = cy.id_contract
        WHERE
                p.id_team = p_team_id
          AND cy.year = '2023-24'
        GROUP BY
            p.id_player, p.name, p.surname;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION CalculateLuxuryTax(p_team_id INT)
    RETURNS DECIMAL(15, 2) AS $$
DECLARE
    cap_space_limit DECIMAL(15, 2) := 165294000;  -- NBA cap space limit in dollars
    tax_rate_1 DECIMAL(4, 2) := 2.50;  -- Tax rate for $0-5 million above tax line
    tax_rate_2 DECIMAL(4, 2) := 2.75;  -- Tax rate for $5-10 million above tax line
    tax_rate_3 DECIMAL(4, 2) := 3.50;  -- Tax rate for $10-15 million above tax line
    tax_rate_4 DECIMAL(4, 2) := 4.25;  -- Tax rate for $15-20 million above tax line
    additional_rate DECIMAL(4, 2) := 0.50;  -- Additional rate for every $5 million above $20 million
    team_salary DECIMAL(15, 2);
    luxury_tax DECIMAL(15, 2);
BEGIN
    -- Get the total salary for the team
    SELECT total_salary from getTeamSalaries(p_team_id)
    INTO team_salary;


    -- Calculate luxury tax
    IF team_salary > cap_space_limit THEN
        -- Calculate tax for the first $5 million above the cap
        IF team_salary <= cap_space_limit + 5000000 THEN
            luxury_tax := (team_salary - cap_space_limit) * (tax_rate_1 / 100);
        ELSE
            luxury_tax := 5000000 * (tax_rate_1 / 100);

            -- Calculate tax for the next $5 million above the cap
            IF team_salary <= cap_space_limit + 10000000 THEN
                luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 5000000)) * (tax_rate_2 );
            ELSE
                luxury_tax := luxury_tax + 5000000 * (tax_rate_2 / 100);

                -- Calculate tax for the next $5 million above the cap
                IF team_salary <= cap_space_limit + 15000000 THEN
                    luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 10000000)) * (tax_rate_3 );
                ELSE
                    luxury_tax := luxury_tax + 5000000 * (tax_rate_3 / 100);

                    -- Calculate tax for the next $5 million above the cap
                    IF team_salary <= cap_space_limit + 20000000 THEN
                        luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 15000000)) * (tax_rate_4 );
                    ELSE
                        luxury_tax := luxury_tax + 5000000 * (tax_rate_4 / 100);

                        -- Calculate tax for every additional $5 million above the cap
                        luxury_tax := luxury_tax + CEIL((team_salary - (cap_space_limit + 20000000)) / 5000000) *
                                                   additional_rate * (team_salary - (cap_space_limit + 20000000)) ;
                    END IF;
                END IF;
            END IF;
        END IF;
    ELSE
        luxury_tax := 0.00;  -- No luxury tax if team is under the cap
    END IF;

    RETURN luxury_tax;
END;
$$ LANGUAGE plpgsql;