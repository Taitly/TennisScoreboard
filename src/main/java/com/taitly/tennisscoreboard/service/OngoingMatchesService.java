package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.mapper.MatchScoreMapper;
import com.taitly.tennisscoreboard.model.MatchScore;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchesService {
    @Getter
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private final MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;
    private final static Map<UUID, MatchScore> currentMatches = new ConcurrentHashMap<>();

    private OngoingMatchesService() {

    }

    public MatchScoreDto getMatchScore(UUID uuid) {
        MatchScore matchScore = currentMatches.get(uuid);
        return matchScoreMapper.toDto(matchScore);
    }

    public UUID setMatchScore(MatchScoreDto matchScoreDto) {
        UUID uuid = UUID.randomUUID();
        MatchScore matchScore = matchScoreMapper.toEntity(matchScoreDto);
        currentMatches.put(uuid, matchScore);
        return uuid;
    }

    public void updateMatchScore(UUID uuid, MatchScoreDto matchScoreDto) {
        MatchScore matchScore = MatchScoreMapper.INSTANCE.toEntity(matchScoreDto);
        currentMatches.put(uuid, matchScore);
    }

    public void removeMatchScore(UUID uuid) {
        currentMatches.remove(uuid);
    }
}