package com.taitly.tennisscoreboard.dto;

import java.util.List;

public record MatchScoreDto(
        PlayerScoreDto firstPlayerScore,
        PlayerScoreDto secondPlayerScore,
        List<TennisSetDto> sets,
        PlayerDto winner
) {
}