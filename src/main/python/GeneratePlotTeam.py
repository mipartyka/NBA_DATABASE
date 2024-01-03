import sys
import psycopg2
import pandas as pd
import matplotlib.pyplot as plt

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
df = pd.read_sql(f'SELECT id_team_stats, {column1}, {column2} FROM team_stats', conn)

# Create a plot
plt.scatter(df[column1], df[column2])

# Annotate points
for i in range(len(df)):
    plt.annotate(df['id_team_stats'].iloc[i], (df[column1].iloc[i], df[column2].iloc[i]))

plt.xlabel(column1)
plt.ylabel(column2)
plt.show()
# Close the connection
conn.close()