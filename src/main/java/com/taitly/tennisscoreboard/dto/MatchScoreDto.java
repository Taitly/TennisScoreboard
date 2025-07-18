package com.taitly.tennisscoreboard.dto;

import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.model.TennisPoint;

public record MatchScoreDto(
        Player firstPlayer,
        Player secondPlayer,

        int firstPlayerSets,
        int secondPlayerSets,

        int firstPlayerGames,
        int secondPlayerGames,

        TennisPoint firstPlayerPoint,
        TennisPoint secondPlayerPoint,

        int firstPlayerTieBreakPoints,
        int secondPlayerTieBreakPoints
) {
}