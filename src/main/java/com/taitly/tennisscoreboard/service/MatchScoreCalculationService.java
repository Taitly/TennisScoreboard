package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.model.MatchScore;
import lombok.Getter;

public class MatchScoreCalculationService {
    @Getter
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private MatchScoreCalculationService() {

    }

    public void playerScores(MatchScore matchScore, Player player) {
        matchScore.pointWonBy(player);
    }
}