import nba as nba

url = 'https://www.basketball-reference.com/boxscores/202310240DEN.html'

gameID = url.split('/')[-1].split('.')[0]

insertGameList, insertPlayerGameList = nba.getGameInserts(url, gameID)

with open('../../sqlGenerated/InsertPlayerGame' + gameID + '.sql', 'w') as f:
    f.write(''.join(insertPlayerGameList))
with open('../../sqlGenerated/InsertGame' + gameID + '.sql', 'w') as f:
    f.write('\n'.join(insertGameList))