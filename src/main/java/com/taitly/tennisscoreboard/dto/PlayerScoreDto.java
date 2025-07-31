package com.taitly.tennisscoreboard.dto;

import com.taitly.tennisscoreboard.model.TennisPoint;

public record PlayerScoreDto(
        PlayerDto player,
        int setsWon,
        int gamesWon,
        TennisPoint currentPoint,
        int tieBreakPoints
) {
}