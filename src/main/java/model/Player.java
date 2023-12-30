package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;

@Getter
@AllArgsConstructor
public class Player {
    private Integer id;
    private String name;
    private String lastName;
    private String team;
    private Time minutes;
    private Double points;
    private Double rebounds;
    private Double assists;
    private Double steals;
    private Double blocks;
    private Double fgMade;
    private Double fgAttempted;
    private Double fgPercentage;
    private Double threePMade;
    private Double threePAttempted;
    private Double threePPercentage;
    private Double ftMade;
    private Double ftAttempted;
    private Double ftPercentage;
    private Double offRebounds;
    private Double defRebounds;
    private Double turnovers;
    private Double fouls;
    private Double plusMinus;
}
