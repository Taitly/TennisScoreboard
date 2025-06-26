package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    private final static Map<UUID, MatchScoreDto> currentMatches = new ConcurrentHashMap<>();

    @Getter
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    public MatchScoreDto getMatchScore(UUID uuid) {
        return currentMatches.get(uuid);
    }

    public UUID setMatchScore(MatchScoreDto matchScoreDto) {
        UUID uuid = UUID.randomUUID();

        currentMatches.put(uuid, matchScoreDto);
        return uuid;
    }

    public void removeMatchScore(UUID uuid) {
        currentMatches.remove(uuid);
    }

    private OngoingMatchesService() {

    }
}