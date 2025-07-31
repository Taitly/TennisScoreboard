package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.exception.MatchNotFoundException;
import com.taitly.tennisscoreboard.model.MatchScore;
import com.taitly.tennisscoreboard.model.TennisSet;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    @Getter
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getINSTANCE();
    private final static Map<UUID, MatchScore> currentMatches = new ConcurrentHashMap<>();

    private OngoingMatchesService() {

    }

    public MatchScore getMatchScore(UUID uuid) {
        MatchScore matchScore = currentMatches.get(uuid);
        if (matchScore == null) {
            throw new MatchNotFoundException("Match not found");
        }
        return matchScore;
    }

    public UUID setMatchScore(MatchScore matchScore) {
        UUID uuid = UUID.randomUUID();

        Player firstPlayer = matchScore.getFirstPlayerScore().getPlayer();
        Player secondPlayer = matchScore.getSecondPlayerScore().getPlayer();

        matchScore.getSets().add(new TennisSet(firstPlayer, secondPlayer));

        currentMatches.put(uuid, matchScore);
        return uuid;
    }

    public void updateMatchScore(UUID uuid, Player player) {
        MatchScore matchScore = currentMatches.get(uuid);
        if (matchScore == null) {
            throw new MatchNotFoundException("Match not found");
        }
        matchScoreCalculationService.playerScores(matchScore, player);
    }

    public void removeMatchScore(UUID uuid) {
        currentMatches.remove(uuid);
    }
}