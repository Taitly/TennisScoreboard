package com.taitly.tennisscoreboard.dto;

import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.model.TennisPoint;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchScoreDto {
    private Player firstPlayer;
    private Player secondPlayer;

    private int firstPlayerSets;
    private int secondPlayerSets;

    private int firstPlayerGames;
    private int secondPlayerGames;

    private TennisPoint firstPlayerPoint;
    private TennisPoint secondPlayerPoint;

    private int firstPlayerTieBreakPoints;
    private int secondPlayerTieBreakPoints;
}