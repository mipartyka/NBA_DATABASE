import requests
from bs4 import BeautifulSoup, NavigableString

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

        # Find the line containing 'Pronunciation'
        pronunciation_line = soup.find('strong', text='Pronunciation')

        position_line = soup.find(lambda tag: tag.name == 'strong' and 'Position:' in tag.get_text())

        # Find the player's position
        if position_line is not None:
            next_sibling = position_line.next_sibling
            while isinstance(next_sibling, NavigableString):
                next_sibling = next_sibling.next_sibling
            position = next_sibling.get_text(strip=True)
        else:
            print("No element found with 'Position:'.")
            position = None
        # Find the player name
        player_name = pronunciation_line.find_next('strong').text.strip()

        # Find the player's height and weight
        height_weight = pronunciation_line.find_next('p').find_next('p').text.strip()
        height = height_weight.split(',')[0].strip()
        weight = height_weight.split(',')[1].strip()

        # Find the player's date of birth
        date_of_birth = pronunciation_line.find_next('span', {'id': 'necro-birth'}).get('data-birth')
        # Print the extracted information
        print(f"Name: {player_name}")
        print(f"Position: {position}")
        print(f"Height: {height}")
        print(f"Weight: {weight}")
        print(f"Date of birth: {date_of_birth}")
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
# Example usage
#getPlayerGameInserts('https://www.basketball-reference.com/boxscores/202312020CHO.html', '202312020CHO')
getPlayerInfo("https://www.basketball-reference.com/players/g/goberru01.html")