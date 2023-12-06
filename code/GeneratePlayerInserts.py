import nba as nba

primaryKeys = nba.primaryKeys
reversedPrimaryKeys = {value: key for key, value in primaryKeys.items()}

open('../sqlGenerated/PlayerInserts.sql', 'w').close()

for i in range(0, 11):
    url = f"https://www.basketball-reference.com/players/{reversedPrimaryKeys[i][0]}/{reversedPrimaryKeys[i]}.html"
    player_info, contract_info, contract_year_list = nba.getPlayerInfo(url)
    with open('../sqlGenerated/PlayerInserts.sql', 'a') as file:
        file.write(str(player_info))
        file.write(str(contract_info))
        for contract_year in contract_year_list:
            file.write(str(contract_year))



