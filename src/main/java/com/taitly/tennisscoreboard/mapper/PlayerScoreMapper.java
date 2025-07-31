package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.PlayerScoreDto;
import com.taitly.tennisscoreboard.model.PlayerScore;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PlayerMapper.class)
public interface PlayerScoreMapper {
    PlayerScoreMapper INSTANCE = Mappers.getMapper(PlayerScoreMapper.class);

    PlayerScoreDto toDto(PlayerScore playerScore);

    PlayerScore toEntity(PlayerScoreDto playerScoreDto);
}