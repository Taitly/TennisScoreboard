package com.taitly.tennisscoreboard.dto;

public record MatchDto(
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,
        PlayerDto winner
) {
}