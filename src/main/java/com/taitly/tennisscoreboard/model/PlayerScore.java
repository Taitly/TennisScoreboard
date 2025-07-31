package com.taitly.tennisscoreboard.model;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerScore {
    private Player player;

    private int setsWon = 0;
    private int gamesWon = 0;
    private TennisPoint currentPoint = TennisPoint.ZERO;
    private int tieBreakPoints = 0;

    public PlayerScore(Player player) {
        this.player = player;
    }

    public void updateSetsWon(int setsWon) {
        this.setsWon = setsWon;
    }

    public void updateGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void updateCurrentPoint(TennisPoint point) {
        this.currentPoint = point != null ? point : TennisPoint.ZERO;
    }

    public void updateTieBreakPoints(int points) {
        this.tieBreakPoints = points;
    }
}