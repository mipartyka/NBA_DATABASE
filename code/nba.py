import requests
import nbaTeams as teams
from bs4 import BeautifulSoup

us_states = [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut',
    'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa',
    'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan',
    'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
    'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma',
    'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas',
    'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
]

# Assume primaryKeys is a dictionary mapping player names to IDs
# Initialize an empty dictionary if not already defined
primaryKeys = {}

# Open the file in read mode
with open('../IDMap.txt', 'r') as f:
    for line in f:
        # Split the line into key and value
        key, value = line.strip().split(': ')
        # Remove the curly braces from the value
        value = value.strip('{}')
        # Add the key-value pair to the dictionary
        primaryKeys[key] = int(value)

def getPlayerGameInserts(url, gameID):
    # Send a GET request to the URL
    response = requests.get(url)

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.text, 'html.parser')

        # Find every 8th <th> element for "Basic Box Score Stats"
        basic_stats_headers = soup.find_all('th', {'data-stat': 'header_tmp', 'colspan': '20', 'class': 'over_header center'})[::8]

        with open('InsertPlayerGame_' + gameID + '.sql', 'w') as file:
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
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")


def getPlayerInfo(url):
    # Send a GET request to the URL
    response = requests.get(url)

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.text, 'html.parser')

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

        height_tag = line.find_all_next('span')[1]
        height = height_tag.text.strip()

        country_tag = soup.select_one('a[href*="/friv/birthplaces.fcgi?country="]')
        country = country_tag.text.strip()
        if country in us_states:
            country = country + ' USA'

        date_of_birth = line.find_next('span', {'id': 'necro-birth'}).get('data-birth')

        name_parts = player_name.split()

        first_name = name_parts[0]
        last_name = name_parts[-1]

        playerID = url.split('/')[-1].replace('.html', '')
        playerID = primaryKeys[playerID]

        # Define the SQL insert statement
        sql_insert = (
            f"INSERT INTO your_table_name "
            f"(playerID, firstName, lastName, team, teamID, position, country, height, dateOfBirth) "
            f"VALUES "
            f"({playerID}, '{first_name}', '{last_name}', '{team}', {teamID}, '{position}', '{country}', '{height}', '{date_of_birth}');\n"
        )
        return sql_insert
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
# Example usage
#getPlayerGameInserts('https://www.basketball-reference.com/boxscores/202312020CHO.html', '202312020CHO')
getPlayerInfo("https://www.basketball-reference.com/players/c/cabocbr01.html")