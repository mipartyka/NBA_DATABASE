import re

import requests
from bs4 import BeautifulSoup, Comment

import nbaTeams as teams

us_states = [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut',
    'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa',
    'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan',
    'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
    'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma',
    'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas',
    'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
]

canadian_provinces = [
    "Alberta",
    "British Columbia",
    "Manitoba",
    "New Brunswick",
    "Newfoundland and Labrador",
    "Nova Scotia",
    "Ontario",
    "Prince Edward Island",
    "Quebec",
    "Saskatchewan",
]

# Assume primaryKeys is a dictionary mapping player names to IDs
# Initialize an empty dictionary if not already defined
primaryKeys = {}

# Open the file in read mode
with open('IDMap.txt', 'r') as f:
    for line in f:
        # Split the line into key and value
        key, value = line.strip().split(': ')
        # Remove the curly braces from the value
        value = value.strip('{}')
        # Add the key-value pair to the dictionary
        primaryKeys[key] = int(value)


def convert_time(time_str):
    minutes, seconds = map(int, time_str.split(':'))
    hours = minutes // 60
    minutes = minutes % 60
    return f'{hours:02d}:{minutes:02d}:{seconds:02d}'


def getGameInserts(url, gameID, type='regular season game'):
    sqlInsertListGame = []
    sqlInsertListPlayerGame = []

    # Send a GET request to the URL
    response = requests.get(url)

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.text, 'html.parser')

        # Find every 8th <th> element for "Basic Box Score Stats"
        basic_stats_headers = soup.find_all('th', {'data-stat': 'header_tmp', 'colspan': '20',
                                                   'class': 'over_header center'})[::7]

        for basic_stats_header in basic_stats_headers:
            # Navigate to the corresponding <tbody> element for each header
            tbody = basic_stats_header.find_next('tbody')

            if tbody:
                # Find all player rows within the targeted tbody
                player_rows = tbody.find_all('tr')

                for player_row in player_rows:
                    # Check if player row contains data-append-csv attribute
                    if player_row.find('th', {'data-append-csv': True}):
                        playerID = primaryKeys[player_row.find('th', {'data-append-csv': True}).get('data-append-csv')]

                        # Initialize a dictionary to store the values for each data-stat
                        player_data = {"id_player": playerID}

                        # Find and store the values for the specified data-stat attributes
                        for stat in ["mp", "fg", "fga", "fg_pct", "fg3", "fg3a", "fg3_pct", "ft", "fta", "ft_pct",
                                     "orb", "drb", "trb", "ast", "stl", "blk", "tov", "pf", "pts", "plus_minus"]:
                            stat_value = player_row.find('td', {'data-stat': stat})
                            stat_text = stat_value.text.strip() if stat_value else 'NULL'
                            player_data[stat] = stat_text

                        if player_data['fg'] == '0' and player_data['fga'] == '0':
                            player_data['fg_pct'] = 'NULL'
                        if player_data['fg3'] == '0' and player_data['fg3a'] == '0':
                            player_data['fg3_pct'] = 'NULL'
                        if player_data['ft'] == '0' and player_data['fta'] == '0':
                            player_data['ft_pct'] = 'NULL'

                        # Generate the SQL INSERT statement
                        # Corrected the line below to properly join the gameID
                        columns = ', '.join(['id_game'] + list(player_data.keys()))
                        values = ', '.join([f"'{gameID}'"] + [
                            f"'{convert_time(value)}'" if key == 'mp' and value != 'NULL' else f"0{value}" if 'pct' in key and f"{value}".startswith(
                                '.') else f"{value.replace('+', '')}" if key == 'plus_minus' else f"{value}" if value == 'NULL' else f"{value}"
                            for key, value in player_data.items()])
                        sql_insert = f"INSERT INTO player_game ({columns}) VALUES ({values});\n"

                        # Write the SQL INSERT statement to the file
                        sqlInsertListPlayerGame.append(sql_insert)
            else:
                print("No <tbody> element found under 'Basic Box Score Stats'.")
        # Find the breadcrumbs div
        breadcrumbs_div = soup.find('div', class_='breadcrumbs')

        if breadcrumbs_div:
            # Find the strong tag within the breadcrumbs div
            strong_tag = breadcrumbs_div.find('strong')

            if strong_tag:
                # Extract and print the text inside the strong tag
                game_description = strong_tag.get_text(strip=True)
            else:
                print("Strong tag not found within breadcrumbs div.")
            game_description = game_description.split(' at ')
            away_team = game_description[0].strip()
            game_description = game_description[1].split(' Box Score, ')
            home_team = game_description[0].strip()
            game_date = game_description[1].strip()
            away_team_id = teams.nba_teams[away_team]
            home_team_id = teams.nba_teams[home_team]
        else:
            print("Breadcrumbs div not found.")

        sql_insert = (
            f"INSERT INTO game "
            f"(id_game, id_home_team, id_away_team, game_date, game_type) "
            f"VALUES "
            f"('{gameID}', {home_team_id}, {away_team_id}, TO_DATE('{game_date}', 'Month DD, YYYY'), '{type}');\n"
        )
        sqlInsertListGame.append(sql_insert)
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
    return sqlInsertListGame, sqlInsertListPlayerGame


def getPlayerInfo(url, contractYearID):
    response = requests.get(url)

    if response.status_code == 200:

        soup = BeautifulSoup(response.text, 'html.parser')

        ############ PLAYER INFO ############

        team_tag = soup.find('a', {'class': 'poptip default', 'data-tip': lambda x: x and '2024' in x})

        teamYears = team_tag.get('data-tip') if team_tag and '2024' in team_tag.get('data-tip', '') else None
        team = teamYears.split(',')[0] if teamYears else None
        teamID = teams.nba_teams[team] if team else 'NULL'

        player_tag = soup.find('div', class_='breadcrumbs').find('strong')
        player_name = player_tag.text.strip()

        line = soup.find('div', {'id': 'info', 'class': 'players'})

        position_line = soup.find('h3', string=lambda s: s and 'position' in s.lower())

        p_tag = position_line.find_next('p')

        position = p_tag.text.strip()
        position = position.replace('.', '')

        height_tag = line.find_all('span', class_=False)[1]
        height = height_tag.text.strip()

        country_tag = soup.select_one('a[href*="/friv/birthplaces.fcgi?country="]')
        country = country_tag.text.strip()
        if country in us_states:
            country = country + ' USA'
        elif country in canadian_provinces:
            country = 'Canada'

        date_of_birth = line.find_next('span', {'id': 'necro-birth'}).get('data-birth')

        salaryList = []
        yearList = ['2023-24', '2024-25', '2025-26', '2026-27', '2027-28', '2028-29']
        contractYearList = []
        contractOptionList = []
        contractLength = None
        contractMoney = None

        comments = soup.find_all(
            string=lambda text: isinstance(text, Comment) and '<div class="section_content" id="div_contract">' in text)

        for comment in comments:
            comment_soup = BeautifulSoup(comment, 'html.parser')

            ############ CONTRACT INFO ############

            try:

                li_element = comment_soup.find('li', string=lambda text: 'Signed' in text)
                if li_element:
                    contract = li_element.text.strip()
                    if contract.split(' ')[
                        2] != 'minimum' and '/' in contract or 'two-way' in contract or 'Exhibit' in contract or 'rookie' in contract or 'extension' in contract:
                        contract = contract.split(' ')[1]
                    elif contract.split(' ')[1] == 'minimum':
                        contract = contract.split(' ')[1]
                        contractLength = 'minimum'
                        contractMoney = 'minimum'
                    else:
                        contract = contract.split(' ')
                        contractLength = contract[1].replace('-yr', '')
                        contract = contract[2]

                    if 'MM' in contract or contract != 'rookie' and contract != 'extension' and 'two-way' not in contract and contract != 'minimum' and contract != 'Exhibit':
                        contractMoney = contract.replace('$', '')
                        if 'MM' in contractMoney:
                            contractMoney = contractMoney.replace('MM', '000000')
                        elif '/' not in contractMoney:
                            contractMoney = contractMoney.replace('M', '000000').replace('$', '')
                        else:
                            contractLength = contract.split('/')[0].replace('-yr', '')
                            contractMoney = contract.split('/')[1].replace('$', '')
                            contractMoney = contractMoney.replace('M', '000000')
                        contractMoney = float(contractMoney)
                        contractLength = int(contractLength)
                    else:
                        if contract == 'extension':
                            contractLength = 'extension'
                            contractMoney = 'extension'
                        elif contract == 'rookie':
                            contractLength = 'rookie'
                            contractMoney = 'rookie'
                        elif 'two-way' in contract:
                            contractLength = 'two-way'
                            contractMoney = 'two-way'
                        elif contract == 'minimum':
                            contractLength = contractLength
                            contractMoney = 'minimum'
                        elif contract == 'Exhibit':
                            contractLength = 'Exhibit'
                            contractMoney = 'Exhibit'
                        else:
                            contractLength = 'NULL'
                            contractMoney = 'NULL'
            except Exception:
                contractLength = 'no information'
                contractMoney = 'no information'

            ############ CONTRACT YEAR INFO ############

            td_elements = comment_soup.find_all('td')

            for td in td_elements:
                span = td.find('span')
                if span is not None:

                    if span.get('class') == ['salary-pl']:
                        contractOptionList.append('Player Option')
                    elif span.get('class') == ['salary-tm']:
                        contractOptionList.append('Team Option')
                    else:
                        contractOptionList.append('No Option')

                    salary = span.text.strip()
                    salary = salary.replace('$', '').replace(',', '')
                    salaryList.append(salary)

        name_parts = player_name.split()

        first_name = name_parts[0]
        if len(name_parts) > 2 and (
                name_parts[-1] == 'Jr.' or name_parts[-1] == 'Jr' or name_parts[-1] == 'Junior' or re.match(
            r'^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$', name_parts[-1])):
            last_name = name_parts[-2] + ' ' + name_parts[-1]
        else:
            last_name = name_parts[-1]

        playerID = url.split('/')[-1].replace('.html', '')
        playerID = primaryKeys[playerID]

        for i, salary in enumerate(salaryList, start=1):
            sql_insert = (
                f"INSERT INTO contract_year "
                f"(id_contract_year, id_contract, year, money, option) "
                f"VALUES "
                f"({contractYearID}, {playerID}, '{yearList[i - 1]}', {salary}, '{contractOptionList[i - 1]}');\n"
            )
            contractYearList.append(sql_insert)
            contractYearID += 1
        if contractLength == 'NULL':
            contractInfo = (
                f"INSERT INTO player_contract "
                f"(id_contract, money, length) "
                f"VALUES "
                f"({playerID}, '{contractMoney}', '{contractLength}');\n"
            )
        else:
            contractInfo = (
                f"INSERT INTO player_contract "
                f"(id_contract, money, length) "
                f"VALUES "
                f"({playerID}, NULL, NULL);\n"
            )

        # Define the SQL insert statement
        playerInfo = (
            f"INSERT INTO player "
            f"(id_player, id_team, id_contract, id_player_stats, name, surname, date_of_birth, nationality, position, height) "
            f"VALUES "
            f"({playerID}, {teamID}, {playerID}, {playerID}, '{first_name}', '{last_name}', '{date_of_birth}'::DATE, '{country}', '{position}', '{height}');\n"
        )
        print(first_name, last_name)
        return playerInfo, contractInfo, contractYearList
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")


def update_player_team(url, teamID):
    # Send an HTTP request to the URL
    response = requests.get(url)

    print(response.status_code)
    # Parse the HTML content of the page using BeautifulSoup
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the line with the specified caption
    roster_table_caption = soup.find('caption', string='Roster Table')

    # Initialize a list to store href values
    href_list = []
    sqlInsertList = []
    playerIDList = []
    missingPlayersList = []

    # If the caption is found, proceed to extract href from each line
    if roster_table_caption:
        # Find all rows (tr) in the table
        rows = roster_table_caption.find_next('table').find_all('tr')

        # Loop through each row
        for row in rows:
            # Find the link (a) within the row
            link = row.find('a', href=True)

            # If a link is found, append the href to the list
            if link and link['href'] not in href_list:
                href_list.append(link['href'])
            try:
                href_list = list(set(href_list))
                for (href) in href_list:
                    playerID = href.split('/')[-1].replace('.html', '')
                    playerID = primaryKeys[playerID]
                    if playerID not in playerIDList:
                        playerIDList.append(playerID)
            except Exception:
                if playerID not in missingPlayersList:
                    missingPlayersList.append(playerID)
                    print(playerID + ' not found')
        for playerID in playerIDList:
            sql_insert = (
                f"UPDATE player "
                f"SET id_team = {teamID} "
                f"WHERE id_player = {playerID};\n"
            )
            sqlInsertList.append(sql_insert)
    return sqlInsertList, missingPlayersList

def getDayGameLinks(url):
    # Send an HTTP request to the URL
    response = requests.get(url)

    # Parse the HTML content of the page using BeautifulSoup
    soup = BeautifulSoup(response.text, 'html.parser')

    # Initialize a list to store href values
    hrefList = []

    # If the caption is found, proceed to extract href from each line
    game_summaries_div = soup.find('div', class_='game_summaries')
     # Find all links (a) within the div with class 'right gamelink'
    game_links = game_summaries_div.find_all('td', class_='right gamelink')
    
    # Iterate through each td and find the link
    for gamelink_td in game_links:
        game_link = gamelink_td.find('a', href=True)
        
        # Check if the link is found
        if game_link:
            # Print the href attribute of the link
            hrefList.append(game_link['href'])
        else:
            print("Link not found inside the td.")
    else:
        print("Div not found.")

    return hrefList

# Example usage
# getGameInserts('https://www.basketball-reference.com/boxscores/202312020CHO.html', '202312020CHO')
# getPlayerInfo("https://www.basketball-reference.com/players/b/bullore01.html", 0)
# update_player_team('https://www.basketball-reference.com/teams/BOS/2024.html', 2)
# getDayGameLinks('https://www.basketball-reference.com/boxscores/?month=12&day=17&year=2023')
