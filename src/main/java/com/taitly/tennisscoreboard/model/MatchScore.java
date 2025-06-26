package com.taitly.tennisscoreboard.model;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchScore {
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