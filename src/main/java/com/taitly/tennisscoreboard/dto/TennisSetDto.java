package com.taitly.tennisscoreboard.dto;

public record TennisSetDto(
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,
        int firstPlayerGames,
        int secondPlayerGames,
        boolean tieBreakActive,
        TennisTieBreakDto tieBreak
) {
}