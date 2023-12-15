import nba as nba
import time

primaryKeys = nba.primaryKeys
reversedPrimaryKeys = {value: key for key, value in primaryKeys.items()}

#open('../../sqlGenerated/PlayerInserts.sql', 'w').close()

contractYearID = 0

for i in range(0, 767):
    url = f"https://www.basketball-reference.com/players/{reversedPrimaryKeys[i][0]}/{reversedPrimaryKeys[i]}.html"
    player_info, contract_info, contract_year_list = nba.getPlayerInfo(url, contractYearID)
    with open('../../sqlGenerated/PlayerInserts.sql', 'a') as file:
        file.write(str(contract_info))
        file.write(str(player_info))
        for contract_year in contract_year_list:
            file.write(str(contract_year))
    contractYearID += len(contract_year_list)
    print("{:.2f}%".format((i/760)*100))

    # to prevent getting blocked by the website
    time.sleep(2)



