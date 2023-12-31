set search_path to "nba_project";

CREATE OR REPLACE FUNCTION add_user(
    p_login VARCHAR(255),
    p_password VARCHAR(255),
    p_role VARCHAR(50)
) RETURNS VOID AS
$$
BEGIN
    INSERT INTO "user" (login, password, role)
    VALUES (p_login, p_password, p_role);
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_by_login(p_login VARCHAR(255)) RETURNS TABLE (
                                                                                     id_user INTEGER,
                                                                                     login VARCHAR(255),
                                                                                     password VARCHAR(255),
                                                                                     role VARCHAR(50)
                                                                                 ) AS
$$
BEGIN
    RETURN QUERY
        SELECT u.id_user, u.login, u.password, u.role
        FROM "user" u
        WHERE u.login = p_login;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_by_id(p_id_user INTEGER) RETURNS TABLE (
                                                                               id_user INTEGER,
                                                                               login VARCHAR(255),
                                                                               password VARCHAR(255),
                                                                               role VARCHAR(50)
                                                                           ) AS
$$
BEGIN
    RETURN QUERY
        SELECT u.id_user, u.login, u.password, u.role
        FROM "user" u
        WHERE u.id_user = p_id_user;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_if_user_and_password_exist(
    p_login VARCHAR(255),
    p_password VARCHAR(255)
) RETURNS BOOLEAN AS
$$
DECLARE
    user_exists BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1
        FROM "user"
        WHERE login = p_login AND password = p_password
    ) INTO user_exists;

    RETURN user_exists;
END;
$$
LANGUAGE plpgsql;
