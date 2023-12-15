import sys
import nbaTeams as nt
import nba as nba
import time

sqlInsertList = []

lastID = int(sys.argv[1])

open('../../sqlGenerated/PlayerTeamUpdates.sql', 'w').close()

for i in range(1, 31):
    team = nt.team_short_names[i]
    sqlInsertList, missingPlayersList = nba.update_player_team(f'https://www.basketball-reference.com/teams/{team}/2024.html', i)
    with open('../../sqlGenerated/PlayerTeamUpdates.sql', 'a') as file:
        for sqlInsert in sqlInsertList:
            file.write(sqlInsert)
    with open('../../IDMap.txt', 'a') as file:
        for player in missingPlayersList:
            lastID += 1
            file.write(f'\n{player}: ' + '{' +str(lastID) + '}')
    time.sleep(2)

