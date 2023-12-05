import requests
from bs4 import BeautifulSoup
import string
import time

id = 0
player_map = {}

letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'y', 'z']


for letter in letters:
    url = f'https://www.basketball-reference.com/players/{letter}/'
    time.sleep(1) 

    # Send a GET request to the URL
    response = requests.get(url)

    # Check if the request was successful (status code 200)
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.text, 'html.parser')

        # Find all player rows
        player_rows = soup.find_all('tr')

        # Initialize a map to store data-append-csv values as keys and corresponding numbers as values
        


        for player_row in player_rows:
            # Find the <strong> tag within the player row
            strong_tag = player_row.find('strong')

            # If <strong> tag is found, extract the player name
            if strong_tag:
                player_name = strong_tag.text.strip()

                # Find the data-append-csv value
                data_append_csv = player_row.find('th', {'data-stat': 'player'}).get('data-append-csv')

                # Check if data-append-csv exists
                if data_append_csv:
                    # Add the data-append-csv value to the map with a placeholder number (you can replace it with actual numbers)
                    player_map[data_append_csv] = {
                        id
                    }
                    id += 1

        # Print the map
    else:
        print(f"Failed to retrieve the webpage. Status code: {response.status_code}")

with open('output.txt', 'w') as f:
    for key, value in player_map.items():
        # Write the key-value pair to the file
        f.write(f"{key}: {value}\n")
