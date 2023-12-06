import nba as nba

primaryKeys = nba.primaryKeys
reversedPrimaryKeys = {value: key for key, value in primaryKeys.items()}

open('PlayerInserts', 'w').close()

for i in range(0, 11):
    url = f"https://www.basketball-reference.com/players/{reversedPrimaryKeys[i][0]}/{reversedPrimaryKeys[i]}.html"
    with open('PlayerInserts', 'a') as file:
        player_info = nba.getPlayerInfo(url)
        file.write(str(player_info))

