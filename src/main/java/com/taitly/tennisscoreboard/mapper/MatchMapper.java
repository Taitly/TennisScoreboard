package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.MatchDto;
import com.taitly.tennisscoreboard.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "player1", target = "firstPlayer")
    @Mapping(source = "player2", target = "secondPlayer")
    MatchDto toDto(Match match);
}