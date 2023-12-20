import psycopg2
import sys
import nba as nba


def addNewPlayerByURL(playerIDBBR, teamID=None):
    # Database connection parameters
    db_name = 'cetphhnl'
    db_user = 'cetphhnl'
    db_password = 'aAGfNp5piy3_W3aKzF05IuesJLQg44Li'
    db_host = 'dumbo.db.elephantsql.com'
    db_port = '5432'

    # Connect to the database
    conn = psycopg2.connect(
        dbname=db_name,
        user=db_user,
        password=db_password,
        host=db_host,
        port=db_port
    )

    lastID = len(nba.primaryKeys)

    with open('../../IDMap.txt', 'a') as file:
        file.write(f'\n{playerIDBBR}: ' + '{' + str(lastID) + '}')
        file.close()

    nba.primaryKeys[playerIDBBR] = lastID

    url = f'https://www.basketball-reference.com/players/{playerIDBBR[0]}/{playerIDBBR}.html'

    # Open a cursor to perform database operations
    cur = conn.cursor()

    # Fetch the maximum contractYearID from the contract_year table
    cur.execute("SET search_path TO nba_project")
    cur.execute("SELECT MAX(id_contract_year) FROM contract_year")
    max_contract_year_id = cur.fetchone()[0]

    # If there are no records in the table, set the ID to 1
    if max_contract_year_id is None:
        next_contract_year_id = 1
    else:
        # Increment the maximum ID by 1
        next_contract_year_id = max_contract_year_id + 1

    player, contract, contractYearList = nba.getPlayerInfo(url, next_contract_year_id)

    cur.execute(f"insert into player_stats (id_player_stats) select generate_series ({lastID}, {lastID} + 1);")

    cur.execute(contract)

    print(player)
    cur.execute(player)

    for query in contractYearList:
        cur.execute(query)

    conn.commit()

    # Close the cursor and connection
    cur.close()
    conn.close()
