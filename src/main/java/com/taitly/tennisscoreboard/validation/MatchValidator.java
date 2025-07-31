package com.taitly.tennisscoreboard.validation;

import com.taitly.tennisscoreboard.exception.MatchNotFoundException;
import com.taitly.tennisscoreboard.model.MatchScore;
import com.taitly.tennisscoreboard.service.OngoingMatchesService;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MatchValidator {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getINSTANCE();

    public void validateMatchExists(UUID matchId) {
        MatchScore matchScore = ongoingMatchesService.getMatchScore(matchId);

        if (matchScore == null) {
            throw new MatchNotFoundException("Match not found");
        }
    }
}