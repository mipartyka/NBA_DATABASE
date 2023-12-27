import time

import psycopg2
import sys
import nba as nba

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

# Create a cursor object
cur = conn.cursor()

cur.execute("SET search_path TO nba_project")

dayURL = 'https://www.basketball-reference.com/boxscores/?month=' + sys.argv[1] + '&day=' + sys.argv[2] + '&year=' + sys.argv[3]


hrefList = nba.getDayGameLinks(dayURL)


for url in hrefList:
    print('Adding game: ' + url + '\n')
    print('Thats game number: ' + str(hrefList.index(url) + 1) + '/' + str(len(hrefList)) + ' for day ' + sys.argv[1] + '/' + sys.argv[2] + '/' + sys.argv[3] + '\n')

    url = 'https://www.basketball-reference.com' + url
    # print(url)
    gameID = url.split('/')[-1].split('.')[0]

    insertGameList, insertPlayerGameList = nba.getGameInserts(url, gameID)

    # Write to the database

    for query in insertGameList:
        cur.execute(query)

    for query in insertPlayerGameList:
        cur.execute(query)

    time.sleep(2)

# Commit the transaction
conn.commit()

# Close the cursor and connection
cur.close()
conn.close()
