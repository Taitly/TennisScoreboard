package com.taitly.tennisscoreboard.dto;

public record TennisTieBreakDto(
        int firstPlayerTieBreakPoints,
        int secondPlayerTieBreakPoints
) {
}