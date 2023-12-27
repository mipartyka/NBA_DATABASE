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

CREATE OR REPLACE FUNCTION CalculateLuxuryTax(p_team_id INT, p_alternate_rates BOOLEAN)
    RETURNS DECIMAL(15, 2) AS $$
DECLARE
    cap_space_limit DECIMAL(15, 2) := 165294000;  -- NBA cap space limit in dollars
    tax_rate_1 DECIMAL(4, 2);
    tax_rate_2 DECIMAL(4, 2);
    tax_rate_3 DECIMAL(4, 2);
    tax_rate_4 DECIMAL(4, 2);
    additional_rate DECIMAL(4, 2) := 0.50;  -- Additional rate for every $5 million above $20 million
    team_salary DECIMAL(15, 2);
    luxury_tax DECIMAL(15, 2);
    rest INT;
BEGIN
    -- Set tax rates based on the alternate_rates argument
    IF p_alternate_rates THEN
        tax_rate_1 := 2.50;
        tax_rate_2 := 2.75;
        tax_rate_3 := 3.50;
        tax_rate_4 := 4.25;
    ELSE
        tax_rate_1 := 1.50;
        tax_rate_2 := 1.75;
        tax_rate_3 := 2.50;
        tax_rate_4 := 3.25;
    END IF;

    -- Get the total salary for the team
    SELECT total_salary FROM getTeamSalaries(p_team_id)
    INTO team_salary;

    -- Calculate luxury tax
    IF team_salary > cap_space_limit THEN
        -- Calculate tax for each bracket
        IF team_salary <= cap_space_limit + 5000000 THEN
            luxury_tax := (team_salary - cap_space_limit) * tax_rate_1;
        ELSE
            luxury_tax := 5000000 * tax_rate_1;

            IF team_salary <= cap_space_limit + 10000000 THEN
                luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 5000000)) * tax_rate_2;
            ELSE
                luxury_tax := luxury_tax + 5000000 * tax_rate_2;

                IF team_salary <= cap_space_limit + 15000000 THEN
                    luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 10000000)) * tax_rate_3;
                ELSE
                    luxury_tax := luxury_tax + 5000000 * tax_rate_3;

                    IF team_salary <= cap_space_limit + 20000000 THEN
                        luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 15000000)) * tax_rate_4;
                    ELSE
                        luxury_tax := luxury_tax + 5000000 * tax_rate_4;
                        -- Calculate tax for the next $5 million above the cap
                        IF team_salary <= cap_space_limit + 20000000 THEN
                            luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 15000000)) * (tax_rate_4);
                        ELSE
                            luxury_tax := luxury_tax + 5000000 * (tax_rate_4);
                            -- Calculate tax for every additional $5 million above the cap
                            additional_rate := tax_rate_4;
                            IF team_salary > cap_space_limit + 20000000 THEN
                                FOR i IN 1..((team_salary - (cap_space_limit + 20000000)) / 5000000) LOOP
                                    luxury_tax := luxury_tax + additional_rate * 5000000;
                                    additional_rate := additional_rate + 0.5; -- Increase additional rate by 0.5 for each $5 million increment
                                END LOOP;
                                rest := ((team_salary - (cap_space_limit + 20000000)) / 5000000);
                                luxury_tax := luxury_tax + (team_salary - (cap_space_limit + 20000000 + 5000000 * rest)) * additional_rate;
                            END IF;
                        END IF;
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
