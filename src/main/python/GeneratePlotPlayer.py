import sys
import psycopg2
import pandas as pd
import plotly.express as px

db_name = 'cetphhnl'
db_user = 'cetphhnl'
db_password = 'aAGfNp5piy3_W3aKzF05IuesJLQg44Li'
db_host = 'dumbo.db.elephantsql.com'
db_port = '5432'

# Get column names from command line arguments
column1 = sys.argv[1]
column2 = sys.argv[2]

# Connect to the database
conn = psycopg2.connect(
    dbname=db_name,
    user=db_user,
    password=db_password,
    host=db_host,
    port=db_port
)

# Create a cursor
cur = conn.cursor()

# Set the schema
cur.execute("SET search_path TO nba_project")

# Query the database
df = pd.read_sql(f'SELECT p."name" || \' \' || p.surname as player_name, ps."{column1}", ps."{column2}" FROM player_stats_advanced ps JOIN player p on p.id_player_stats = ps.id_player_stats', conn)

# Create a plot
fig = px.scatter(df, x=column1, y=column2, hover_data=['player_name'])
fig.show()

# Close the connection
conn.close()