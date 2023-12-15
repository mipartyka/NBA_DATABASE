import nbaTeams as nt

reversedTeams = {v: k for k, v in nt.nba_teams.items()}

open('../../sqlGenerated/TeamInserts.sql', 'w').close()

with open('../../sqlGenerated/TeamInserts.sql', 'a') as file:
    for i in range(1, 31):
        team = reversedTeams[i]
        file.write(f"INSERT INTO team "
                   f"(id_team, id_team_stats, coach, name, city, arena, conference, division, short_name) "
                   f"VALUES "
                   f"({i}, {i}, '{nt.nba_coaches[i]}', '{team}', '{nt.team_cities[i]}', '{nt.nba_arenas[i]}', "
                   f"'{nt.division_conference[nt.nba_divisions[i]]}', '{nt.nba_divisions[i]}', "
                   f"'{nt.team_short_names[i]}');\n")
