import requests
import nbaTeams as teams
from bs4 import BeautifulSoup, Comment

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
with open('../../IDMap.txt', 'r') as f:
    for line in f:
        # Split the line into key and value
        key, value = line.strip().split(': ')
        # Remove the curly braces from the value
        value = value.strip('{}')
        # Add the key-value pair to the dictionary
        primaryKeys[key] = int(value)

def getGameInserts(url, gameID, type = 'regular season game'):
    # Send a GET request to the URL
    response = requests.get(url)

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.text, 'html.parser')

        # Find every 8th <th> element for "Basic Box Score Stats"
        basic_stats_headers = soup.find_all('th', {'data-stat': 'header_tmp', 'colspan': '20', 'class': 'over_header center'})[::8]

        with open('../../sqlGenerated/InsertPlayerGame_' + gameID + '.sql', 'w') as file:
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
                            player_data = {"playerID": playerID}

                            # Find and store the values for the specified data-stat attributes
                            for stat in ["mp", "fg", "fga", "fg_pct", "fg3", "fg3a", "fg3_pct", "ft", "fta", "ft_pct", "orb", "drb", "trb", "ast", "stl", "blk", "tov", "pf", "pts", "plus_minus"]:
                                stat_value = player_row.find('td', {'data-stat': stat})
                                stat_text = stat_value.text.strip() if stat_value else 'N/A'
                                player_data[stat] = stat_text

                            # Generate the SQL INSERT statement
                            # Corrected the line below to properly join the gameID
                            columns = ', '.join(['id_game'] + list(player_data.keys()))
                            values = ', '.join([gameID] + [f"'{value}'" for value in player_data.values()])
                            sql_insert = f"INSERT INTO your_table_name ({columns}) VALUES ({values});\n"

                            # Write the SQL INSERT statement to the file
                            file.write(sql_insert)
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

        with open('../../sqlGenerated/InsertGame_' + gameID + '.sql', 'w') as file:
            file.write(
                f"INSERT INTO game "
                f"(id_game, id_away_team, id_home_team, date, type) "
                f"VALUES "
                f"('{gameID}', '{away_team_id}', '{home_team_id}', 'TO_DATE({game_date})', '{type}');\n"
            )
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")


def getPlayerInfo(url):

    response = requests.get(url)

    if response.status_code == 200:

        soup = BeautifulSoup(response.text, 'html.parser')

        ############ PLAYER INFO ############

        team_tag = soup.find('a', {'class': 'poptip default', 'data-tip': lambda x: x and '2024' in x})

        teamYears = team_tag.get('data-tip') if team_tag and '2024' in team_tag.get('data-tip', '') else None
        team = teamYears.split(',')[0] if teamYears else None
        teamID = teams.nba_teams[team] if team else None

        player_tag = soup.find('div', class_='breadcrumbs').find('strong')
        player_name = player_tag.text.strip()

        line = soup.find('div', {'id': 'info', 'class': 'players'})

        position_line = soup.find('h3', string=lambda s: s and 'position' in s.lower())

        p_tag = position_line.find_next('p')

        position = p_tag.text.strip()
        position = position.replace('.','')

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
        contractYearList= []
        contractOptionList = []
        contractLength = None
        contractMoney = None

        comments = soup.find_all(string=lambda text: isinstance(text, Comment) and '<div class="section_content" id="div_contract">' in text)

        for comment in comments:
            comment_soup = BeautifulSoup(comment, 'html.parser')

            ############ CONTRACT INFO ############

            li_element = comment_soup.find('li', string=lambda text: 'Signed' in text)
            contract = li_element.text.strip()
            contract = contract.split(' ')[1]

            if contract != 'rookie' and contract != 'extension' and contract != 'two-way':
                contractLength = contract.split('/')[0].replace('-yr', '')
                contractLength = int(contractLength)
                contractMoney = contract.split('/')[1].replace('$', '').replace('M', '000000')
                contractMoney = float(contractMoney)
            else:
                if contract == 'extension':
                    contractLength = 'extension'
                    contractMoney = 'extension'
                elif contract == 'rookie':
                    contractLength = 'rookie'
                    contractMoney = 'rookie'
                elif contract == 'two-way':
                    contractLength = 'two-way'
                    contractMoney = 'two-way'
                else:
                    contractLength = None
                    contractMoney = None


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
        last_name = name_parts[-1]

        playerID = url.split('/')[-1].replace('.html', '')
        playerID = primaryKeys[playerID]

        for i, salary in enumerate(salaryList, start=1):
            sql_insert = (
            f"INSERT INTO contract_year "
            f"(id_contract_year, id_contract, year, money, option) "
            f"VALUES "
            f"({i}, '{playerID}', '{yearList[i - 1]}', '{salary}', '{contractOptionList[i]}');\n"
            )
            contractYearList.append(sql_insert)

        contractInfo = (
            f"INSERT INTO player_contract "
            f"(id_contract, money, length, "
            f"VALUES "
            f"({playerID}, '{contractMoney}', '{contractLength}');\n"
        )

        # Define the SQL insert statement
        playerInfo = (
            f"INSERT INTO player "
            f"(id_player, id_team, id_contract, id_player_stats, name, surname, date_of_birth, nationality, position, height) "
            f"VALUES "
            f"({playerID}, '{teamID}', '{playerID}', '{playerID}', {first_name}, '{last_name}', '{date_of_birth}', '{country}', '{position}' '{height}');\n"
        )
        print(first_name, last_name)
        return playerInfo, contractInfo, contractYearList
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")



# Example usage
getGameInserts('https://www.basketball-reference.com/boxscores/202312020CHO.html', '202312020CHO')
#getPlayerInfo("https://www.basketball-reference.com/players/a/antetgi01.html")