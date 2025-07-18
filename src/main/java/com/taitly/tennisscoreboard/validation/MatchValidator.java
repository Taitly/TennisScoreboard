package com.taitly.tennisscoreboard.validation;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.exception.MatchNotFoundException;
import com.taitly.tennisscoreboard.service.OngoingMatchesService;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MatchValidator {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getINSTANCE();

    public void validateMatchExists(UUID matchId) {
        MatchScoreDto matchScoreDto = ongoingMatchesService.getMatchScore(matchId);

        if (matchScoreDto == null) {
            throw new MatchNotFoundException("Match not found");
        }
    }
}