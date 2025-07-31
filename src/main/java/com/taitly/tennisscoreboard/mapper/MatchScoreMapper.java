package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.model.MatchScore;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlayerScoreMapper.class, PlayerMapper.class, TennisSetMapper.class, TennisTieBreakMapper.class})
public interface MatchScoreMapper {
    MatchScoreMapper INSTANCE = Mappers.getMapper(MatchScoreMapper.class);

    MatchScoreDto toDto(MatchScore matchScore);

    MatchScore toEntity(MatchScoreDto matchScoreDto);
}